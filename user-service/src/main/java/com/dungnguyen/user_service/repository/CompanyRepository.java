package com.dungnguyen.user_service.repository;

import com.dungnguyen.user_service.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Integer> {
    Optional<Company> findByName(String name);
    Optional<Company> findByTaxCode(String taxCode);
    Optional<Company> findByNameIgnoreCase(String name);
}