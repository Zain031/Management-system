package com.abpgroup.managementsystem.service;

import com.abpgroup.managementsystem.model.dto.response.BusinessPerformanceResponseDTO;

import java.time.LocalDate;

public interface BusinessPerfomanceService {

    BusinessPerformanceResponseDTO getBusinessPerformanceByPeriodAndYears(String period, Long years);
}
