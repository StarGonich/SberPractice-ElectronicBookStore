package ru.sber.practice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "books")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private String language;

    @Column(name = "publication_year", nullable = false)
    private Integer publicationYear;

    private String genre;
    private String description;
    private String isbn;

    @Column(name = "page_count")
    private Integer pageCount;

    private Double rating;

    @Column(name = "is_new")
    private Boolean isNew;

    @Column(name = "image_path")
    private String imagePath;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(name = "stock_quantity", nullable = false)
    private Integer stockQuantity;
}
