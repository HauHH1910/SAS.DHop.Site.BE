package com.sas.dhop.site.service.impl;

import com.sas.dhop.site.exception.BusinessException;
import com.sas.dhop.site.exception.ErrorConstant;
import com.sas.dhop.site.model.Status;
import com.sas.dhop.site.model.enums.StatusType;
import com.sas.dhop.site.repository.StatusRepository;
import com.sas.dhop.site.service.StatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "[Status Service]")
public class StatusServiceImpl implements StatusService {

    private final StatusRepository statusRepository;

    @Override
    public Status findStatusOrCreated(String status) {
        return statusRepository
                .findByStatusName(status)
                .orElseGet(() -> statusRepository.save(Status.builder()
                        .statusName(status)
                        .statusType(StatusType.ACTIVE)
                        .description(status)
                        .build()));
    }

    @Override
    public Status getStatus(String status) {
        return statusRepository
                .findByStatusName(status)
                .orElseThrow(() -> new BusinessException(ErrorConstant.STATUS_NOT_FOUND));
    }
}
