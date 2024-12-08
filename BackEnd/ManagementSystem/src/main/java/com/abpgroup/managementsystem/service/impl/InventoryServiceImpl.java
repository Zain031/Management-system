package com.abpgroup.managementsystem.service.impl;

import com.abpgroup.managementsystem.model.dto.request.InventoryRequestDTO;
import com.abpgroup.managementsystem.model.dto.response.InventoryResponseDTO;
import com.abpgroup.managementsystem.model.dto.response.UsersResponseDTO;
import com.abpgroup.managementsystem.model.entity.Inventory;
import com.abpgroup.managementsystem.model.entity.Users;
import com.abpgroup.managementsystem.repository.InventoryRepository;
import com.abpgroup.managementsystem.repository.UsersRepository;
import com.abpgroup.managementsystem.service.InventoryService;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.AreaBreakType;
import com.itextpdf.layout.properties.TextAlignment;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {
    public final InventoryRepository inventoryRepository;
    public final UsersRepository usersRepository;

    @Override
    public Page<InventoryResponseDTO> getAllInventory(Pageable pageable) {
        Pageable sortedByDateMaterialBuy= PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.ASC, "dateMaterialBuy"));
        Page<Inventory> inventories = inventoryRepository.findAll(sortedByDateMaterialBuy);
        return inventories.map(this::toInventoryResponseDTO);
    }

    @Override
    public Page<InventoryResponseDTO> getAllInventoryByCategory(Pageable pageable, String category) {
        Pageable sortedByDateMaterialBuy= PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.ASC, "dateMaterialBuy"));
        Page<Inventory> inventories = inventoryRepository.findAllByCategory(Inventory.Category.valueOf(category.toUpperCase()), sortedByDateMaterialBuy);
        return inventories.map(this::toInventoryResponseDTO);
    }

    @Override
    public InventoryResponseDTO getInventoryById(Long id) {
        return toInventoryResponseDTO(findInventoryByIdOrThrow(id));
    }

    @Override
    public InventoryResponseDTO createInventory(InventoryRequestDTO inventoryRequestDTO) {
        if(inventoryRequestDTO.getMaterialName() == null || inventoryRequestDTO.getMaterialName().isEmpty()){
            throw new IllegalArgumentException("Material name cannot be empty");
        }

        if (inventoryRequestDTO.getMaterialPriceUnit() <= 0) {
            throw new IllegalArgumentException("Material price must be greater than zero");
        }

        if (inventoryRequestDTO.getMaterialCategory() == null || inventoryRequestDTO.getMaterialCategory().isEmpty()) {
            throw new IllegalArgumentException("Material category cannot be empty");
        }

        Users user = usersRepository.findById(inventoryRequestDTO.getIdUser())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Double materialPriceDiscount = inventoryRequestDTO.getMaterialPriceUnit() - (inventoryRequestDTO.getMaterialPriceUnit() * (inventoryRequestDTO.getMaterialDiscount()/100));
        Double materialTotalPrice = materialPriceDiscount * inventoryRequestDTO.getMaterialQuantity();
        Inventory inventory = Inventory.builder()
                .user(user)
                .category(Inventory.Category.valueOf(inventoryRequestDTO.getMaterialCategory().toUpperCase()))
                .materialName(inventoryRequestDTO.getMaterialName())
                .materialPriceUnit(inventoryRequestDTO.getMaterialPriceUnit())
                .materialQuantity(inventoryRequestDTO.getMaterialQuantity())
                .materialDiscount(inventoryRequestDTO.getMaterialDiscount())
                .materialPriceDiscount(materialPriceDiscount)
                .materialTotalPrice(materialTotalPrice)
                .dateMaterialBuy(inventoryRequestDTO.getDateMaterialBuy())
                .period(inventoryRequestDTO.getDateMaterialBuy().getMonth().name())
                .years(Long.valueOf(inventoryRequestDTO.getDateMaterialBuy().getYear()))
                .build();

        inventoryRepository.save(inventory);
        return toInventoryResponseDTO(inventory);
    }

    @Override
    public InventoryResponseDTO updateInventory(Long id, InventoryRequestDTO inventoryRequestDTO) {
        Inventory inventory = findInventoryByIdOrThrow(id);

        Users user = usersRepository.findById(inventoryRequestDTO.getIdUser())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Double materialPriceDiscount = inventoryRequestDTO.getMaterialPriceUnit() - (inventoryRequestDTO.getMaterialPriceUnit() * (inventoryRequestDTO.getMaterialDiscount()/100));
        Double materialTotalPrice = materialPriceDiscount * inventoryRequestDTO.getMaterialQuantity();
        inventory.setUser(user);
        inventory.setCategory(Inventory.Category.valueOf(inventoryRequestDTO.getMaterialCategory().toUpperCase()));
        inventory.setMaterialName(inventoryRequestDTO.getMaterialName());
        inventory.setMaterialPriceUnit(inventoryRequestDTO.getMaterialPriceUnit());
        inventory.setMaterialQuantity(inventoryRequestDTO.getMaterialQuantity());
        inventory.setMaterialDiscount(inventoryRequestDTO.getMaterialDiscount());
        inventory.setMaterialPriceDiscount(materialPriceDiscount);
        inventory.setMaterialTotalPrice(materialTotalPrice);
        inventory.setDateMaterialBuy(inventoryRequestDTO.getDateMaterialBuy());
        inventory.setPeriod(inventoryRequestDTO.getDateMaterialBuy().getMonth().name());
        inventory.setYears(Long.valueOf(inventoryRequestDTO.getDateMaterialBuy().getYear()));
        inventoryRepository.save(inventory);
        return toInventoryResponseDTO(inventory);
    }

    @Override
    public void deleteInventoryById(Long id) {
        Inventory inventory = findInventoryByIdOrThrow(id);
        inventoryRepository.delete(inventory);
    }

    @Override
    public Page<InventoryResponseDTO> getInventoryByMaterialName(String materialName, Pageable pageable) {
        Pageable sortedByDateMaterialBuy= PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.ASC, "dateMaterialBuy"));
        Page<Inventory> inventories = inventoryRepository.getInventoryByMaterialName(materialName, sortedByDateMaterialBuy);
        return inventories.map(this::toInventoryResponseDTO);
    }

    @Override
    public byte[] generatedPdfByPeriodAndYears(List<Inventory> inventory, String periodUpper, Long years) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(outputStream);
            com.itextpdf.kernel.pdf.PdfDocument pdf = new com.itextpdf.kernel.pdf.PdfDocument(writer);

            pdf.setDefaultPageSize(com.itextpdf.kernel.geom.PageSize.A4.rotate());

            Document document = new Document(pdf);

            // Add title
            Paragraph title = new Paragraph("Purchase Report By " + periodUpper + " " + years)
                    .setBold()
                    .setFontSize(16)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(10);
            document.add(title);

            document.add(new Paragraph(" "));

            // Add period and year (capitalize period)
            String formattedPeriod = periodUpper.substring(0, 1).toUpperCase() + periodUpper.substring(1).toLowerCase();
            Paragraph date = new Paragraph("Period: " + formattedPeriod + ", Years: " + years)
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setFontSize(12)
                    .setMarginBottom(10);
            document.add(date);

            // Define column widths
            float[] columnWidths = {0.8f, 3f, 2f, 2f, 2f, 2f, 2.5f, 2.5f, 2.5f};
            // Sort inventory by dateMaterialBuy in ascending order
            inventory.sort(Comparator.comparing(Inventory::getDateMaterialBuy));

            int itemsPerPage = 10;
            int totalItems = inventory.size();
            int totalPages = (int) Math.ceil((double) totalItems / itemsPerPage);

            int index = 1;
            // Calculate total for material total price
            long totalMaterialPrice = 0;

            for (int page = 0; page < totalPages; page++) {
                Table table = new Table(columnWidths);

                // Header
                table.addHeaderCell(new Cell().add(new Paragraph("No").setBold()).setTextAlignment(TextAlignment.CENTER));
                table.addHeaderCell(new Cell().add(new Paragraph("Material Name").setBold()).setTextAlignment(TextAlignment.CENTER));
                table.addHeaderCell(new Cell().add(new Paragraph("Material Price").setBold()).setTextAlignment(TextAlignment.CENTER));
                table.addHeaderCell(new Cell().add(new Paragraph("Material Quantity").setBold()).setTextAlignment(TextAlignment.CENTER));
                table.addHeaderCell(new Cell().add(new Paragraph("Material Discount").setBold()).setTextAlignment(TextAlignment.CENTER));
                table.addHeaderCell(new Cell().add(new Paragraph("Material Price Discount").setBold()).setTextAlignment(TextAlignment.CENTER));
                table.addHeaderCell(new Cell().add(new Paragraph("Category").setBold()).setTextAlignment(TextAlignment.CENTER));
                table.addHeaderCell(new Cell().add(new Paragraph("Date Material Buy").setBold()).setTextAlignment(TextAlignment.CENTER));
                table.addHeaderCell(new Cell().add(new Paragraph("Material Total Price").setBold()).setTextAlignment(TextAlignment.CENTER));

                // Rows
                int start = page * itemsPerPage;
                int end = Math.min(start + itemsPerPage, totalItems);
                for (int i = start; i < end; i++) {
                    Inventory inventory1 = inventory.get(i);
                    // Round values
                    long materialPriceUnit = Math.round(inventory1.getMaterialPriceUnit());
                    long materialQuantity = Math.round(inventory1.getMaterialQuantity());
                    long materialDiscount = Math.round(inventory1.getMaterialDiscount());
                    long materialPriceDiscount = Math.round(inventory1.getMaterialPriceDiscount());
                    long materialTotalPrice = Math.round(inventory1.getMaterialTotalPrice());

                    // Add to total material price
                    totalMaterialPrice += materialTotalPrice;

                    // Format category (capitalize first letter, lowercase the rest)
                    String categoryFormatted = String.valueOf(inventory1.getCategory());
                    if (categoryFormatted != null && !categoryFormatted.isEmpty()) {
                        categoryFormatted = categoryFormatted.substring(0, 1).toUpperCase() + categoryFormatted.substring(1).toLowerCase();
                    }

                    table.addCell(new Cell().add(new Paragraph(String.valueOf(index++))).setTextAlignment(TextAlignment.CENTER));
                    table.addCell(new Cell().add(new Paragraph(inventory1.getMaterialName())).setTextAlignment(TextAlignment.CENTER));
                    table.addCell(new Cell().add(new Paragraph(String.valueOf(materialPriceUnit))).setTextAlignment(TextAlignment.CENTER));
                    table.addCell(new Cell().add(new Paragraph(String.valueOf(materialQuantity))).setTextAlignment(TextAlignment.CENTER));
                    table.addCell(new Cell().add(new Paragraph(String.valueOf(materialDiscount))).setTextAlignment(TextAlignment.CENTER));
                    table.addCell(new Cell().add(new Paragraph(String.valueOf(materialPriceDiscount))).setTextAlignment(TextAlignment.CENTER));
                    table.addCell(new Cell().add(new Paragraph(categoryFormatted)).setTextAlignment(TextAlignment.CENTER));
                    table.addCell(new Cell().add(new Paragraph(inventory1.getDateMaterialBuy().toString())).setTextAlignment(TextAlignment.CENTER));
                    table.addCell(new Cell().add(new Paragraph(String.valueOf(materialTotalPrice))).setTextAlignment(TextAlignment.CENTER));
                }

                document.add(table);

                if (page < totalPages - 1) {
                    document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
                }
            }

            Table totalTable = new Table(columnWidths);
            document.add(totalTable);
            Paragraph total = new Paragraph("Grand Total Purchase: " + totalMaterialPrice)
                    .setBold()
                    .setFontSize(12)
                    .setTextAlignment(TextAlignment.RIGHT);
            document.add(total);

            document.close();

            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generating Product Sales PDF", e);
        }
    }

    @Override
    public byte[] generatedPdfByDatePurchases(List<Inventory> inventory, LocalDate datePurchases) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(outputStream);
            com.itextpdf.kernel.pdf.PdfDocument pdf = new com.itextpdf.kernel.pdf.PdfDocument(writer);
            Document document = new Document(pdf);

            // Add title
            Paragraph title = new Paragraph("Purchase Report By Date")
                    .setBold()
                    .setFontSize(16)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(10);
            document.add(title);

            document.add(new Paragraph(" "));

            // Add date
            Paragraph date = new Paragraph("Date Purchases: " + datePurchases)
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setFontSize(12)
                    .setMarginBottom(10);
            document.add(date);
            // Sort inventory by dateMaterialBuy in ascending order
            inventory.sort(Comparator.comparing(Inventory::getDateMaterialBuy));

            // Define column widths
            float[] columnWidths = {0.8f, 3f, 2f, 2f, 2f, 2f, 2.5f, 2.5f, 2.5f};

            int itemsPerPage = 10;
            int totalItems = inventory.size();
            int totalPages = (int) Math.ceil((double) totalItems / itemsPerPage);

            int index = 1;
            // Calculate total for material total price
            long totalMaterialPrice = 0;

            for (int page = 0; page < totalPages; page++) {
                Table table = new Table(columnWidths);

                // Header
                table.addHeaderCell(new Cell().add(new Paragraph("No").setBold()).setTextAlignment(TextAlignment.CENTER));
                table.addHeaderCell(new Cell().add(new Paragraph("Material Name").setBold()).setTextAlignment(TextAlignment.CENTER));
                table.addHeaderCell(new Cell().add(new Paragraph("Material Price").setBold()).setTextAlignment(TextAlignment.CENTER));
                table.addHeaderCell(new Cell().add(new Paragraph("Material Quantity").setBold()).setTextAlignment(TextAlignment.CENTER));
                table.addHeaderCell(new Cell().add(new Paragraph("Material Discount").setBold()).setTextAlignment(TextAlignment.CENTER));
                table.addHeaderCell(new Cell().add(new Paragraph("Material Price Discount").setBold()).setTextAlignment(TextAlignment.CENTER));
                table.addHeaderCell(new Cell().add(new Paragraph("Category").setBold()).setTextAlignment(TextAlignment.CENTER));
                table.addHeaderCell(new Cell().add(new Paragraph("Date Material Buy").setBold()).setTextAlignment(TextAlignment.CENTER));
                table.addHeaderCell(new Cell().add(new Paragraph("Material Total Price").setBold()).setTextAlignment(TextAlignment.CENTER));

                // Rows
                int start = page * itemsPerPage;
                int end = Math.min(start + itemsPerPage, totalItems);
                for (int i = start; i < end; i++) {
                    Inventory inventory1 = inventory.get(i);
                    // Round values
                    long materialPriceUnit = Math.round(inventory1.getMaterialPriceUnit());
                    long materialQuantity = Math.round(inventory1.getMaterialQuantity());
                    long materialDiscount = Math.round(inventory1.getMaterialDiscount());
                    long materialPriceDiscount = Math.round(inventory1.getMaterialPriceDiscount());
                    long materialTotalPrice = Math.round(inventory1.getMaterialTotalPrice());

                    // Add to total material price
                    totalMaterialPrice += materialTotalPrice;

                    // Format category (capitalize first letter, lowercase the rest)
                    String categoryFormatted = String.valueOf(inventory1.getCategory());
                    if (categoryFormatted != null && !categoryFormatted.isEmpty()) {
                        categoryFormatted = categoryFormatted.substring(0, 1).toUpperCase() + categoryFormatted.substring(1).toLowerCase();
                    }

                    table.addCell(new Cell().add(new Paragraph(String.valueOf(index++))).setTextAlignment(TextAlignment.CENTER));
                    table.addCell(new Cell().add(new Paragraph(inventory1.getMaterialName())).setTextAlignment(TextAlignment.CENTER));
                    table.addCell(new Cell().add(new Paragraph(String.valueOf(materialPriceUnit))).setTextAlignment(TextAlignment.CENTER));
                    table.addCell(new Cell().add(new Paragraph(String.valueOf(materialQuantity))).setTextAlignment(TextAlignment.CENTER));
                    table.addCell(new Cell().add(new Paragraph(String.valueOf(materialDiscount))).setTextAlignment(TextAlignment.CENTER));
                    table.addCell(new Cell().add(new Paragraph(String.valueOf(materialPriceDiscount))).setTextAlignment(TextAlignment.CENTER));
                    table.addCell(new Cell().add(new Paragraph(categoryFormatted)).setTextAlignment(TextAlignment.CENTER));
                    table.addCell(new Cell().add(new Paragraph(inventory1.getDateMaterialBuy().toString())).setTextAlignment(TextAlignment.CENTER));
                    table.addCell(new Cell().add(new Paragraph(String.valueOf(materialTotalPrice))).setTextAlignment(TextAlignment.CENTER));
                }

                document.add(table);

                if (page < totalPages - 1) {
                    document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
                }
            }

            Table totalTable = new Table(columnWidths);
            document.add(totalTable);
            Paragraph total = new Paragraph("Grand Total Purchase: " + totalMaterialPrice)
                    .setBold()
                    .setFontSize(12)
                    .setTextAlignment(TextAlignment.RIGHT);
            document.add(total);

            document.close();

            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generating Product Sales PDF", e);
        }
    }

    private Inventory findInventoryByIdOrThrow(Long id) {
        return inventoryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Inventory not found"));
    }

    private InventoryResponseDTO toInventoryResponseDTO(Inventory inventory) {
        return InventoryResponseDTO.builder()
                .idMaterial(inventory.getIdMaterial())
                .usersResponseDTO(convertToResponse(inventory.getUser()))
                .materialCategory(inventory.getCategory().toString())
                .materialName(inventory.getMaterialName())
                .materialPriceUnit(inventory.getMaterialPriceUnit())
                .materialQuantity(inventory.getMaterialQuantity())
                .materialDiscount(inventory.getMaterialDiscount())
                .materialPriceDiscount(inventory.getMaterialPriceDiscount())
                .materialTotalPrice(inventory.getMaterialTotalPrice())
                .dateMaterialBuy(inventory.getDateMaterialBuy())
                .period(inventory.getPeriod())
                .years(inventory.getYears())
                .build();
    }

    private UsersResponseDTO convertToResponse(Users user) {
        return UsersResponseDTO.builder()
                .idUser(user.getIdUser())
                .email(user.getEmail())
                .name(user.getName())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
