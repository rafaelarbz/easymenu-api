package com.easymenu.api.order.entity;

import com.easymenu.api.enterprise.entity.Enterprise;
import com.easymenu.api.order.enums.OrderStatus;
import com.easymenu.api.shared.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.Set;

@jakarta.persistence.Table(name = "orders")
@Entity
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class Order extends BaseEntity {
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean paid;

    @Column(name = "command_id")
    private Long commandId;

    @Column(name = "table_id")
    private Long tableId;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "command_id", insertable = false, updatable = false)
    private Command command;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_id", insertable = false, updatable = false)
    private Table table;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enterprise_id", insertable = false, updatable = false)
    private Enterprise enterprise;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrderItemRelation> orderItemRelations;
}
