package com.easymenu.api.enterprise.service;

import com.easymenu.api.enterprise.dto.EnterpriseDTO;

import java.util.List;

public interface EnterpriseService {
    EnterpriseDTO findEnterpriseById(Long id);
    List<EnterpriseDTO> findAllEnterprises();
    List<EnterpriseDTO> findAllByParentId(Long id);
    EnterpriseDTO createEnterprise(EnterpriseDTO enterpriseDTO);
    EnterpriseDTO updateEnterprise(Long id, EnterpriseDTO enterpriseDTO);
    void disableEnterprise(Long id);
    void deleteEnterprise(Long id);
}
