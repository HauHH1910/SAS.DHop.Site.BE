package com.sas.dhop.site.exception;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

@Component
@Slf4j(topic = "[GraphQL Exception Resolver]")
public class GraphQLExceptionResolver extends DataFetcherExceptionResolverAdapter {

    @Override
    protected GraphQLError resolveToSingleError(Throwable ex, DataFetchingEnvironment env) {
        log.error("[GraphQL Exception] - [{}]", ex.getMessage());

        if (ex instanceof BusinessException businessException) {
            ErrorConstant errorConstant = businessException.getErrorConstant();
            return GraphqlErrorBuilder.newError()
                    .message(errorConstant.getMessage())
                    .errorType(mapToGraphQLErrorType(errorConstant))
                    .location(env.getField().getSourceLocation())
                    .path(env.getExecutionStepInfo().getPath())
                    .build();
        }

        if (ex instanceof AccessDeniedException) {
            return GraphqlErrorBuilder.newError()
                    .message("Không có quyền truy cập")
                    .errorType(ErrorType.FORBIDDEN)
                    .location(env.getField().getSourceLocation())
                    .path(env.getExecutionStepInfo().getPath())
                    .build();
        }

        // Handle any other runtime exceptions
        return GraphqlErrorBuilder.newError()
                .message("Lỗi hệ thống")
                .errorType(ErrorType.INTERNAL_ERROR)
                .location(env.getField().getSourceLocation())
                .path(env.getExecutionStepInfo().getPath())
                .build();
    }

    private ErrorType mapToGraphQLErrorType(ErrorConstant errorConstant) {
        return switch (errorConstant.getHttpStatusCode().value()) {
            case 400 -> ErrorType.BAD_REQUEST;
            case 401, 403 -> ErrorType.FORBIDDEN;
            case 404 -> ErrorType.NOT_FOUND;
            default -> ErrorType.INTERNAL_ERROR;
        };
    }
} 