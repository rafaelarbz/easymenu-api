package com.easymenu.api.order.service;

import com.easymenu.api.order.dto.CommonRequestBatchDTO;
import com.easymenu.api.order.dto.CommonRequestDTO;
import com.easymenu.api.order.dto.CommonResponseDTO;

import java.util.List;

public interface CommonService {
    CommonResponseDTO findEntityById(Long id);
    List<CommonResponseDTO> findAllByEnterprise(Long id);
    List<CommonResponseDTO> createEntityBatch(CommonRequestBatchDTO commonRequestBatchDTO);
    CommonResponseDTO updateEntity(Long id, Long enterpriseId, CommonRequestDTO commonRequestDTO);
    void changeAvailability(Long id, Long enterpriseId);
}
