package com.easymenu.api.menu.service;

import com.easymenu.api.menu.dto.MenuCategoryDTO;

import java.util.List;

public interface MenuCategoryService {
    List<MenuCategoryDTO> findAllByEnterprise(Long id);
    MenuCategoryDTO findMenuCategoryById(Long id);
    MenuCategoryDTO createMenu(MenuCategoryDTO menuCategoryDTOme);
    MenuCategoryDTO updateMenu(Long id, MenuCategoryDTO menuCategoryDTO);
    void deleteMenu(Long id, Long enterpriseId);
}
