package com.easymenu.api.menu.service.implementation;

import com.easymenu.api.menu.dto.MenuItemDTO;
import com.easymenu.api.menu.entity.MenuItem;
import com.easymenu.api.menu.mapper.MenuItemMapper;
import com.easymenu.api.menu.repository.MenuCategoryRepository;
import com.easymenu.api.menu.repository.MenuItemRepository;
import com.easymenu.api.menu.service.MenuItemService;
import com.easymenu.api.shared.service.WebSocketService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Transactional(readOnly = true)
public class MenuItemServiceImpl implements MenuItemService {
    @Autowired private MenuItemRepository menuItemRepository;
    @Autowired private MenuItemMapper menuItemMapper;
    @Autowired private MenuCategoryRepository menuCategoryRepository;
    @Autowired private WebSocketService webSocketService;

    @Override
    public List<MenuItemDTO> findAllByEnterprise(Long id) {
        return menuItemRepository.findAllByEnterpriseId(id)
            .stream()
            .map(menuItemMapper::toDTO)
            .toList();
    }

    @Override
    public List<MenuItemDTO> findAllByMenuCategory(Long id) {
        return menuItemRepository.findAllByMenuCategoryId(id)
            .stream()
            .map(menuItemMapper::toDTO)
            .toList();
    }

    @Override
    public List<MenuItemDTO> findAllByMenuCategoryAndEnterprise(Long menuCategoryId, Long enterpriseId) {
        return menuItemRepository.findAllByMenuCategoryIdAndEnterpriseId(menuCategoryId, enterpriseId)
            .stream()
            .map(menuItemMapper::toDTO)
            .toList();
    }

    @Override
    public MenuItemDTO findMenuItemById(Long id) {
        MenuItem menuItem = findById(id);
        return menuItemMapper.toDTO(menuItem);
    }

    @Override
    @Transactional
    public MenuItemDTO createMenuItem(MenuItemDTO menuItemDTO) {
        menuCategoryExists(menuItemDTO.menuCategoryId());
        MenuItem menuItem =
            menuItemRepository.save(menuItemMapper.toEntity(menuItemDTO));
        return menuItemMapper.toDTO(menuItem);
    }

    @Override
    @Transactional
    public MenuItemDTO updateMenuItem(Long id, MenuItemDTO menuItemDTO) {
        MenuItem menuItem = findByIdAndEnterprise(id, menuItemDTO.enterpriseId());
        menuCategoryExists(menuItem.getMenuCategoryId());

        updateFields(menuItem, menuItemDTO);
        MenuItem updatedMenuItem = menuItemRepository.save(menuItem);

        MenuItemDTO updatedMenuItemDTO = menuItemMapper.toDTO(updatedMenuItem);

        webSocketService.sendMenuItemUpdated(updatedMenuItemDTO);
        return updatedMenuItemDTO;
    }

    @Override
    @Transactional
    public void disableMenuItem(Long id, Long enterpriseId) {
        MenuItem menuItem = findByIdAndEnterprise(id, enterpriseId);
        menuItem.setActive(false);
        menuItemRepository.save(menuItem);

        webSocketService.sendMenuItemUpdated(menuItemMapper.toDTO(menuItem));
    }

    @Override
    @Transactional
    public void deleteMenuItem(Long id, Long enterpriseId) {
        MenuItem menuItem = findByIdAndEnterprise(id, enterpriseId);
        menuItemRepository.deleteById(menuItem.getId());
    }

    private MenuItem findById(Long id) {
        return menuItemRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Menu item not found with ID: " + id));
    }

    private MenuItem findByIdAndEnterprise(Long id, Long enterpriseId) {
        return menuItemRepository.findByIdAndEnterpriseId(id, enterpriseId)
            .orElseThrow(() -> new EntityNotFoundException("Menu item not found with ID #" + id + " for enterprise ID #" + enterpriseId));
    }

    private void menuCategoryExists(Long id) {
        if (!menuCategoryRepository.existsById(id)) {
            throw new IllegalArgumentException("Menu category not found with ID: " + id);
        }
    }

    private void updateFields(MenuItem menuItem, MenuItemDTO menuItemDTO) {
        if (Objects.nonNull(menuItemDTO.name())) {
            menuItem.setName(menuItemDTO.name());
        }

        if (Objects.nonNull(menuItemDTO.description())) {
            menuItem.setDescription(menuItemDTO.description());
        }

        if (Objects.nonNull(menuItemDTO.price())) {
            menuItem.setPrice(menuItemDTO.price());
        }

        if (Objects.nonNull(menuItemDTO.imageUrl())) {
            menuItem.setImageUrl(menuItemDTO.imageUrl());
        }

        if (Objects.nonNull(menuItemDTO.menuCategoryId())) {
            menuItem.setMenuCategoryId(menuItemDTO.menuCategoryId());
        }

        if (Objects.nonNull(menuItemDTO.enterpriseId())) {
            menuItem.setEnterpriseId(menuItemDTO.enterpriseId());
        }
    }
}
