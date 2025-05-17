package com.dungnguyen.registration_service.repository;

import com.dungnguyen.registration_service.entity.InternshipApplicationDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InternshipApplicationDetailRepository extends JpaRepository<InternshipApplicationDetail, Integer> {

    @Query("SELECT d FROM InternshipApplicationDetail d WHERE d.position.id IN :positionIds AND d.status = :status AND d.deletedAt IS NULL")
    List<InternshipApplicationDetail> findByPositionIdInAndStatus(
            @Param("positionIds") List<Integer> positionIds,
            @Param("status") InternshipApplicationDetail.Status status);

    @Query("SELECT d FROM InternshipApplicationDetail d WHERE d.position.id IN :positionIds AND d.deletedAt IS NULL")
    List<InternshipApplicationDetail> findByPositionIdIn(@Param("positionIds") List<Integer> positionIds);

    @Query("SELECT d FROM InternshipApplicationDetail d WHERE d.application.id = :applicationId AND d.status = :status AND d.deletedAt IS NULL ORDER BY d.preferenceOrder ASC")
    List<InternshipApplicationDetail> findByApplicationIdAndStatusOrderByPreferenceOrder(
            @Param("applicationId") Integer applicationId,
            @Param("status") InternshipApplicationDetail.Status status);
}