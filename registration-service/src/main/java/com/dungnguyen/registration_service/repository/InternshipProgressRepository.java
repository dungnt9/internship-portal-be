package com.dungnguyen.registration_service.repository;

import com.dungnguyen.registration_service.entity.InternshipProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InternshipProgressRepository extends JpaRepository<InternshipProgress, Integer> {
    // No custom methods needed for now
}