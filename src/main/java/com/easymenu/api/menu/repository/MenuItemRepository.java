package com.easymenu.api.menu.repository;

import com.easymenu.api.menu.entity.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    List<MenuItem> findAllByEnterpriseId(Long id);
    List<MenuItem> findAllByMenuCategoryId(Long id);
    List<MenuItem> findAllByMenuCategoryIdAndEnterpriseId(Long menuCategoryId, Long enterpriseId);
    Optional<MenuItem> findByIdAndEnterpriseId(Long id, Long enterpriseId);
}
