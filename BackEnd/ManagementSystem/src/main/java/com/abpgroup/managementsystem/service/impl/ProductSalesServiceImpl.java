package com.abpgroup.managementsystem.service.impl;

import com.abpgroup.managementsystem.model.dto.request.ProductSalesRequestDTO;
import com.abpgroup.managementsystem.model.dto.response.ProductResponseDTO;
import com.abpgroup.managementsystem.model.dto.response.ProductSalesResponseDTO;
import com.abpgroup.managementsystem.model.dto.response.UsersResponseDTO;
import com.abpgroup.managementsystem.model.entity.ProductSales;
import com.abpgroup.managementsystem.model.entity.Products;
import com.abpgroup.managementsystem.model.entity.Users;
import com.abpgroup.managementsystem.repository.ProductSalesRepository;
import com.abpgroup.managementsystem.repository.ProductsRepository;
import com.abpgroup.managementsystem.repository.UsersRepository;
import com.abpgroup.managementsystem.service.ProductSalesService;
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
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductSalesServiceImpl implements ProductSalesService {
    private final ProductSalesRepository productSalesRepository;
    private final ProductsRepository productsRepository;
    private final UsersRepository usersRepository;

    @Override
    public ProductSalesResponseDTO createProductSales(ProductSalesRequestDTO productSalesRequestDTO) {
        Users user = usersRepository.findById(productSalesRequestDTO.getIdUser())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Products product = productsRepository.findById(productSalesRequestDTO.getIdProduct())
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        LocalDate dateProductSales = productSalesRequestDTO.getDateProductSales();

        if (productSalesRequestDTO.getTotalProductToSell() < 0) {
            throw new IllegalArgumentException("Total product sales must be greater than zero");
        }

        if (productSalesRequestDTO.getLeftoverProductSales() < 0) {
            throw new IllegalArgumentException("Leftover product sales must be greater than zero");
        }

        if (productSalesRequestDTO.getDateProductSales() == null) {
            throw new IllegalArgumentException("Date product sales cannot be empty");
        }

        ProductSales productSales = ProductSales.builder()
                .user(user)
                .product(product)
                .totalProductToSell(productSalesRequestDTO.getTotalProductToSell())
                .leftoverProductSales(productSalesRequestDTO.getLeftoverProductSales())
                .totalLeftoverProductSalesPrice((productSalesRequestDTO.getLeftoverProductSales() * product.getProductPrice()))
                .totalProductSales(productSalesRequestDTO.getTotalProductToSell() - productSalesRequestDTO.getLeftoverProductSales())
                .totalProductSalesPrice((productSalesRequestDTO.getTotalProductToSell() - productSalesRequestDTO.getLeftoverProductSales()) * product.getProductPrice())
                .dateProductSales(dateProductSales)
                .period(dateProductSales.getMonth().name())
                .years((long) dateProductSales.getYear())
                .build();

        productSalesRepository.save(productSales);

        return convertToResponse(productSales);
    }

    @Override
    public Page<ProductSalesResponseDTO> getAllProductSales(Pageable pageable) {
        Pageable sortedByDateProductSales = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.ASC, "dateProductSales"));
        return productSalesRepository.findAll(sortedByDateProductSales)
                .map(this::convertToResponse);
    }

    @Override
    public Page<ProductSalesResponseDTO> getProductSalesByDate(LocalDate dateProductSales, Pageable pageable) {
       Page<ProductSales> productSales = productSalesRepository.findProductSalesByDateProductSales(dateProductSales, pageable);
       return productSales.map(this::convertToResponse);
    }

    @Override
    public Page<ProductSalesResponseDTO> getProductSalesByPeriodAndYears(String period,Long years, Pageable pageable) {
        Pageable sortedByDateProductSales = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.ASC, "dateProductSales"));
        Page<ProductSales> productSales = productSalesRepository.findProductSalesByPeriodAndYears(period.toUpperCase(),years, sortedByDateProductSales);
        return productSales.map(this::convertToResponse);
    }

    @Override
    public Page<ProductSalesResponseDTO> getProductSalesByProductCategories(String productCategories, Pageable pageable) {
        Products.ProductCategory category;
        try {
            category = Products.ProductCategory.valueOf(productCategories.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid product category: " + productCategories);
        }

        Pageable sortedByDateProductSales = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.ASC, "dateProductSales"));
        Page<ProductSales> productSales = productSalesRepository.findProductSalesByProductCategories(category, sortedByDateProductSales);
        return productSales.map(this::convertToResponse);
    }


    @Override
    public ProductSalesResponseDTO updateProductSales(Long id, ProductSalesRequestDTO productSalesRequestDTO) {
        ProductSales productSales = productSalesRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product sales not found"));
        Users user = usersRepository.findById(productSalesRequestDTO.getIdUser())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Products product = productsRepository.findById(productSalesRequestDTO.getIdProduct())
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        if (productSalesRequestDTO.getTotalProductToSell() <= 0) {
            throw new IllegalArgumentException("Total product sales must be greater than zero");
        }

        if (productSalesRequestDTO.getLeftoverProductSales() <= 0) {
            throw new IllegalArgumentException("Leftover product sales must be greater than zero");
        }

        if (productSalesRequestDTO.getDateProductSales() == null) {
            throw new IllegalArgumentException("Date product sales cannot be empty");
        }

        ProductSales updatedProductSales = ProductSales.builder()
                .user(user)
                .product(product)
                .totalProductToSell(productSalesRequestDTO.getTotalProductToSell())
                .leftoverProductSales(productSalesRequestDTO.getLeftoverProductSales())
                .totalLeftoverProductSalesPrice((productSalesRequestDTO.getLeftoverProductSales() * product.getProductPrice()))
                .totalProductSales(productSalesRequestDTO.getTotalProductToSell() - productSalesRequestDTO.getLeftoverProductSales())
                .totalProductSalesPrice((productSalesRequestDTO.getTotalProductToSell() - productSalesRequestDTO.getLeftoverProductSales()) * product.getProductPrice())
                .dateProductSales(productSalesRequestDTO.getDateProductSales())
                .period(productSalesRequestDTO.getDateProductSales().getMonth().name())
                .years((long) productSalesRequestDTO.getDateProductSales().getYear())
                .build();

        updatedProductSales.setIdProductSales(productSales.getIdProductSales());
        productSalesRepository.save(updatedProductSales);
        return convertToResponse(updatedProductSales);
    }

    @Override
    public ProductSalesResponseDTO deleteProductSales(Long id) {
        ProductSales productSales = productSalesRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product sales not found"));
        productSalesRepository.delete(productSales);
        return convertToResponse(productSales);
    }

    @Override
    public Page<ProductSalesResponseDTO> getProductSalesByProductName(String productName, Pageable pageable) {
        Pageable sortedByDateProductSales = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.ASC, "dateProductSales"));
        Page<ProductSales> productSales = productSalesRepository.findProductSalesByProductName(productName, sortedByDateProductSales);
        return productSales.map(this::convertToResponse);
    }

    @Override
    public byte[] generatedPdf(List<ProductSales> productSalesList, String period, Long year) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(outputStream);
            com.itextpdf.kernel.pdf.PdfDocument pdf = new com.itextpdf.kernel.pdf.PdfDocument(writer);

            pdf.setDefaultPageSize(com.itextpdf.kernel.geom.PageSize.A4.rotate());

            Document document = new Document(pdf);

            // Add title
            Paragraph title = new Paragraph("Product Sales Report By " + productSalesList.get(0).getPeriod().toCharArray()[0] + productSalesList.get(0).getPeriod().substring(1).toLowerCase()+ " " + year)
                    .setBold()
                    .setFontSize(16)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(10);
            document.add(title);

            document.add(new Paragraph(" "));

            // Add period and year
            Paragraph periodAndYear = new Paragraph("Period: " + productSalesList.get(0).getPeriod().toCharArray()[0] + productSalesList.get(0).getPeriod().substring(1).toLowerCase() + "   Year: " + year)
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setFontSize(12)
                    .setMarginBottom(10);
            document.add(periodAndYear);

            // Define column widths
            float[] columnWidths = {0.8f, 3f, 2f, 2f, 2f, 2f, 2.5f, 2.5f, 2.5f};

            int itemsPerPage = 10;
            int totalItems = productSalesList.size();
            int totalPages = (int) Math.ceil((double) totalItems / itemsPerPage);

            int index = 1;
            long totalProductSalesPrice = 0;

            for (int page = 0; page < totalPages; page++) {
                Table table = new Table(columnWidths);

                // Header
                table.addHeaderCell(new Cell().add(new Paragraph("No").setBold()).setTextAlignment(TextAlignment.CENTER));
                table.addHeaderCell(new Cell().add(new Paragraph("Product Name").setBold()).setTextAlignment(TextAlignment.CENTER));
                table.addHeaderCell(new Cell().add(new Paragraph("Product Price").setBold()).setTextAlignment(TextAlignment.CENTER));
                table.addHeaderCell(new Cell().add(new Paragraph("Total Products To Sell").setBold()).setTextAlignment(TextAlignment.CENTER));
                table.addHeaderCell(new Cell().add(new Paragraph("Total Products Remaining").setBold()).setTextAlignment(TextAlignment.CENTER));
                table.addHeaderCell(new Cell().add(new Paragraph("Total Products Sold").setBold()).setTextAlignment(TextAlignment.CENTER));
                table.addHeaderCell(new Cell().add(new Paragraph("Total Price Remaining").setBold()).setTextAlignment(TextAlignment.CENTER));
                table.addHeaderCell(new Cell().add(new Paragraph("Date").setBold()).setTextAlignment(TextAlignment.CENTER));
                table.addHeaderCell(new Cell().add(new Paragraph("Total Price Sold").setBold()).setTextAlignment(TextAlignment.CENTER));

                // Rows
                int start = page * itemsPerPage;
                int end = Math.min(start + itemsPerPage, totalItems);
                for (int i = start; i < end; i++) {
                    ProductSales productSales = productSalesList.get(i);
                    table.addCell(new Cell().add(new Paragraph(String.valueOf(index++))).setTextAlignment(TextAlignment.CENTER));
                    table.addCell(new Cell().add(new Paragraph(productSales.getProduct().getProductName())).setTextAlignment(TextAlignment.CENTER));
                    table.addCell(new Cell().add(new Paragraph(String.valueOf(productSales.getProduct().getProductPrice()))).setTextAlignment(TextAlignment.CENTER));
                    table.addCell(new Cell().add(new Paragraph(String.valueOf(productSales.getTotalProductToSell()))).setTextAlignment(TextAlignment.CENTER));
                    table.addCell(new Cell().add(new Paragraph(String.valueOf(productSales.getLeftoverProductSales()))).setTextAlignment(TextAlignment.CENTER));
                    table.addCell(new Cell().add(new Paragraph(String.valueOf(productSales.getTotalProductToSell() - productSales.getLeftoverProductSales()))).setTextAlignment(TextAlignment.CENTER));
                    table.addCell(new Cell().add(new Paragraph(String.valueOf(productSales.getTotalLeftoverProductSalesPrice()))).setTextAlignment(TextAlignment.CENTER));
                    table.addCell(new Cell().add(new Paragraph(productSales.getDateProductSales().toString())).setTextAlignment(TextAlignment.CENTER));
                    table.addCell(new Cell().add(new Paragraph(String.valueOf(productSales.getTotalProductSalesPrice()))).setTextAlignment(TextAlignment.CENTER));

                    // Add TotalProductSalesPrice to the running total
                    totalProductSalesPrice += productSales.getTotalProductSalesPrice();
                }

                document.add(table);

                // Add a page break only if this is not the last page
                if (page < totalPages - 1) {
                    document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
                }
            }

            // Add the final row for "Grand Total" on the last page
            Table totalTable = new Table(columnWidths);
            document.add(totalTable);

            Paragraph grandTotal = new Paragraph("Grand Total: " + totalProductSalesPrice)
                    .setBold()
                    .setFontSize(12)
                    .setTextAlignment(TextAlignment.RIGHT);
            document.add(grandTotal);

            document.close();

            return outputStream.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error generating Product Sales PDF", e);
        }
    }

    @Override
    public byte[] generatedPdfByDate(List<ProductSales> productSalesList, LocalDate dateProductSales) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(outputStream);
            com.itextpdf.kernel.pdf.PdfDocument pdf = new com.itextpdf.kernel.pdf.PdfDocument(writer);

            pdf.setDefaultPageSize(com.itextpdf.kernel.geom.PageSize.A4.rotate());

            Document document = new Document(pdf);

            // Add title
            Paragraph title = new Paragraph("Product Sales Report By Date")
                    .setBold()
                    .setFontSize(16)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(10);
            document.add(title);

            document.add(new Paragraph(" "));

            // Add date
            Paragraph date = new Paragraph("Date: " + dateProductSales)
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setFontSize(12)
                    .setMarginBottom(10);
            document.add(date);

            // Define column widths
            float[] columnWidths = {0.8f, 3f, 2f, 2f, 2f, 2f, 2.5f, 2.5f, 2.5f};

            int itemsPerPage = 10;
            int totalItems = productSalesList.size();
            int totalPages = (int) Math.ceil((double) totalItems / itemsPerPage);

            int index = 1;
            long totalProductSalesPrice = 0;

            for (int page = 0; page < totalPages; page++) {
                Table table = new Table(columnWidths);

                // Header
                table.addHeaderCell(new Cell().add(new Paragraph("No").setBold()).setTextAlignment(TextAlignment.CENTER));
                table.addHeaderCell(new Cell().add(new Paragraph("Product Name").setBold()).setTextAlignment(TextAlignment.CENTER));
                table.addHeaderCell(new Cell().add(new Paragraph("Product Price").setBold()).setTextAlignment(TextAlignment.CENTER));
                table.addHeaderCell(new Cell().add(new Paragraph("Total Products To Sell").setBold()).setTextAlignment(TextAlignment.CENTER));
                table.addHeaderCell(new Cell().add(new Paragraph("Total Products Remaining").setBold()).setTextAlignment(TextAlignment.CENTER));
                table.addHeaderCell(new Cell().add(new Paragraph("Total Products Sold").setBold()).setTextAlignment(TextAlignment.CENTER));
                table.addHeaderCell(new Cell().add(new Paragraph("Total Price Remaining").setBold()).setTextAlignment(TextAlignment.CENTER));
                table.addHeaderCell(new Cell().add(new Paragraph("Date").setBold()).setTextAlignment(TextAlignment.CENTER));
                table.addHeaderCell(new Cell().add(new Paragraph("Total Price Sold").setBold()).setTextAlignment(TextAlignment.CENTER));

                // Rows
                int start = page * itemsPerPage;
                int end = Math.min(start + itemsPerPage, totalItems);
                for (int i = start; i < end; i++) {
                    ProductSales productSales = productSalesList.get(i);
                    table.addCell(new Cell().add(new Paragraph(String.valueOf(index++))).setTextAlignment(TextAlignment.CENTER));
                    table.addCell(new Cell().add(new Paragraph(productSales.getProduct().getProductName())).setTextAlignment(TextAlignment.CENTER));
                    table.addCell(new Cell().add(new Paragraph(String.valueOf(productSales.getProduct().getProductPrice()))).setTextAlignment(TextAlignment.CENTER));
                    table.addCell(new Cell().add(new Paragraph(String.valueOf(productSales.getTotalProductToSell()))).setTextAlignment(TextAlignment.CENTER));
                    table.addCell(new Cell().add(new Paragraph(String.valueOf(productSales.getLeftoverProductSales()))).setTextAlignment(TextAlignment.CENTER));
                    table.addCell(new Cell().add(new Paragraph(String.valueOf(productSales.getTotalProductToSell() - productSales.getLeftoverProductSales()))).setTextAlignment(TextAlignment.CENTER));
                    table.addCell(new Cell().add(new Paragraph(String.valueOf(productSales.getTotalLeftoverProductSalesPrice()))).setTextAlignment(TextAlignment.CENTER));
                    table.addCell(new Cell().add(new Paragraph(productSales.getDateProductSales().toString())).setTextAlignment(TextAlignment.CENTER));
                    table.addCell(new Cell().add(new Paragraph(String.valueOf(productSales.getTotalProductSalesPrice()))).setTextAlignment(TextAlignment.CENTER));

                    // Add TotalProductSalesPrice to the running total
                    totalProductSalesPrice += productSales.getTotalProductSalesPrice();
                }

                document.add(table);

                // Add a page break only if this is not the last page
                if (page < totalPages - 1) {
                    document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
                }
            }

            // Add the final row for "Grand Total" on the last page
            Table totalTable = new Table(columnWidths);
            document.add(totalTable);

            Paragraph grandTotal = new Paragraph("Grand Total: " + totalProductSalesPrice)
                    .setBold()
                    .setFontSize(12)
                    .setTextAlignment(TextAlignment.RIGHT);
            document.add(grandTotal);

            document.close();

            return outputStream.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error generating Product Sales PDF", e);
        }
    }

    private ProductSalesResponseDTO convertToResponse(ProductSales productSales) {
        return ProductSalesResponseDTO.builder()
                .idProductSales(productSales.getIdProductSales())
                .usersResponseDTO(convertToUsersResponseDTO(productSales.getUser()))
                .productResponseDTO(convertToResponse(productSales.getProduct()))
                .totalProductToSell(productSales.getTotalProductToSell())
                .totalProductSales(productSales.getTotalProductSales())
                .leftoverProductSales(productSales.getLeftoverProductSales())
                .totalProductSalesPrice(productSales.getTotalProductSalesPrice())
                .totalLeftoverProductSalesPrice(productSales.getTotalLeftoverProductSalesPrice())
                .dateProductSales(productSales.getDateProductSales())
                .period(productSales.getPeriod())
                .years(productSales.getYears())
                .build();
    }
    private ProductResponseDTO convertToResponse(Products product) {
        return ProductResponseDTO.builder()
                .idProduct(product.getIdProduct())
                .productName(product.getProductName())
                .productPrice(product.getProductPrice())
                .categories(product.getCategories().name())
                .usersResponseDTO(UsersResponseDTO.builder()
                        .idUser(product.getUser().getIdUser())
                        .email(product.getUser().getEmail())
                        .name(product.getUser().getName())
                        .role(product.getUser().getRole().name())
                        .build())
                .build();
    }

    private UsersResponseDTO convertToUsersResponseDTO(Users user) {
        return UsersResponseDTO.builder()
                .idUser(user.getIdUser())
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole().name())
                .build();
    }

}
