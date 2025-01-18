package com.easymenu.api.enterprise.repository;

import com.easymenu.api.enterprise.entity.Enterprise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnterpriseRepository extends JpaRepository<Enterprise, Long> {
    List<Enterprise> findAllByParentId(Long id);
    Optional<Enterprise> findByTaxIdentifier(String taxIdentifier);
}
