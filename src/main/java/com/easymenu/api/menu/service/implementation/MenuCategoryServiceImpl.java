package com.easymenu.api.menu.service.implementation;

import com.easymenu.api.menu.dto.MenuCategoryDTO;
import com.easymenu.api.menu.entity.MenuCategory;
import com.easymenu.api.menu.mapper.MenuCategoryMapper;
import com.easymenu.api.menu.repository.MenuCategoryRepository;
import com.easymenu.api.menu.service.MenuCategoryService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class MenuCategoryServiceImpl implements MenuCategoryService {
    @Autowired private MenuCategoryRepository menuCategoryRepository;
    @Autowired private MenuCategoryMapper menuCategoryMapper;

    @Override
    public List<MenuCategoryDTO> findAllByEnterprise(Long id) {
        return menuCategoryRepository.findAllByEnterpriseId(id)
            .stream()
            .map(menuCategoryMapper::toDTO)
            .toList();
    }

    @Override
    public MenuCategoryDTO findMenuCategoryById(Long id) {
        MenuCategory menuCategory = findById(id);
        return menuCategoryMapper.toDTO(menuCategory);
    }

    @Override
    public MenuCategoryDTO createMenuCategory(MenuCategoryDTO menuCategoryDTO) {
        MenuCategory menuCategory =
            menuCategoryRepository.save(menuCategoryMapper.toEntity(menuCategoryDTO));
        return menuCategoryMapper.toDTO(menuCategory);
    }

    @Override
    public MenuCategoryDTO updateMenuCategory(Long id, MenuCategoryDTO menuCategoryDTO) {
        MenuCategory menuCategory = findByIdAndEnterprise(id, menuCategoryDTO.enterpriseId());
        updateFields(menuCategory, menuCategoryDTO);
        MenuCategory updatedMenuCategory = menuCategoryRepository.save(menuCategory);
        return menuCategoryMapper.toDTO(updatedMenuCategory);
    }

    @Override
    public void deleteMenuCategory(Long id, Long enterpriseId) {
        MenuCategory menuCategory = findByIdAndEnterprise(id, enterpriseId);
        menuCategoryRepository.deleteById(menuCategory.getId());
    }

    private MenuCategory findById(Long id) {
        return menuCategoryRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Menu category not found with ID: " + id));
    }

    private MenuCategory findByIdAndEnterprise(Long id, Long enterpriseId) {
        return menuCategoryRepository.findByIdAndEnterpriseId(id, enterpriseId)
            .orElseThrow(() -> new EntityNotFoundException("Menu category not found with ID #" + id + " for enterprise ID #" + enterpriseId));
    }

    private void updateFields(MenuCategory menuCategory, MenuCategoryDTO menuCategoryDTO) {
        if (Objects.nonNull(menuCategoryDTO.name())) {
            menuCategory.setName(menuCategoryDTO.name());
        }

        if (Objects.nonNull(menuCategoryDTO.description())) {
            menuCategory.setDescription(menuCategoryDTO.description());
        }

        if (Objects.nonNull(menuCategoryDTO.enterpriseId())) {
            menuCategory.setEnterpriseId(menuCategoryDTO.enterpriseId());
        }
    }
}
