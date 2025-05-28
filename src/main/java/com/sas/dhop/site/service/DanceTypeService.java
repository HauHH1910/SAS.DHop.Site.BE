package com.sas.dhop.site.service;

import com.sas.dhop.site.dto.request.DanceTypeRequest;
import com.sas.dhop.site.dto.response.DanceTypeResponse;
import com.sas.dhop.site.model.DanceType;
import java.util.List;

public interface DanceTypeService {

    List<DanceTypeResponse> getAllDanceType();

    DanceTypeResponse createDanceType(DanceTypeRequest request);

    DanceType findDanceType(Integer type);
}
