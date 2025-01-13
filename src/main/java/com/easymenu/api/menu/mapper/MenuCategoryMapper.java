package com.easymenu.api.menu.mapper;

import com.easymenu.api.menu.dto.MenuCategoryDTO;
import com.easymenu.api.menu.entity.MenuCategory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MenuCategoryMapper {
    @Mapping(target = "enterprise", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    MenuCategory toEntity(MenuCategoryDTO menuCategoryDTO);

    MenuCategoryDTO toDTO(MenuCategory menuCategory);
}
