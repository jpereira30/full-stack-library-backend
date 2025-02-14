package com.jpereira30.library_api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "books")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank(message = "Title is required")
  @Column(nullable = false, length = 180)
  private String title;

  @NotBlank(message = "Author is required")
  @Size(max = 50, message = "Author name must be at most 50 characters")
  private String author;

  @NotBlank(message = "ISBN is required")
  @Size(min = 10, max = 13, message = "ISBN must be between 10 and 13 characters")
  private String isbn;

  @NotNull(message = "Publication year is required")
  private Integer publicationYear;

  @NotBlank(message = "Description is required")
  @Size(max = 500, message = "Description must be at most 500 characters")
  private String description;
}
