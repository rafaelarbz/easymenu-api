package com.easymenu.api.enterprise.service;

import com.easymenu.api.enterprise.dto.EnterpriseDTO;
import com.easymenu.api.enterprise.entity.Enterprise;
import com.easymenu.api.enterprise.mapper.EnterpriseMapper;
import com.easymenu.api.enterprise.repository.EnterpriseRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class EnterpriseServiceImpl implements EnterpriseService {
    @Autowired private EnterpriseRepository enterpriseRepository;
    @Autowired private EnterpriseMapper enterpriseMapper;

    @Override
    public List<EnterpriseDTO> findAllEnterprises() {
        return enterpriseRepository.findAll()
            .stream()
            .map(enterpriseMapper::toDTO)
            .toList();
    }

    @Override
    public List<EnterpriseDTO> findAllByParentId(Long id) {
        return enterpriseRepository.findAllByParentId(id)
            .stream()
            .map(enterpriseMapper::toDTO)
            .toList();
    }

    @Override
    public EnterpriseDTO createEnterprise(EnterpriseDTO enterpriseDTO) {
        Enterprise createdEnterprise =
            enterpriseRepository.save(enterpriseMapper.toEntity(enterpriseDTO));
        return enterpriseMapper.toDTO(createdEnterprise);
    }

    @Override
    public EnterpriseDTO updateEnterprise(Long id, EnterpriseDTO enterpriseDTO) {
        Enterprise enterprise = findById(id);
        isParentValid(enterpriseDTO);
        updateFields(enterprise, enterpriseDTO);
        Enterprise updatedEnterprise = enterpriseRepository.save(enterprise);
        return enterpriseMapper.toDTO(updatedEnterprise);
    }

    @Override
    public EnterpriseDTO findEnterpriseById(Long id) {
        Enterprise enterprise = findById(id);
        return enterpriseMapper.toDTO(enterprise);
    }

    @Override
    public void deleteEnterprise(Long id) {
        Enterprise enterprise = findById(id);
        enterpriseRepository.deleteById(enterprise.getId());
    }

    private Enterprise findById(Long id) {
        return enterpriseRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Enterprise not found with ID: " + id));
    }

    private void isParentValid(EnterpriseDTO enterpriseDTO) {
        if (Objects.nonNull(enterpriseDTO.parentId())) {
            if (!enterpriseRepository.existsById(enterpriseDTO.parentId())) {
                throw new IllegalArgumentException("Parent enterprise not found with ID: " + enterpriseDTO.parentId());
            }
        }
    }

    private void updateFields(Enterprise enterprise, EnterpriseDTO enterpriseDTO) {
        if (Objects.nonNull(enterpriseDTO.name())) {
            enterprise.setName(enterpriseDTO.name());
        }

        if (Objects.nonNull(enterpriseDTO.taxIdentifierType())) {
            enterprise.setTaxIdentifierType(enterpriseMapper.mapStringToEnum(enterpriseDTO.taxIdentifierType()));
        }

        if (Objects.nonNull(enterpriseDTO.taxIdentifier())) {
            enterprise.setTaxIdentifier(enterpriseDTO.taxIdentifier());
        }

        if (Objects.nonNull(enterpriseDTO.parentId())) {
            enterprise.setParentId(enterpriseDTO.parentId());
        }
    }
}
