package com.sas.dhop.site.service;

import com.sas.dhop.site.dto.request.ChoreographerFiltersRequest;
import com.sas.dhop.site.dto.request.ChoreographerRequest;
import com.sas.dhop.site.dto.response.ChoreographerFiltersResponse;
import com.sas.dhop.site.dto.response.ChoreographerResponse;
import java.util.List;

public interface ChoreographerService {

    ChoreographerResponse updateChoreographer(Integer choreographyId, ChoreographerRequest choreographerRequest);

    ChoreographerResponse removeChoreographer(Integer choreographyId);

    ChoreographerResponse getChoreographerById(Integer choreographyId);

    List<ChoreographerResponse> getAllChoreography();

    public List<ChoreographerFiltersResponse> getAllChoreographersFilters(
            ChoreographerFiltersRequest choreographerFiltersRequest);

    ChoreographerResponse getChoreographerBySubscriptionStatus(Integer id);

    ChoreographerResponse getAllChoreographerBySubscriptionStatus();
}
