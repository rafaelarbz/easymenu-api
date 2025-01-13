package com.easymenu.api.menu.repository;

import com.easymenu.api.menu.entity.MenuCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MenuCategoryRepository extends JpaRepository<MenuCategory, Long> {
    List<MenuCategory> findAllByEnterpriseId(Long id);
    Optional<MenuCategory> findByIdAndEnterpriseId(Long id, Long enterpriseId);
}
