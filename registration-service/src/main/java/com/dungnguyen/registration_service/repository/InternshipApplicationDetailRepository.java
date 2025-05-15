package com.dungnguyen.registration_service.repository;

import com.dungnguyen.registration_service.entity.InternshipApplicationDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InternshipApplicationDetailRepository extends JpaRepository<InternshipApplicationDetail, Integer> { }