package com.easymenu.api.menu.service;

import com.easymenu.api.menu.dto.MenuDTO;
import com.easymenu.api.menu.dto.MenuItemDTO;

import java.util.List;

public interface MenuService {
    List<MenuDTO> findAllByEnterprise(Long id);
    List<MenuItemDTO> findAvailableMenuItemsByMenu(Long id);
    MenuDTO findMenuById(Long id);
    MenuDTO createMenu(MenuDTO menuDTO);
    MenuDTO updateMenu(Long id, MenuDTO menuDTO);
    void disableMenu(Long id, Long enterpriseId);
    void deleteMenu(Long id, Long enterpriseId);
}
