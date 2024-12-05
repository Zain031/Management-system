package com.abpgroup.managementsystem.service;

import com.abpgroup.managementsystem.model.dto.response.BusinessPerformanceResponseDTO;

public interface BusinessPerformanceService {
    BusinessPerformanceResponseDTO getBusinessPerformanceByPeriodAndYears(String period, Long years);
}
