package com.sas.dhop.site.service.impl;

import com.sas.dhop.site.dto.request.PermissionRequest;
import com.sas.dhop.site.dto.response.PermissionResponse;
import com.sas.dhop.site.model.Permission;
import com.sas.dhop.site.repository.PermissionRepository;
import com.sas.dhop.site.service.PermissionService;
import com.sas.dhop.site.util.mapper.PermissionMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "[Permission Service]")
public class PermissionServiceImpl implements PermissionService {

	private final PermissionRepository permissionRepository;
	private final PermissionMapper permissionMapper;

	@Override
	public PermissionResponse createPermission(PermissionRequest request) {
		Permission permission = permissionMapper.toPermission(request);
		permission = permissionRepository.save(permission);
		return permissionMapper.toPermissionResponse(permission);
	}

	@Override
	public List<PermissionResponse> getAll() {
		return permissionRepository.findAll().stream().map(permissionMapper::toPermissionResponse).toList();
	}

	@Override
	public void deletePermission(Integer id) {
		permissionRepository.deleteById(id);
	}
}
