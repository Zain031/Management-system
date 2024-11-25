package com.abpgroup.managementsystem.service;


import com.abpgroup.managementsystem.model.dto.response.SalesResponseDTO;

import java.time.LocalDate;

public interface SalesService {
    SalesResponseDTO getSalesByDate(LocalDate date);

    SalesResponseDTO getSalesByPeriod(String period);
}
