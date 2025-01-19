package com.easymenu.api.menu.entity;

import com.easymenu.api.enterprise.entity.Enterprise;
import com.easymenu.api.shared.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Table(name = "menus")
@Entity
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class Menu extends BaseEntity {
    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean active;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enterprise_id", insertable = false, updatable = false)
    private Enterprise enterprise;

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MenuItemRelation> menuItemRelations;
}
