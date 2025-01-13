package com.easymenu.api.menu.repository;

import com.easymenu.api.menu.entity.Menu;
import com.easymenu.api.menu.entity.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {
    List<Menu> findAllByEnterpriseId(Long id);
    Optional<Menu> findByIdAndEnterpriseId(Long id, Long enterpriseId);
    @Query("SELECT r.menuItem FROM MenuItemRelation r WHERE r.menu.id = :menuId AND r.available = true")
    List<MenuItem> findAvailableMenuItemsByMenuId(@Param("menuId") Long menuId);
}
