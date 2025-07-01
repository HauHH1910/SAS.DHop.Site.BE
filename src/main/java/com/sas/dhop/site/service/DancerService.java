package com.sas.dhop.site.service;

import com.sas.dhop.site.dto.request.DancerRequest;
import com.sas.dhop.site.dto.request.DancersFiltersRequest;
import com.sas.dhop.site.dto.response.DancerResponse;
import com.sas.dhop.site.dto.response.DancersFiltersResponse;
import java.util.List;

public interface DancerService {
    DancerResponse updateDancer(Integer dancerId, DancerRequest dancerRequest);

    DancerResponse removeDancer(Integer dancerId);

    DancerResponse getDancerById(Integer dancerId);

    DancerResponse getDancerByDanceType(Integer dancerId);

    List<DancerResponse> getAllDancers();

    List<DancersFiltersResponse> getAllDancersFilters(DancersFiltersRequest dancersFiltersRequest);

    DancerResponse getDancerBySubscriptionStatus(Integer id);

    DancerResponse getAllDancerBySubscriptionStatus();
}
