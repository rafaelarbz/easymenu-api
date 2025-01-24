package com.easymenu.api.menu.service.implementation;

import com.easymenu.api.menu.dto.MenuDTO;
import com.easymenu.api.menu.dto.MenuItemDTO;
import com.easymenu.api.menu.entity.Menu;
import com.easymenu.api.menu.mapper.MenuItemMapper;
import com.easymenu.api.menu.mapper.MenuMapper;
import com.easymenu.api.menu.repository.MenuItemRepository;
import com.easymenu.api.menu.repository.MenuRepository;
import com.easymenu.api.menu.service.MenuService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Transactional(readOnly = true)
public class MenuServiceImpl implements MenuService {
    @Autowired private MenuRepository menuRepository;
    @Autowired private MenuItemRepository menuItemRepository;
    @Autowired private MenuMapper menuMapper;
    @Autowired private MenuItemMapper menuItemMapper;

    @Override
    public List<MenuDTO> findAllByEnterprise(Long id) {
        return menuRepository.findAllByEnterpriseId(id)
            .stream()
            .map(menuMapper::toDTO)
            .toList();
    }

    @Override
    public List<MenuItemDTO> findAvailableMenuItemsByMenu(Long id) {
        return menuRepository.findAvailableMenuItemsByMenuId(id)
            .stream()
            .map(menuItemMapper::toDTO)
            .toList();
    }

    @Override
    public MenuDTO findMenuById(Long id) {
        Menu menu = findById(id);
        return menuMapper.toDTO(menu);
    }

    @Override
    @Transactional
    public MenuDTO createMenu(MenuDTO menuDTO) {
        Menu menu = menuRepository.save(menuMapper.toEntity(menuDTO));
        return menuMapper.toDTO(menu);
    }

    @Override
    @Transactional
    public MenuDTO updateMenu(Long id, MenuDTO menuDTO) {
        Menu menu = findByIdAndEnterprise(id, menuDTO.enterpriseId());
        updateFields(menu, menuDTO);
        Menu updatedMenu = menuRepository.save(menu);
        return menuMapper.toDTO(updatedMenu);
    }

    @Override
    @Transactional
    public void disableMenu(Long id, Long enterpriseId) {
        Menu menu = findByIdAndEnterprise(id, enterpriseId);
        menu.setActive(false);
        menuRepository.save(menu);
    }

    @Override
    @Transactional
    public void deleteMenu(Long id, Long enterpriseId) {
        Menu menu = findByIdAndEnterprise(id, enterpriseId);
        menuRepository.deleteById(menu.getId());
    }

    private Menu findById(Long id) {
        return menuRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Menu not found with ID: " + id));
    }

    private Menu findByIdAndEnterprise(Long id, Long enterpriseId) {
        return menuRepository.findByIdAndEnterpriseId(id, enterpriseId)
            .orElseThrow(() -> new EntityNotFoundException("Menu not found with ID #" + id + " for enterprise ID #" + enterpriseId));
    }

    private void updateFields(Menu menu, MenuDTO menuDTO) {
        if (Objects.nonNull(menuDTO.name())) {
            menu.setName(menuDTO.name());
        }

        if (Objects.nonNull(menuDTO.description())) {
            menu.setDescription(menuDTO.description());
        }

        if (Objects.nonNull(menuDTO.enterpriseId())) {
            menu.setEnterpriseId(menuDTO.enterpriseId());
        }
    }
}
