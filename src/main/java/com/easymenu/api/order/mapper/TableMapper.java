package com.easymenu.api.order.mapper;

import com.easymenu.api.order.dto.CommonResponseDTO;
import com.easymenu.api.order.entity.Table;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TableMapper {
    CommonResponseDTO toDTO(Table table);
}
