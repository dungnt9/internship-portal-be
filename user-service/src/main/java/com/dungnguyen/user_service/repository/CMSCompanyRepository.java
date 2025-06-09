package com.dungnguyen.user_service.repository;

import com.dungnguyen.user_service.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CMSCompanyRepository extends JpaRepository<Company, Integer> {
}
