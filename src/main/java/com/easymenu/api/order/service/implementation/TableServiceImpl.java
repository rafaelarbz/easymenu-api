package com.easymenu.api.order.service.implementation;

import com.easymenu.api.order.dto.CommonRequestBatchDTO;
import com.easymenu.api.order.dto.CommonRequestDTO;
import com.easymenu.api.order.dto.CommonResponseDTO;
import com.easymenu.api.order.dto.CommonQRCodeDTO;
import com.easymenu.api.order.entity.Table;
import com.easymenu.api.order.mapper.TableMapper;
import com.easymenu.api.order.repository.TableRepository;
import com.easymenu.api.order.service.CommonService;
import com.easymenu.api.shared.service.QRCodeService;
import com.easymenu.api.shared.service.WebSocketService;
import com.google.zxing.WriterException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

import static com.easymenu.api.shared.utils.StringsUtil.URL_API;

@Service
@Primary
@Qualifier("tableService")
@Transactional(readOnly = true)
public class TableServiceImpl implements CommonService {
    @Autowired private TableRepository tableRepository;
    @Autowired private TableMapper tableMapper;
    @Autowired private QRCodeService qrCodeService;
    @Autowired private WebSocketService webSocketService;

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
    @Transactional
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
    @Transactional
    public CommonResponseDTO updateEntity(Long id, Long enterpriseId, CommonRequestDTO commonRequestDTO) {
        Table table = findByIdAndEnterprise(id, enterpriseId);
        table.setCode(commonRequestDTO.code());
        Table updatedTable = tableRepository.save(table);

        CommonResponseDTO updatedTableDTO = tableMapper.toDTO(updatedTable);

        webSocketService.sendTableUpdated(updatedTableDTO);
        return updatedTableDTO;
    }

    @Override
    @Transactional
    public void changeAvailability(Long id, Long enterpriseId) {
        Table table = findByIdAndEnterprise(id, enterpriseId);
        table.setAvailable(!table.isAvailable());
        tableRepository.save(table);

        webSocketService.sendTableUpdated(tableMapper.toDTO(table));
    }

    @Override
    public byte[] generateQRCode(Long id, CommonQRCodeDTO commonQRCodeDTO) throws IOException, WriterException {
        Table table = findById(id);
        String content = String.format(
            URL_API + "/menu/enterprise/%d/all?tableId=%d",
            table.getEnterpriseId(),
            table.getId());
        return qrCodeService.generateQRCodeImage(
            content,
            commonQRCodeDTO.width(),
            commonQRCodeDTO.height());
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
