package com.easymenu.api.order.entity;

import com.easymenu.api.enterprise.entity.Enterprise;
import com.easymenu.api.shared.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Table(name = "commands")
@Entity
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class Command extends BaseEntity {
    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean available;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enterprise_id", insertable = false, updatable = false)
    private Enterprise enterprise;
}
