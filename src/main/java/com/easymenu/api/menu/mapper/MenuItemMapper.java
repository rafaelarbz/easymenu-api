package com.easymenu.api.menu.mapper;

import com.easymenu.api.menu.dto.MenuItemDTO;
import com.easymenu.api.menu.entity.MenuItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MenuItemMapper {
    @Mapping(target = "menuCategory", ignore = true)
    @Mapping(target = "menuItemRelations", ignore = true)
    @Mapping(target = "orderItemRelations", ignore = true)
    @Mapping(target = "enterprise", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    MenuItem toEntity(MenuItemDTO menuItemDTO);

    MenuItemDTO toDTO(MenuItem menuItem);
}
