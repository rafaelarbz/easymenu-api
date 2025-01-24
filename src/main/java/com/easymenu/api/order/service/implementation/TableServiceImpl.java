package com.easymenu.api.order.service.implementation;

import com.easymenu.api.order.dto.CommonRequestBatchDTO;
import com.easymenu.api.order.dto.CommonRequestDTO;
import com.easymenu.api.order.dto.CommonResponseDTO;
import com.easymenu.api.order.dto.TableQRCodeDTO;
import com.easymenu.api.order.entity.Table;
import com.easymenu.api.order.mapper.TableMapper;
import com.easymenu.api.order.repository.TableRepository;
import com.easymenu.api.order.service.CommonService;
import com.easymenu.api.shared.service.QRCodeService;
import com.google.zxing.WriterException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;

import static com.easymenu.api.shared.utils.StringsUtil.URL_API;

public class TableServiceImpl implements CommonService {
    @Autowired TableRepository tableRepository;
    @Autowired TableMapper tableMapper;
    @Autowired QRCodeService qrCodeService;

    @Override
    public CommonResponseDTO findEntityById(Long id) {
        Table table = findById(id);
        return tableMapper.toDTO(table);
    }

    @Override
    public List<CommonResponseDTO> findAllByEnterprise(Long id) {
        return tableRepository.findAllByEnterpriseId(id)
            .stream()
            .map(tableMapper::toDTO)
            .toList();
    }

    @Override
    public List<CommonResponseDTO> createEntityBatch(CommonRequestBatchDTO commonRequestBatchDTO) {
        List<Table> tables = commonRequestBatchDTO.codes()
            .stream()
            .map(c -> buildTable(c, commonRequestBatchDTO.enterpriseId()))
            .toList();

        List<Table> createdTables = tableRepository.saveAll(tables);

        return createdTables
            .stream()
            .map(tableMapper::toDTO)
            .toList();
    }

    @Override
    public CommonResponseDTO updateEntity(Long id, Long enterpriseId, CommonRequestDTO commonRequestDTO) {
        Table table = findByIdAndEnterprise(id, enterpriseId);
        table.setCode(commonRequestDTO.code());
        Table updatedTable = tableRepository.save(table);
        return tableMapper.toDTO(updatedTable);
    }

    @Override
    public void changeAvailability(Long id, Long enterpriseId) {
        Table table = findByIdAndEnterprise(id, enterpriseId);
        table.setAvailable(!table.isAvailable());
        tableRepository.save(table);
    }

    public byte[] generateQRCode(Long id, TableQRCodeDTO tableQRCodeDTO) throws IOException, WriterException {
        Table table = findById(id);
        String content = String.format(
            URL_API + "/menu/enterprise/%d/all?tableId=%d",
            table.getEnterpriseId(),
            table.getId());
        return qrCodeService.generateQRCodeImage(
            content,
            tableQRCodeDTO.width(),
            tableQRCodeDTO.height());
    }

    private Table findById(Long id) {
        return tableRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Table not found with ID: " + id));
    }

    private Table findByIdAndEnterprise(Long id, Long enterpriseId) {
        return tableRepository.findByIdAndEnterpriseId(id, enterpriseId)
            .orElseThrow(() -> new EntityNotFoundException("Table not found with ID #" + id + " for enterprise ID #" + enterpriseId));
    }

    private Table buildTable(String code, Long enterpriseId) {
        return Table.builder()
            .code(code)
            .enterpriseId(enterpriseId)
            .build();
    }
}
