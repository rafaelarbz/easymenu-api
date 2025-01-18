package com.easymenu.api.enterprise.mapper;

import com.easymenu.api.enterprise.dto.EnterpriseDTO;
import com.easymenu.api.enterprise.entity.Enterprise;
import com.easymenu.api.enterprise.enums.TaxIdentifierType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EnterpriseMapper {
    @Mapping(target = "parentEnterprise", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Enterprise toEntity(EnterpriseDTO enterpriseDTO);

    EnterpriseDTO toDTO(Enterprise enterprise);

    default TaxIdentifierType mapStringToEnum(String taxIdentifierType) {
        return TaxIdentifierType.valueOf(taxIdentifierType.toUpperCase());
    }
    default String mapEnumToString(TaxIdentifierType taxIdentifierType) {
        return taxIdentifierType.name();
    }
}
