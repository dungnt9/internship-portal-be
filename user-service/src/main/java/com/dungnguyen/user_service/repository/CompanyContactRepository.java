package com.dungnguyen.user_service.repository;

import com.dungnguyen.user_service.entity.CompanyContact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyContactRepository extends JpaRepository<CompanyContact, Integer> {
    @Query("SELECT cc FROM CompanyContact cc JOIN cc.company c WHERE cc.authUserId = ?1 AND c.isVerified = true AND c.isLinked = true AND cc.deletedAt IS NULL")
    Optional<CompanyContact> findByAuthUserId(Integer authUserId);

    @Query("SELECT cc FROM CompanyContact cc JOIN cc.company c WHERE cc.company.id = :companyId AND c.isVerified = true AND c.isLinked = true AND cc.deletedAt IS NULL")
    List<CompanyContact> findByCompanyId(@Param("companyId") Integer companyId);

    @Query("SELECT cc FROM CompanyContact cc WHERE cc.company.id = :companyId AND cc.deletedAt IS NULL")
    List<CompanyContact> findActiveByCompanyId(@Param("companyId") Integer companyId);
}