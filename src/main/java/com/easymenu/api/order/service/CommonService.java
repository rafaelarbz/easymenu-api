package com.easymenu.api.order.service;

import com.easymenu.api.order.dto.CommonQRCodeDTO;
import com.easymenu.api.order.dto.CommonRequestBatchDTO;
import com.easymenu.api.order.dto.CommonRequestDTO;
import com.easymenu.api.order.dto.CommonResponseDTO;
import com.google.zxing.WriterException;

import java.io.IOException;
import java.util.List;

public interface CommonService {
    CommonResponseDTO findEntityById(Long id);
    List<CommonResponseDTO> findAllByEnterprise(Long id);
    List<CommonResponseDTO> createEntityBatch(CommonRequestBatchDTO commonRequestBatchDTO);
    CommonResponseDTO updateEntity(Long id, Long enterpriseId, CommonRequestDTO commonRequestDTO);
    byte[] generateQRCode(Long id, CommonQRCodeDTO commonQRCodeDTO) throws IOException, WriterException;
    void changeAvailability(Long id, Long enterpriseId);
}
