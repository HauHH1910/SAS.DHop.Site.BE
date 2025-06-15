package com.sas.dhop.site.service;

import com.sas.dhop.site.dto.request.CreateUserRequest;
import com.sas.dhop.site.dto.request.UpdateUserRequest;
import com.sas.dhop.site.dto.response.UserResponse;
import com.sas.dhop.site.model.User;
import java.util.List;

public interface UserService {

  List<UserResponse> getAllUser();

  UserResponse getUser(Integer id);

  void deleteUser(Integer id);

  UserResponse updateUser(Integer id, UpdateUserRequest request);

  UserResponse createUser(CreateUserRequest request);

  UserResponse getUserInfo();

  UserResponse findUser(String email);

  User getLoginUser();

  User findUserById(Integer userId);
}
