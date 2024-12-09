package com.abpgroup.managementsystem.service.impl;

import com.abpgroup.managementsystem.model.dto.response.BusinessPerformanceResponseDTO;
import com.abpgroup.managementsystem.model.entity.Inventory;
import com.abpgroup.managementsystem.model.entity.Orders;
import com.abpgroup.managementsystem.repository.InventoryRepository;
import com.abpgroup.managementsystem.repository.OrdersRepository;
import com.abpgroup.managementsystem.service.BusinessPerformanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class BusinessPerformanceServiceImpl implements BusinessPerformanceService {
    private final InventoryRepository inventoryRepository;
    private final OrdersRepository ordersRepository;

    @Override
    public BusinessPerformanceResponseDTO getBusinessPerformanceByYears(Long years) {
        // Ambil data berdasarkan tahun
        List<Orders> orders = ordersRepository.getOrdersByYears(years);
        List<Inventory> inventories = inventoryRepository.getInventoryByYears(years);

        // Hitung profit per bulan berdasarkan order
        Map<Integer, Double> monthlyProfitMap = orders.stream()
                .filter(order -> order.getOrderDate().getYear() == years)  // Filter berdasarkan tahun
                .collect(Collectors.groupingBy(
                        order -> order.getOrderDate().getMonthValue(),
                        Collectors.summingDouble(order -> order.getTotalPrice())
                ));

        // Hitung total jumlah order per bulan
        Map<Integer, Long> monthlyOrderCountMap = orders.stream()
                .filter(order -> order.getOrderDate().getYear() == years)
                .collect(Collectors.groupingBy(
                        order -> order.getOrderDate().getMonthValue(),
                        Collectors.counting()
                ));

        // Hitung profit per bulan berdasarkan pembelian material
        Map<Integer, Double> monthlyInventoryCostMap = inventories.stream()
                .filter(inventory -> inventory.getDateMaterialBuy().getYear() == years)  // Filter berdasarkan tahun
                .collect(Collectors.groupingBy(
                        inventory -> inventory.getDateMaterialBuy().getMonthValue(),
                        Collectors.summingDouble(inventory -> inventory.getMaterialTotalPrice())
                ));

        // Buat daftar performa bulanan
        List<BusinessPerformanceResponseDTO.MonthlyPerformance> monthlyPerformances = new ArrayList<>();
        AtomicLong grandTotalOrder = new AtomicLong();
        AtomicLong grandTotalPriceOrder = new AtomicLong();
        AtomicLong grandTotalPriceInventory = new AtomicLong();

        IntStream.rangeClosed(1, 12).forEach(month -> {
            double orderProfit = monthlyProfitMap.getOrDefault(month, 0.0);
            long orderCount = monthlyOrderCountMap.getOrDefault(month, 0L);
            double inventoryCost = monthlyInventoryCostMap.getOrDefault(month, 0.0);
            double profit = orderProfit - inventoryCost;

            // Total harga pesanan
            double totalPriceOrder = monthlyProfitMap.getOrDefault(month, 0.0);
            // Total harga inventory, jika tidak ada pembelian, set 0
            double totalPriceInventory = monthlyInventoryCostMap.getOrDefault(month, 0.0);

            // Update Grand Totals
            grandTotalOrder.addAndGet(orderCount);
            grandTotalPriceOrder.addAndGet((long) totalPriceOrder);
            grandTotalPriceInventory.addAndGet((long) totalPriceInventory);

            BusinessPerformanceResponseDTO.MonthlyPerformance monthlyPerformance = new BusinessPerformanceResponseDTO.MonthlyPerformance();
            // Mengonversi angka bulan menjadi nama bulan
            String monthName = Month.of(month).name().toLowerCase();
            monthName = monthName.substring(0, 1).toUpperCase() + monthName.substring(1); // Mengubah pertama huruf menjadi kapital

            monthlyPerformance.setMonth(monthName);  // Set bulan dalam format string
            monthlyPerformance.setProfit(Math.round(profit));  // Menghitung profit berdasarkan orders dan inventaris
            monthlyPerformance.setTotalOrder(orderCount); // Set jumlah order per bulan
            monthlyPerformance.setTotalPriceOrder(Math.round(totalPriceOrder)); // Set total harga pesanan per bulan
            monthlyPerformance.setTotalPriceInventory(Math.round(totalPriceInventory)); // Set total harga inventory per bulan
            monthlyPerformances.add(monthlyPerformance);
        });

        // Hitung total profit tahunan
        double totalAnnualProfit = monthlyPerformances.stream()
                .mapToDouble(BusinessPerformanceResponseDTO.MonthlyPerformance::getProfit)
                .sum();

        // Return respons dengan Grand Totals
        return BusinessPerformanceResponseDTO.builder()
                .totalProfit(Math.round(totalAnnualProfit))
                .years(years)
                .monthlyPerformances(monthlyPerformances)
                .grandTotalOrder(grandTotalOrder.get()) // Grand total jumlah order
                .grandTotalPriceOrder(grandTotalPriceOrder.get()) // Grand total harga order
                .grandTotalPriceInventory(grandTotalPriceInventory.get()) // Grand total harga inventory
                .build();
    }
}
