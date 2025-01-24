package com.easymenu.api.order.repository;

import com.easymenu.api.order.entity.Table;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TableRepository extends JpaRepository<Table, Long> {
    List<Table> findAllByEnterpriseId(Long id);
    Optional<Table> findByIdAndEnterpriseId(Long id, Long enterpriseId);
}
