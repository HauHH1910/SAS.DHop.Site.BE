package com.sas.dhop.site.util.mapper;

import com.sas.dhop.site.dto.response.ComplainResponse;
import com.sas.dhop.site.model.Complain;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ComplainMapper {

    ComplainResponse mapToComplain(Complain complain);
}
