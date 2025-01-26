package com.easymenu.api.order.repository;

import com.easymenu.api.order.entity.OrderItemRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRelationRepository extends JpaRepository<OrderItemRelation, Long> {
    void deleteByOrderIdAndMenuItemId(Long orderId, Long itemId);
    List<OrderItemRelation> findAllByOrderId(Long id);
}
