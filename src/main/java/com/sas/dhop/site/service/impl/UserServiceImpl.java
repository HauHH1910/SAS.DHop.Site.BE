package com.sas.dhop.site.service.impl;

import com.sas.dhop.site.dto.request.CreateUserRequest;
import com.sas.dhop.site.dto.request.UpdateUserRequest;
import com.sas.dhop.site.dto.response.UserResponse;
import com.sas.dhop.site.exception.BusinessException;
import com.sas.dhop.site.exception.ErrorConstant;
import com.sas.dhop.site.model.User;
import com.sas.dhop.site.repository.UserRepository;
import com.sas.dhop.site.service.UserService;
import com.sas.dhop.site.util.mapper.UserMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "[User Service]")
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public List<UserResponse> getAllUser() {
        log.info("[get all user]");
        return userRepository.findAll().stream()
                .map(userMapper::mapToUserResponse)
                .toList();
    }

    @Override
    public UserResponse getUser(Integer id) {
        log.info("[get user] - [{}]", id);
        return userMapper.mapToUserResponse(findUserById(id));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(Integer id) {
        log.info("[delete user] - [{}]", id);
        userRepository.delete(findUserById(id));
    }

    @Override
    public UserResponse updateUser(Integer id, UpdateUserRequest request) {
        User user = findUserById(id);
        userMapper.mapToUpdateUser(user, request);
        log.info("[update user] - [{}]", id);
        return userMapper.mapToUserResponse(user);
    }

    @Override
    public UserResponse createUser(CreateUserRequest request) {
        log.info("[create user] - [{}]", request.email());
        return userMapper.mapToUserResponse(userRepository.save(userMapper.mapToUser(request)));
    }

    @Override
    public UserResponse getUserInfo() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("[get user info] - [{}]", email);

        return userMapper.mapToUserResponse(userRepository
                .findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorConstant.USER_NOT_FOUND)));
    }

    @Override
    public UserResponse findUser(String email) {
        return userMapper.mapToUserResponse(userRepository
                .findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorConstant.EMAIL_NOT_FOUND)));
    }

    private User findUserById(Integer id) {
        log.info("[find user] - [{}]", id);
        return userRepository.findById(id).orElseThrow(() -> new BusinessException(ErrorConstant.USER_NOT_FOUND));
    }
}
