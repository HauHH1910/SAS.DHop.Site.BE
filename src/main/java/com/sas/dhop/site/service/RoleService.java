package com.sas.dhop.site.service;

import com.sas.dhop.site.dto.request.RoleRequest;
import com.sas.dhop.site.dto.response.RoleResponse;
import java.util.List;

public interface RoleService {

    RoleResponse createRole(RoleRequest request);

    List<RoleResponse> getAll();

    void deleteRole(Integer id);
}
