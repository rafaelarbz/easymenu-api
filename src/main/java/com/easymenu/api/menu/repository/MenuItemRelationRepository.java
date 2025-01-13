package com.easymenu.api.menu.repository;

import com.easymenu.api.menu.entity.MenuItemRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MenuItemRelationRepository extends JpaRepository<MenuItemRelation, Long> {
    Optional<MenuItemRelation> findByMenuIdAndMenuItemId(Long menuId, Long menuItemId);
}
