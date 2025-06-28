package com.sas.dhop.site.util.mapper;

import com.sas.dhop.site.dto.request.SubscriptionRequest;
import com.sas.dhop.site.dto.response.SubscriptionResponse;
import com.sas.dhop.site.model.Subscription;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface SubscriptionMapper {

    SubscriptionResponse mapToResponse(Subscription request);

    Subscription mapToModel(SubscriptionRequest request);

    void mapToUpdateUser(@MappingTarget Subscription subscription, SubscriptionRequest request);
}
