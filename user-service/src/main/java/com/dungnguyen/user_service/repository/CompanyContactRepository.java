package com.dungnguyen.user_service.repository;

import com.dungnguyen.user_service.entity.CompanyContact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyContactRepository extends JpaRepository<CompanyContact, Integer> {
    Optional<CompanyContact> findByAuthUserId(Integer authUserId);
    Optional<CompanyContact> findByEmail(String email);
    Optional<CompanyContact> findByPhone(String phone);
}