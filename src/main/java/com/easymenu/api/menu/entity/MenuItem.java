package com.easymenu.api.menu.entity;

import com.easymenu.api.enterprise.entity.Enterprise;
import com.easymenu.api.order.entity.OrderItemRelation;
import com.easymenu.api.shared.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Table(name = "menu_items")
@Entity
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class MenuItem extends BaseEntity {
    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    private String imageUrl;

    @Column(name = "menu_category_id")
    private Long menuCategoryId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_category_id", insertable = false, updatable = false)
    private MenuCategory menuCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enterprise_id", insertable = false, updatable = false)
    private Enterprise enterprise;

    @OneToMany(mappedBy = "menuItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MenuItemRelation> menuItemRelations;

    @OneToMany(mappedBy = "menuItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItemRelation> orderItemRelations;
}
