package com.sas.dhop.site.service;

import com.sas.dhop.site.dto.request.PermissionRequest;
import com.sas.dhop.site.dto.response.PermissionResponse;
import java.util.List;

public interface PermissionService {

    PermissionResponse createPermission(PermissionRequest request);

    List<PermissionResponse> getAll();

    void deletePermission(Integer id);
}
