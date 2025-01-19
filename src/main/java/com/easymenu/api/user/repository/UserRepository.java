package com.easymenu.api.user.repository;

import com.easymenu.api.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEnterpriseId(Long id);
    Optional<User> findByIdAndEnterpriseId(Long id, Long enterpriseId);
    Optional<User> findByCpf(String cpf);
}
