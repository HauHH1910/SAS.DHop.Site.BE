package com.sas.dhop.site.util.mapper;

import com.sas.dhop.site.dto.response.SubscriptionResponse;
import com.sas.dhop.site.model.Subscription;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SubscriptionMapper {

    @Mapping(target = "status", source = "status.statusName")
    SubscriptionResponse mapToResponse(Subscription request);
}
