package com.easymenu.api.order.repository;

import com.easymenu.api.order.entity.Command;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommandRepository extends JpaRepository<Command, Long> {
    List<Command> findAllByEnterpriseId(Long id);
    Optional<Command> findByIdAndEnterpriseId(Long id, Long enterpriseId);

}
