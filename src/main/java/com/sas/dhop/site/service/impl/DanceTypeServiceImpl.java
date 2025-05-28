package com.sas.dhop.site.service.impl;

import com.sas.dhop.site.dto.request.DanceTypeRequest;
import com.sas.dhop.site.dto.response.DanceTypeResponse;
import com.sas.dhop.site.exception.BusinessException;
import com.sas.dhop.site.exception.ErrorConstant;
import com.sas.dhop.site.model.DanceType;
import com.sas.dhop.site.repository.DanceTypeRepository;
import com.sas.dhop.site.service.DanceTypeService;
import com.sas.dhop.site.util.mapper.DanceTypeMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "[Dance Type Service]")
public class DanceTypeServiceImpl implements DanceTypeService {

    private final DanceTypeRepository danceTypeRepository;
    private final DanceTypeMapper danceTypeMapper;

    @Override
    public List<DanceTypeResponse> getAllDanceType() {
        return danceTypeRepository.findAll().stream()
                .map(danceTypeMapper::mapToResponse)
                .toList();
    }

    @Override
    public DanceTypeResponse createDanceType(DanceTypeRequest request) {
        if (danceTypeRepository.findByType(request.type()).isPresent()) {
            throw new BusinessException(ErrorConstant.DANCE_TYPE_ALREADY_EXISTS);
        }

        var type = danceTypeRepository.save(DanceType.builder()
                .type(request.type())
                .description(request.description())
                .build());
        return danceTypeMapper.mapToResponse(type);
    }

    @Override
    public DanceType findDanceType(Integer id) {
        return danceTypeRepository
                .findById(id)
                .orElseThrow(() -> new BusinessException(ErrorConstant.DANCE_TYPE_NOT_FOUND));
    }
}
