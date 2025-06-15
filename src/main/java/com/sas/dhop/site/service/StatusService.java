package com.sas.dhop.site.service;

import com.sas.dhop.site.model.Status;

public interface StatusService {

	Status findStatusOrCreated(String status);

	Status getStatus(String status);
}
