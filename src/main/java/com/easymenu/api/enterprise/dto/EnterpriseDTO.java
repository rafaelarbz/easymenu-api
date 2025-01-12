package com.easymenu.api.enterprise.dto;

import com.easymenu.api.enterprise.enums.TaxIdentifierSize;
import com.easymenu.api.enterprise.enums.TaxIdentifierType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Objects;


public record EnterpriseDTO(
        Long id,

        @NotBlank(message = "Name must not be empty.")
        String name,

        @NotBlank(message = "Tax identifier type must be either CPF or CNPJ.")
        String taxIdentifierType,

        @NotBlank(message = "Tax identifier must have 11 or 14 digits.")
        @Size(min = 11, max = 14, message = "Tax identifier must be between 11 and 14 characters long.")
        String taxIdentifier,

        Long parentId
) {
        public interface Creation {}
        public interface Update {}

        public EnterpriseDTO {
                if(Objects.nonNull(taxIdentifierType) && Objects.nonNull(taxIdentifier)) {
                        validateTaxIdentifier(taxIdentifierType, taxIdentifier);
                }
        }

        private void validateTaxIdentifier(String taxIdentifierType, String taxIdentifier) {
                if (taxIdentifierType.equals(TaxIdentifierType.CNPJ.name())
                && taxIdentifier.length() != TaxIdentifierSize.CNPJ.size) {
                        throw new IllegalArgumentException("CNPJ must be 14 characters");
                } else if (taxIdentifierType.equals(TaxIdentifierType.CPF.name())
                && taxIdentifier.length() != TaxIdentifierSize.CPF.size) {
                        throw new IllegalArgumentException("CPF must be 11 characters");
                }
        }
}