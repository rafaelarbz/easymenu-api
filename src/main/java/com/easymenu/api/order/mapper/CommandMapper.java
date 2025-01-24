package com.easymenu.api.order.mapper;

import com.easymenu.api.order.dto.CommonResponseDTO;
import com.easymenu.api.order.entity.Command;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CommandMapper {
    CommonResponseDTO toDTO(Command command);
}
