package com.easymenu.api.order.entity;

import com.easymenu.api.enterprise.entity.Enterprise;
import com.easymenu.api.shared.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@jakarta.persistence.Table(name = "tables")
@Entity
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class Table extends BaseEntity {
    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean available;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enterprise_id", insertable = false, updatable = false)
    private Enterprise enterprise;
}
