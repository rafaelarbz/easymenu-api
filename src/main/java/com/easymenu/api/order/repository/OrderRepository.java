package com.easymenu.api.order.repository;

import com.easymenu.api.order.dto.OrderItemResponseDTO;
import com.easymenu.api.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByEnterpriseId(Long id);
    List<Order> findAllByEnterpriseIdAndPaidFalse(Long id);
    Optional<Order> findByTableIdAndPaidFalse(Long id);
    Optional<Order> findByCommandIdAndPaidFalse(Long id);
    Optional<Order> findByTableIdAndCommandIdAndPaidFalse(Long tableId, Long commandId);
    Order findFirstByTableIdOrderByIdDesc(Long tableId);
    Order findFirstByCommandIdOrderByIdDesc(Long commandId);

    @Query("""
        SELECT new com.easymenu.api.order.dto.OrderItemResponseDTO(
            r.id,
            m.id,
            m.name,
            r.quantity,
            r.itemPrice
        )
        FROM OrderItemRelation r
        JOIN r.menuItem m
        WHERE r.orderId = :orderId
    """)
    List<OrderItemResponseDTO> findItemsByOrder(Long id);
}
