package com.easymenu.api.menu.service;

import com.easymenu.api.menu.dto.MenuCategoryDTO;

import java.util.List;

public interface MenuCategoryService {
    List<MenuCategoryDTO> findAllByEnterprise(Long id);
    MenuCategoryDTO findMenuCategoryById(Long id);
    MenuCategoryDTO createMenuCategory(MenuCategoryDTO menuCategoryDTOme);
    MenuCategoryDTO updateMenuCategory(Long id, MenuCategoryDTO menuCategoryDTO);
    void deleteMenuCategory(Long id, Long enterpriseId);
}
