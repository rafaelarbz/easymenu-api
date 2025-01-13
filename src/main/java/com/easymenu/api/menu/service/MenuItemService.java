package com.easymenu.api.menu.service;

import com.easymenu.api.menu.dto.MenuItemDTO;

import java.util.List;

public interface MenuItemService {
    List<MenuItemDTO> findAllByEnterprise(Long id);
    List<MenuItemDTO> findAllByMenuCategory(Long id);
    List<MenuItemDTO> findAllByMenuCategoryAndEnterprise(Long menuCategoryId, Long enterpriseId);
    MenuItemDTO findMenuItemById(Long id);
    MenuItemDTO createMenuItem(MenuItemDTO menuItemDTO);
    MenuItemDTO updateMenuItem(Long id, MenuItemDTO menuItemDTO);
    void deleteMenuItem(Long id, Long enterpriseId);
}
