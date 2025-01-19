package com.easymenu.api.user.mapper;

import com.easymenu.api.user.dto.UserDTO;
import com.easymenu.api.user.entity.User;
import com.easymenu.api.user.enums.UserRole;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "enterprise", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    User toEntity(UserDTO userDTO);

    UserDTO toDTO(User user);

    default UserRole mapStringToEnum(String userRole) {
        return UserRole.valueOf(userRole.toUpperCase());
    }
    default String mapEnumToString(UserRole userRole) {
        return userRole.name();
    }
}
