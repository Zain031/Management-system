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
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
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

        if (productSalesRequestDTO.getTotalProductToSell() <= 0) {
            throw new IllegalArgumentException("Total product sales must be greater than zero");
        }

        if (productSalesRequestDTO.getLeftoverProductSales() <= 0) {
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
        Pageable sortedByDateProductSales = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.ASC, "dateProductSales"));
        Page<ProductSales> productSales = productSalesRepository.findProductSalesByProductCategories(productCategories.toUpperCase(), sortedByDateProductSales);
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
    public byte[] generatedPdf(List<ProductSales> productSalesList) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            // Initialize PDF writer and document
            PdfWriter writer = new PdfWriter(outputStream);
            com.itextpdf.kernel.pdf.PdfDocument pdf = new com.itextpdf.kernel.pdf.PdfDocument(writer);

            // Set the page size to landscape orientation (A4)
            pdf.setDefaultPageSize(com.itextpdf.kernel.geom.PageSize.A4.rotate());

            // Initialize document
            Document document = new Document(pdf);

            // Add Title
            Paragraph title = new Paragraph("Product Sales Report")
                    .setBold()
                    .setFontSize(16)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(10);
            document.add(title);

            // Add empty line for spacing
            document.add(new Paragraph(" "));

            // Define table column widths for better readability
            float[] columnWidths = {0.8f, 2.5f, 2.5f, 2.5f, 2.5f, 2.5f, 2.5f, 2.5f, 2.5f, 2.5f, 2.5f}; // Adjust column widths
            Table table = new Table(columnWidths);

            // Add Table Header with styling
            table.addHeaderCell(new Cell().add(new Paragraph("No").setBold()).setTextAlignment(TextAlignment.CENTER));
            table.addHeaderCell(new Cell().add(new Paragraph("Product Name").setBold()).setTextAlignment(TextAlignment.CENTER));
            table.addHeaderCell(new Cell().add(new Paragraph("Product Price").setBold()).setTextAlignment(TextAlignment.CENTER));
            table.addHeaderCell(new Cell().add(new Paragraph("Total Products To Sell").setBold()).setTextAlignment(TextAlignment.CENTER));
            table.addHeaderCell(new Cell().add(new Paragraph("Total Products Remaining").setBold()).setTextAlignment(TextAlignment.CENTER));
            table.addHeaderCell(new Cell().add(new Paragraph("Total Products Sold").setBold()).setTextAlignment(TextAlignment.CENTER));
            table.addHeaderCell(new Cell().add(new Paragraph("Total Price Remaining").setBold()).setTextAlignment(TextAlignment.CENTER));
            table.addHeaderCell(new Cell().add(new Paragraph("Total Price Sold").setBold()).setTextAlignment(TextAlignment.CENTER));
            table.addHeaderCell(new Cell().add(new Paragraph("Date").setBold()).setTextAlignment(TextAlignment.CENTER));
            table.addHeaderCell(new Cell().add(new Paragraph("Period").setBold()).setTextAlignment(TextAlignment.CENTER));
            table.addHeaderCell(new Cell().add(new Paragraph("Year").setBold()).setTextAlignment(TextAlignment.CENTER));

            // Add Table Data with centered text
            int index = 1;
            for (ProductSales productSales : productSalesList) {
                table.addCell(new Cell().add(new Paragraph(String.valueOf(index++))).setTextAlignment(TextAlignment.CENTER));
                table.addCell(new Cell().add(new Paragraph(productSales.getProduct().getProductName())).setTextAlignment(TextAlignment.CENTER));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(productSales.getProduct().getProductPrice()))).setTextAlignment(TextAlignment.CENTER));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(productSales.getTotalProductSales()))).setTextAlignment(TextAlignment.CENTER));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(productSales.getLeftoverProductSales()))).setTextAlignment(TextAlignment.CENTER));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(productSales.getTotalProductSales() - productSales.getLeftoverProductSales()))).setTextAlignment(TextAlignment.CENTER));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(productSales.getTotalLeftoverProductSalesPrice()))).setTextAlignment(TextAlignment.CENTER));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(productSales.getTotalProductSalesPrice()))).setTextAlignment(TextAlignment.CENTER));
                table.addCell(new Cell().add(new Paragraph(productSales.getDateProductSales().toString())).setTextAlignment(TextAlignment.CENTER));
                table.addCell(new Cell().add(new Paragraph(productSales.getPeriod())).setTextAlignment(TextAlignment.CENTER));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(productSales.getYears()))).setTextAlignment(TextAlignment.CENTER));
            }

            // Add table to the document
            document.add(table);

            // Close the document
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
