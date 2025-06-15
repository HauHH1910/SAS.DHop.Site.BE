package com.sas.dhop.site.service;

import com.sas.dhop.site.dto.request.AreaRequest;
import com.sas.dhop.site.dto.response.AreaResponse;
import java.util.List;

public interface AreaService {
  List<AreaResponse> getAllArea();

  AreaResponse getAreaById(int areaId);

  AreaResponse createNewArea(AreaRequest areaRequest);

  AreaResponse updateArea(int areaId, AreaRequest areaRequest);

  AreaResponse updateAreaStatus(int areaId);
}
