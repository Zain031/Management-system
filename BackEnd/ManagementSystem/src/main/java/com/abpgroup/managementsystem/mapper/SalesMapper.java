package com.abpgroup.managementsystem.mapper;

import com.abpgroup.managementsystem.model.dto.response.SalesResponseDTO;
import com.abpgroup.managementsystem.model.entity.Sales;

import java.util.ArrayList;
import java.util.List;

public class SalesMapper {
    public static List<SalesResponseDTO> toListSalesResponseDTO(List<Sales> salesList) {
        List<SalesResponseDTO> result = new ArrayList<>();

        for (Sales data : salesList) {
            result.add(SalesResponseDTO.builder()
                    .idSales(data.getIdSales())
                    .totalSalesPrice(data.getTotalSalesPrice())
                    .totalLeftoverSalesPrice(data.getTotalLeftoverSalesPrice())
                    .dateSales(data.getDateSales())
                    .period(data.getPeriod())
                    .build());
        }

        return result;

    }
}
