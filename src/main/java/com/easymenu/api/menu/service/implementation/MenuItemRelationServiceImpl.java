package com.easymenu.api.menu.service.implementation;

import com.easymenu.api.menu.entity.MenuItemRelation;
import com.easymenu.api.menu.repository.MenuItemRelationRepository;
import com.easymenu.api.menu.repository.MenuItemRepository;
import com.easymenu.api.menu.repository.MenuRepository;
import com.easymenu.api.menu.service.MenuItemRelationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MenuItemRelationServiceImpl implements MenuItemRelationService {
    @Autowired MenuRepository menuRepository;
    @Autowired MenuItemRepository menuItemRepository;
    @Autowired MenuItemRelationRepository menuItemRelationRepository;

    @Override
    public void addRelation(Long menuId, Long menuItemId) {
        menuExists(menuId);
        menuItemExists(menuItemId);
        MenuItemRelation menuItemRelation = buildRelation(menuId, menuItemId);
        menuItemRelationRepository.save(menuItemRelation);
    }

    @Override
    public void changeMenuItemAvailability(Long menuId, Long menuItemId) {
        MenuItemRelation menuItemRelation = relationExists(menuId, menuItemId);
        menuItemRelation.setAvailable(!menuItemRelation.getAvailable());
        menuItemRelationRepository.save(menuItemRelation);
    }

    private MenuItemRelation relationExists(Long menuId, Long menuItemId) {
        return menuItemRelationRepository.findByMenuIdAndMenuItemId(menuId, menuItemId)
            .orElseThrow(() -> new IllegalArgumentException("Relation not found."));
    }

    private void menuExists(Long id) {
        menuRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Menu not found with ID: " + id));
    }

    private void menuItemExists(Long id) {
        menuItemRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Menu item not found with ID: " + id));
    }

    private MenuItemRelation buildRelation(Long menuId, Long menuItemId) {
        return MenuItemRelation.builder()
            .menuId(menuId)
            .menuItemId(menuItemId)
            .build();
    }
}
