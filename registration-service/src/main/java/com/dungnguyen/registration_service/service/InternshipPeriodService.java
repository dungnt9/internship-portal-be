package com.dungnguyen.registration_service.service;

import com.dungnguyen.registration_service.entity.InternshipPeriod;
import com.dungnguyen.registration_service.exception.InternshipPeriodNotFoundException;
import com.dungnguyen.registration_service.repository.InternshipPeriodRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class InternshipPeriodService {

    private final InternshipPeriodRepository periodRepository;

    /**
     * Get all active internship periods
     *
     * @return List of InternshipPeriod
     */
    public List<InternshipPeriod> getAllActivePeriods() {
        return periodRepository.findAllActivePeriods();
    }

    /**
     * Get current active internship period
     *
     * @return InternshipPeriod
     * @throws InternshipPeriodNotFoundException if no active period is found
     */
    public InternshipPeriod getCurrentActivePeriod() {
        return periodRepository.findCurrentActivePeriod()
                .orElseThrow(() -> new InternshipPeriodNotFoundException("No active internship period found"));
    }

    /**
     * Get internship period by ID
     *
     * @param id Period ID
     * @return InternshipPeriod
     * @throws InternshipPeriodNotFoundException if period is not found
     */
    public InternshipPeriod getPeriodById(String id) {
        return periodRepository.findById(id)
                .orElseThrow(() -> new InternshipPeriodNotFoundException("Internship period not found with ID: " + id));
    }

    /**
     * Check if period exists
     *
     * @param id Period ID
     * @return true if period exists, false otherwise
     */
    public boolean periodExists(String id) {
        return periodRepository.existsById(id);
    }
}