package com.easymenu.api.enterprise.enums;

public enum TaxIdentifierSize {
    CPF(11),
    CNPJ(14);

    public final int size;

    private TaxIdentifierSize(int size) {
        this.size = size;
    }
}
