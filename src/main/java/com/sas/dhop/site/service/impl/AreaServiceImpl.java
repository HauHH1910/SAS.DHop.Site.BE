package com.sas.dhop.site.service.impl;

import com.sas.dhop.site.constant.AreaStatus;
import com.sas.dhop.site.constant.RolePrefix;
import com.sas.dhop.site.dto.request.AreaRequest;
import com.sas.dhop.site.dto.response.AreaResponse;
import com.sas.dhop.site.exception.BusinessException;
import com.sas.dhop.site.exception.ErrorConstant;
import com.sas.dhop.site.model.Area;
import com.sas.dhop.site.model.Status;
import com.sas.dhop.site.model.User;
import com.sas.dhop.site.model.enums.RoleName;
import com.sas.dhop.site.repository.AreaRepository;
import com.sas.dhop.site.service.AreaService;
import com.sas.dhop.site.service.AuthenticationService;
import com.sas.dhop.site.service.StatusService;
import com.sas.dhop.site.service.UserService;
import com.sas.dhop.site.util.mapper.AreaMapper;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
@Slf4j(topic = "[Area Service]")
public class AreaServiceImpl implements AreaService {
	private final AreaRepository areaRepository;
	private final UserService userService;
	private final AreaMapper areaMapper;
	private final StatusService statusService;
	private final AuthenticationService authenticationService;

	@Override
	public List<AreaResponse> getAllArea() {

		boolean isAdmin = authenticationService.authenticationChecking(RolePrefix.ADMIN_PREFIX);

		Status status = statusService.findStatusOrCreated(AreaStatus.ACTIVATED_AREA);

		List<Area> areas = isAdmin ? areaRepository.findAll() : areaRepository.findAreaByStatus(status.getStatusName());

		return areas.stream().map(areaMapper::mapToAreaResponse).collect(Collectors.toList());
	}

	@Override
	public AreaResponse getAreaById(int areaId) {
		Area area = areaRepository.findAreaByIdAndStatus(areaId, AreaStatus.ACTIVATED_AREA);
		if (area == null) {
			throw new BusinessException(ErrorConstant.AREA_NOT_FOUND);
		}
		return areaMapper.mapToAreaResponse(area);
	}

	@Override
	public AreaResponse createNewArea(AreaRequest areaRequest) {
		User currentUser = userService.getLoginUser();

		boolean hasRole = currentUser.getRoles().stream()
				.anyMatch(role -> role.getName().equals(RoleName.STAFF) || role.getName().equals(RoleName.ADMIN));

		if (!hasRole) {
			throw new BusinessException(ErrorConstant.ROLE_ACCESS_DENIED);
		}

		boolean isExists = areaRepository.existsByLocation(areaRequest.city().trim(), areaRequest.district().trim(),
				areaRequest.ward().trim());

		if (isExists) {
			throw new BusinessException(ErrorConstant.AREA_ALREADY_EXISTS);
		}

		Status status = statusService.findStatusOrCreated(AreaStatus.ACTIVATED_AREA);
		Area area = areaMapper.mapToArea(areaRequest);
		area.setStatus(status);
		Area savedArea = areaRepository.save(area);
		return areaMapper.mapToAreaResponse(savedArea);
	}

	@Override
	public AreaResponse updateArea(int areaId, AreaRequest areaRequest) {

		User currentUser = userService.getLoginUser();

		boolean isStaff = currentUser.getRoles().stream().anyMatch(role -> role.getName() == RoleName.STAFF);

		if (!isStaff) {
			throw new BusinessException(ErrorConstant.ROLE_ACCESS_DENIED);
		}

		Area area = areaRepository.findById(areaId)
				.orElseThrow(() -> new BusinessException(ErrorConstant.AREA_NOT_FOUND));

		checkAreaDuplicate(areaId, areaRequest.city(), areaRequest.district(), areaRequest.ward());

		area.setCity(areaRequest.city().trim());
		area.setDistrict(areaRequest.district().trim());
		area.setWard(areaRequest.ward().trim());

		Area updateArea = areaRepository.save(area);

		return areaMapper.mapToAreaResponse(updateArea);
	}

	@Override
	public AreaResponse updateAreaStatus(int areaId) {
		User currentUser = userService.getLoginUser();

		boolean isAdminOrStaff = currentUser.getRoles().stream()
				.anyMatch(role -> role.getName() == RoleName.ADMIN || role.getName() == RoleName.STAFF);

		if (!isAdminOrStaff) {
			throw new BusinessException(ErrorConstant.ROLE_ACCESS_DENIED);
		}

		Area area = areaRepository.findById(areaId)
				.orElseThrow(() -> new BusinessException(ErrorConstant.AREA_NOT_FOUND));

		Status status = area.getStatus();
		if (status.getStatusName().equals(AreaStatus.ACTIVATED_AREA)) {
			status.setStatusName(String.valueOf(AreaStatus.INACTIVE_AREA));
		} else {
			status.setStatusName(String.valueOf(AreaStatus.ACTIVATED_AREA));
		}
		return areaMapper.mapToAreaResponse(area);
	}

	private void checkAreaDuplicate(Integer areaId, String city, String district, String ward) {
		String normalizedCity = city.trim();
		String normalizedDistrict = district.trim();
		String normalizedWard = ward.trim();

		List<Area> allAreas = areaRepository.findAll();

		boolean isDuplicate = allAreas.stream().anyMatch(area -> area.getCity().equalsIgnoreCase(normalizedCity)
				&& area.getDistrict().equalsIgnoreCase(normalizedDistrict)
				&& area.getWard().equalsIgnoreCase(normalizedWard) && (areaId == null || area.getId() != areaId));

		if (isDuplicate) {
			throw new BusinessException(ErrorConstant.AREA_ALREADY_EXISTS);
		}
	}
}
