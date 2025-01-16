package com.easymenu.api.menu.entity;

import com.easymenu.api.enterprise.entity.Enterprise;
import com.easymenu.api.shared.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Table(name = "menu_categories")
@Entity
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class MenuCategory extends BaseEntity {
    @Column(nullable = false)
    private String name;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enterprise_id", insertable = false, updatable = false)
    private Enterprise enterprise;
}
