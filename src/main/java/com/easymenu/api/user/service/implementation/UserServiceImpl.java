package com.easymenu.api.user.service.implementation;

import com.easymenu.api.user.dto.UserDTO;
import com.easymenu.api.user.entity.User;
import com.easymenu.api.user.mapper.UserMapper;
import com.easymenu.api.user.repository.UserRepository;
import com.easymenu.api.user.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private UserRepository userRepository;
    @Autowired private UserMapper userMapper;

    @Override
    public List<UserDTO> findAllByEnterprise(Long id) {
        return userRepository.findAllByEnterpriseId(id)
            .stream()
            .map(userMapper::toDTO)
            .toList();
    }

    @Override
    public UserDTO findUserById(Long id) {
        User user = findById(id);
        return userMapper.toDTO(user);
    }

    @Override
    @Transactional
    public UserDTO createUser(UserDTO userDTO) {
        validateCpf(userDTO.cpf(), null);

        User user = userMapper.toEntity(userDTO);
        user.setPassword(passwordEncoder.encode(userDTO.password()));

        User createdUser = userRepository.save(user);
        return userMapper.toDTO(createdUser);
    }

    @Override
    @Transactional
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        User user = findById(id);
        validateCpf(userDTO.cpf(), id);
        updateFields(user, userDTO);
        User updatedUser = userRepository.save(user);
        return userMapper.toDTO(updatedUser);
    }

    @Override
    @Transactional
    public void disableUser(Long id, Long enterpriseId) {
        User user = findByIdAndEnterprise(id, enterpriseId);
        user.setActive(false);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteUser(Long id, Long enterpriseId) {
        User user = findByIdAndEnterprise(id, enterpriseId);
        userRepository.deleteById(user.getId());
    }

    private User findById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + id));
    }

    private User findByCpf(String cpf) {
        return userRepository.findByCpf(cpf).orElse(null);
    }

    private User findByIdAndEnterprise(Long id, Long enterpriseId) {
        return userRepository.findByIdAndEnterpriseId(id, enterpriseId)
            .orElseThrow(() -> new EntityNotFoundException("User not found with ID #" + id + " for enterprise ID #" + enterpriseId));
    }

    private void validateCpf(String cpf, Long id) {
        User user = findByCpf(cpf);
        if (Objects.isNull(user)) {
            return;
        }
        if (Objects.nonNull(id) && id.equals(user.getId())) {
            return;
        }
        throw new IllegalArgumentException("CPF already exists: " + cpf);
    }

    private void updateFields(User user, UserDTO userDTO) {
        if (Objects.nonNull(userDTO.fullName())) {
            user.setFullName(userDTO.fullName());
        }

        if (Objects.nonNull(userDTO.cpf())) {
            user.setCpf(userDTO.cpf());
        }

        if (Objects.nonNull(userDTO.role())) {
            user.setRole(userMapper.mapStringToEnum(userDTO.role()));
        }

        if (Objects.nonNull(userDTO.password())) {
            user.setPassword(passwordEncoder.encode(userDTO.password()));
        }

        if (Objects.nonNull(userDTO.enterpriseId())) {
            user.setEnterpriseId(userDTO.enterpriseId());
        }
    }
}
