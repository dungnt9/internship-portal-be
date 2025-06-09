package com.dungnguyen.user_service.repository;

import com.dungnguyen.user_service.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Integer> {
    @Override
    @Query("SELECT c FROM Company c WHERE c.isVerified = true AND c.isLinked = true AND c.deletedAt IS NULL")
    List<Company> findAll();

    @Override
    @Query("SELECT c FROM Company c WHERE c.id = ?1 AND c.isVerified = true AND c.isLinked = true AND c.deletedAt IS NULL")
    Optional<Company> findById(Integer id);
}