package com.easymenu.api.order.service.implementation;

import com.easymenu.api.order.dto.CommonRequestBatchDTO;
import com.easymenu.api.order.dto.CommonRequestDTO;
import com.easymenu.api.order.dto.CommonResponseDTO;
import com.easymenu.api.order.entity.Command;
import com.easymenu.api.order.mapper.CommandMapper;
import com.easymenu.api.order.repository.CommandRepository;
import com.easymenu.api.order.service.CommonService;
import com.easymenu.api.shared.service.WebSocketService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Qualifier("commandService")
@Transactional(readOnly = true)
public class CommandServiceImpl implements CommonService {
    @Autowired private CommandRepository commandRepository;
    @Autowired private CommandMapper commandMapper;
    @Autowired private WebSocketService webSocketService;

    @Override
    public CommonResponseDTO findEntityById(Long id) {
        Command command = findById(id);
        return commandMapper.toDTO(command);
    }

    @Override
    public List<CommonResponseDTO> findAllByEnterprise(Long id) {
        return commandRepository.findAllByEnterpriseId(id)
            .stream()
            .map(commandMapper::toDTO)
            .toList();
    }

    @Override
    @Transactional
    public List<CommonResponseDTO> createEntityBatch(CommonRequestBatchDTO commonRequestBatchDTO) {
        List<Command> commands = commonRequestBatchDTO.codes()
            .stream()
            .map(c -> buildCommand(c, commonRequestBatchDTO.enterpriseId()))
            .toList();

        List<Command> createdCommands = commandRepository.saveAll(commands);

        return createdCommands
            .stream()
            .map(commandMapper::toDTO)
            .toList();
    }

    @Override
    @Transactional
    public CommonResponseDTO updateEntity(Long id, Long enterpriseId, CommonRequestDTO commonRequestDTO) {
        Command command = findByIdAndEnterprise(id, enterpriseId);
        command.setCode(commonRequestDTO.code());
        Command updatedCommand = commandRepository.save(command);

        CommonResponseDTO updatedCommandDTO = commandMapper.toDTO(updatedCommand);

        webSocketService.sendCommandUpdated(updatedCommandDTO);
        return updatedCommandDTO;
    }

    @Override
    @Transactional
    public void changeAvailability(Long id, Long enterpriseId) {
        Command command = findByIdAndEnterprise(id, enterpriseId);
        command.setAvailable(!command.isAvailable());
        commandRepository.save(command);

        webSocketService.sendCommandUpdated(commandMapper.toDTO(command));
    }

    private Command findById(Long id) {
        return commandRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Command not found with ID: " + id));
    }

    private Command findByIdAndEnterprise(Long id, Long enterpriseId) {
        return commandRepository.findByIdAndEnterpriseId(id, enterpriseId)
            .orElseThrow(() -> new EntityNotFoundException("Command not found with ID #" + id + " for enterprise ID #" + enterpriseId));
    }

    private Command buildCommand(String code, Long enterpriseId) {
        return Command.builder()
            .code(code)
            .enterpriseId(enterpriseId)
            .build();
    }
}
