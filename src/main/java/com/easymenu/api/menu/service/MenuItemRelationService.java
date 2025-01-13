package com.easymenu.api.menu.service;

public interface MenuItemRelationService {
    void addRelation(Long menuId, Long menuItemId);
    void changeMenuItemAvailability(Long menuId, Long menuItemId);
}
