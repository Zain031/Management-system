package com.abpgroup.managementsystem.service.impl;

import com.abpgroup.managementsystem.model.dto.request.ProductRequestDTO;
import com.abpgroup.managementsystem.model.dto.response.ProductResponseDTO;
import com.abpgroup.managementsystem.model.dto.response.UsersResponseDTO;
import com.abpgroup.managementsystem.model.entity.Products;
import com.abpgroup.managementsystem.model.entity.Users;
import com.abpgroup.managementsystem.repository.ProductsRepository;
import com.abpgroup.managementsystem.repository.UsersRepository;
import com.abpgroup.managementsystem.service.ProductService;
import com.abpgroup.managementsystem.utils.CapitalizeFirstLetter;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.AreaBreakType;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductsRepository productRepository;
    private final UsersRepository usersRepository;

    @Override
    @Transactional
    public ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO) {
        if (productRequestDTO.getProductName() == null || productRequestDTO.getProductName().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be empty");
        }

        if (productRequestDTO.getProductPrice() <= 0) {
            throw new IllegalArgumentException("Product price must be greater than zero");
        }

        if (productRequestDTO.getCategories() == null || productRequestDTO.getCategories().isEmpty()) {
            throw new IllegalArgumentException("Product category cannot be empty");
        }

        Users user = usersRepository.findById(productRequestDTO.getIdUser())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        String productNameFormatted = CapitalizeFirstLetter.capitalizeFirstLetter(productRequestDTO.getProductName());

        Products product = Products.builder()
                .user(user)
                .productName(productNameFormatted)
                .productPrice(productRequestDTO.getProductPrice())
                .categories(Products.ProductCategory.valueOf(productRequestDTO.getCategories().toUpperCase()))
                .availableStock(productRequestDTO.getAvailableStock())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        productRepository.save(product);

        return convertToResponse(product);
    }

    @Override
    public ProductResponseDTO getProductById(Long id) {
        return productRepository.findById(id).map(this::convertToResponse).orElse(null);
    }

    @Override
    public ProductResponseDTO updateProduct(Long id, ProductRequestDTO productRequestDTO) {
        Products product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        Users user = usersRepository.findById(productRequestDTO.getIdUser())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        product.setUser(user);
        product.setProductName(CapitalizeFirstLetter.capitalizeFirstLetter(productRequestDTO.getProductName()));
        product.setProductPrice(productRequestDTO.getProductPrice());
        product.setCategories(Products.ProductCategory.valueOf(productRequestDTO.getCategories().toUpperCase()));
        product.setAvailableStock(productRequestDTO.getAvailableStock());
        product.setUpdatedAt(LocalDateTime.now());
        productRepository.save(product);
        return convertToResponse(product);
    }

    @Override
    public ProductResponseDTO deleteProduct(Long id) {
        Products product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        productRepository.delete(product);
        return convertToResponse(product);
    }

    @Override
    public Page<ProductResponseDTO> getAllProductsByAvailableStock(Pageable pageable, String availableStock) {
        Pageable sortedByPrice = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.ASC, "productPrice"));
        Page<Products> products = productRepository.findAllByAvailableStock(sortedByPrice, Products.AvailableStock.valueOf(availableStock));
        return products.map(this::convertToResponse);
    }

    @Override
    public Page<ProductResponseDTO> getAllProducts(Pageable pageable) {
        Pageable sortedByPrice = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.ASC, "productPrice"));
        Page<Products> products = productRepository.findAll(sortedByPrice);
        return products.map(this::convertToResponse);
    }

    @Override
    public Page<ProductResponseDTO> getProductByCategory(String category, Pageable pageable) {
        Pageable sortedByPrice = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.ASC, "productPrice"));
        Page<Products> products = productRepository.findProductsByCategories(Products.ProductCategory.valueOf(category.toUpperCase()), sortedByPrice);
        return products.map(this::convertToResponse);
    }

    @Override
    public Page<ProductResponseDTO> getProductByProductName(String productName, Pageable pageable) {
        Pageable sortedByPrice = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.ASC, "productPrice"));
        Page<Products> products = productRepository.getProductsByProductName(productName, sortedByPrice);
        return products.map(this::convertToResponse);
    }

    @Override
    public byte[] generatedPdf(List<Products> products) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(outputStream);
            // Set page size to A4 landscape
            PageSize pageSize = PageSize.A4.rotate();
            com.itextpdf.kernel.pdf.PdfDocument pdf = new com.itextpdf.kernel.pdf.PdfDocument(writer);
            pdf.setDefaultPageSize(pageSize);
            Document document = new Document(pdf);

            // Set margins: top, right, bottom, left
            document.setMargins(20, 20, 20, 20);

            // Add title
            Paragraph title = new Paragraph("Product Report")
                    .setBold()
                    .setFontSize(16)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(10);
            document.add(title);

            document.add(new Paragraph(" "));

            int itemsPerPage = 10;
            int totalItems = products.size();
            int totalPages = (int) Math.ceil((double) totalItems / itemsPerPage);

            int index = 1;
            // Calculate total for product total price
            long totalProductPrice = 0;

            for (int page = 0; page < totalPages; page++) {
                // Add new table for each page
                float[] columnWidths = {0.8f, 3f, 2f, 2f, 2.5f};
                Table table = new Table(UnitValue.createPercentArray(columnWidths));
                table.setWidth(UnitValue.createPercentValue(100));

                // Add table header
                table.addHeaderCell(new Cell().add(new Paragraph("No").setBold()).setTextAlignment(TextAlignment.CENTER));
                table.addHeaderCell(new Cell().add(new Paragraph("Product Name").setBold()).setTextAlignment(TextAlignment.CENTER));
                table.addHeaderCell(new Cell().add(new Paragraph("Product Price").setBold()).setTextAlignment(TextAlignment.CENTER));
                table.addHeaderCell(new Cell().add(new Paragraph("Category").setBold()).setTextAlignment(TextAlignment.CENTER));
                table.addHeaderCell(new Cell().add(new Paragraph("Available Stock").setBold()).setTextAlignment(TextAlignment.CENTER));

                // Add rows for the current page
                int start = page * itemsPerPage;
                int end = Math.min(start + itemsPerPage, totalItems);
                for (int i = start; i < end; i++) {
                    Products product = products.get(i);
                    // Round values
                    long productPrice = Math.round(product.getProductPrice());

                    // Add to total product price
                    totalProductPrice += productPrice;

                    // Format category (capitalize first letter, lowercase the rest)
                    String categoryFormatted = product.getCategories().toString();
                    categoryFormatted = categoryFormatted.substring(0, 1).toUpperCase() + categoryFormatted.substring(1).toLowerCase();
                    String availableStock = String.valueOf(product.getAvailableStock());
                    String displayText = "Not Ready";

                    if (availableStock.equals("READY")) {
                        displayText = "Ready";
                    }
                    table.addCell(new Cell().add(new Paragraph(String.valueOf(index++))).setTextAlignment(TextAlignment.CENTER));
                    table.addCell(new Cell().add(new Paragraph(product.getProductName())).setTextAlignment(TextAlignment.CENTER));
                    table.addCell(new Cell().add(new Paragraph(String.valueOf(productPrice))).setTextAlignment(TextAlignment.CENTER));
                    table.addCell(new Cell().add(new Paragraph(categoryFormatted)).setTextAlignment(TextAlignment.CENTER));
                    table.addCell(new Cell().add(new Paragraph(displayText))).setTextAlignment(TextAlignment.CENTER);
                }

                document.add(table);

                // Add a page break except for the last page
                if (page < totalPages - 1) {
                    document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
                }
            }

            // Add total purchase paragraph
            Paragraph total = new Paragraph("Grand Total Product Price: " + totalProductPrice)
                    .setBold()
                    .setFontSize(12)
                    .setTextAlignment(TextAlignment.RIGHT);
            document.add(total);

            document.close();

            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generating Product Report PDF", e);
        }
    }


    @Override
    public List<ProductResponseDTO> getAllProducts() {
        List<Products> products = productRepository.findAllAvailableStock(Products.AvailableStock.READY);
        return products.stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    private ProductResponseDTO convertToResponse(Products product) {
        return ProductResponseDTO.builder()
                .idProduct(product.getIdProduct())
                .usersResponseDTO(convertToUsersResponseDTO(product.getUser()))
                .productName(product.getProductName())
                .productPrice(product.getProductPrice())
                .categories(product.getCategories().name())
                .availableStock(String.valueOf(product.getAvailableStock()))
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }

    private UsersResponseDTO convertToUsersResponseDTO(Users user) {
        return UsersResponseDTO.builder()
                .idUser(user.getIdUser())
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole().name())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
