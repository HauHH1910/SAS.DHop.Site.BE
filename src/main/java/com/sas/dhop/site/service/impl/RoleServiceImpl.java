package com.sas.dhop.site.service.impl;

import com.sas.dhop.site.dto.request.RoleRequest;
import com.sas.dhop.site.dto.response.RoleResponse;
import com.sas.dhop.site.model.Role;
import com.sas.dhop.site.model.enums.RoleName;
import com.sas.dhop.site.repository.PermissionRepository;
import com.sas.dhop.site.repository.RoleRepository;
import com.sas.dhop.site.service.RoleService;
import com.sas.dhop.site.util.mapper.RoleMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "[Role Service]")
public class RoleServiceImpl implements RoleService {

  private final RoleRepository roleRepository;
  private final RoleMapper roleMapper;
  private final PermissionRepository permissionRepository;

  @Override
  public RoleResponse createRole(RoleRequest request) {
    var role = roleMapper.mapToRole(request);

    var permissions = permissionRepository.findAllByNameIn(request.permissions());

    role.setPermissions(permissions);

    return roleMapper.mapToRoleResponse(roleRepository.save(role));
  }

  @Override
  public List<RoleResponse> getAll() {
    return roleRepository.findAll().stream().map(roleMapper::mapToRoleResponse).toList();
  }

  @Override
  public void deleteRole(Integer id) {
    roleRepository.deleteById(id);
  }

  //    @Override
  //    public void checkRole(Integer id, RoleName... allowedRoles) {
  //        Role role = roleRepository.findById(id)
  //                .orElseThrow(() -> new BusinessException(ErrorConstant.ROLE_NOT_FOUND));
  //
  //        if (allowedRoles.length > 0) {
  //            RoleName actualRole = role.getName();
  //            boolean matched = false;
  //            for (RoleName allowed : allowedRoles) {
  //                if (allowed.equals(actualRole)) {
  //                    matched = true;
  //                    break;
  //                }
  //            }
  //            if (!matched) {
  //                throw new BusinessException(ErrorConstant.ROLE_ACCESS_DENIED);
  //            }
  //        }
  //    }

  @Override
  public Role findByRoleName(RoleName role) {
    return roleRepository
        .findByName(role)
        .orElseGet(() -> roleRepository.save(Role.builder().name(role).build()));
  }
}
