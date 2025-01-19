package com.easymenu.api.user.service;

import com.easymenu.api.user.dto.UserDTO;

import java.util.List;

public interface UserService {
    List<UserDTO> findAllByEnterprise(Long id);
    UserDTO findUserById(Long id);
    UserDTO createUser(UserDTO userDTO);
    UserDTO updateUser(Long id, UserDTO userDTO);
    void disableUser(Long id, Long enterpriseId);
    void deleteUser(Long id, Long enterpriseId);
}
