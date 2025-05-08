package com.porul.product_management.auth.repository;

import com.porul.product_management.auth.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<Users, Integer>
{
    boolean existsByUsername(String username);

    Users findByUsername(String username);

    boolean existsByMailId(String mailId);

    void deleteByUsername(String username);
}
