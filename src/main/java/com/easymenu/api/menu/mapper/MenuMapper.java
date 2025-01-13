package com.easymenu.api.menu.mapper;

import com.easymenu.api.menu.dto.MenuDTO;
import com.easymenu.api.menu.entity.Menu;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MenuMapper {
    @Mapping(target = "enterprise", ignore = true)
    @Mapping(target = "menuItemRelations", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Menu toEntity(MenuDTO menuDTO);

    MenuDTO toDTO(Menu menu);
}
