package com.dungnguyen.user_service.repository;

import com.dungnguyen.user_service.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer> {
    Optional<Admin> findByAuthUserId(Integer authUserId);
}