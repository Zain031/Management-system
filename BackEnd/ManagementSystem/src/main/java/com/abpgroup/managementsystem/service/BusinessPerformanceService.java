package com.abpgroup.managementsystem.service;

import com.abpgroup.managementsystem.model.dto.response.BusinessPerformanceResponseDTO;

public interface BusinessPerformanceService {
    BusinessPerformanceResponseDTO getBusinessPerformanceByYears(Long years);
}
