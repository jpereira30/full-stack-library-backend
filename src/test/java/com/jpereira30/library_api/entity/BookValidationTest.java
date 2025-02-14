package com.jpereira30.library_api.entity;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class BookValidationTest {

  private static Validator validator;

  @BeforeAll
  static void setUp() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Test
  void testValidBook() {
    Book validBook =
        Book.builder()
            .title("Effective Java")
            .author("Joshua Bloch")
            .isbn("9780134685991")
            .publicationYear(2018)
            .description("A must-read for Java developers.")
            .build();

    Set<ConstraintViolation<Book>> violations = validator.validate(validBook);

    assertThat(violations).isEmpty();
  }

  @Test
  void testBlankTitle() {
    Book book =
        Book.builder()
            .title("")
            .author("Joshua Bloch")
            .isbn("9780134685991")
            .publicationYear(2018)
            .description("A must-read for Java developers.")
            .build();

    Set<ConstraintViolation<Book>> violations = validator.validate(book);

    assertThat(violations).hasSize(1);
    assertThat(violations.iterator().next().getMessage()).isEqualTo("Title is required");
  }

  @Test
  void testBlankAuthor() {
    Book book =
        Book.builder()
            .title("Effective Java")
            .author("")
            .isbn("9780134685991")
            .publicationYear(2018)
            .description("A must-read for Java developers.")
            .build();

    Set<ConstraintViolation<Book>> violations = validator.validate(book);

    assertThat(violations).hasSize(1);
    assertThat(violations.iterator().next().getMessage()).isEqualTo("Author is required");
  }

  @Test
  void testInvalidISBNLength() {
    Book book =
        Book.builder()
            .title("Effective Java")
            .author("Joshua Bloch")
            .isbn("1234")
            .publicationYear(2018)
            .description("A must-read for Java developers.")
            .build();

    Set<ConstraintViolation<Book>> violations = validator.validate(book);

    assertThat(violations).hasSize(1);
    assertThat(violations.iterator().next().getMessage())
        .isEqualTo("ISBN must be between 10 and 13 characters");
  }

  @Test
  void testNullPublicationYear() {
    Book book =
        Book.builder()
            .title("Effective Java")
            .author("Joshua Bloch")
            .isbn("9780134685991")
            .publicationYear(null)
            .description("A must-read for Java developers.")
            .build();

    Set<ConstraintViolation<Book>> violations = validator.validate(book);

    assertThat(violations).hasSize(1);
    assertThat(violations.iterator().next().getMessage()).isEqualTo("Publication year is required");
  }

  @Test
  void testBlankDescription() {
    Book book =
        Book.builder()
            .title("Effective Java")
            .author("Joshua Bloch")
            .isbn("9780134685991")
            .publicationYear(2018)
            .description("")
            .build();

    Set<ConstraintViolation<Book>> violations = validator.validate(book);

    assertThat(violations).hasSize(1);
    assertThat(violations.iterator().next().getMessage()).isEqualTo("Description is required");
  }

  @Test
  void testDescriptionTooLong() {
    String longDescription = "A".repeat(501); // Exceeds 500 characters
    Book book =
        Book.builder()
            .title("Effective Java")
            .author("Joshua Bloch")
            .isbn("9780134685991")
            .publicationYear(2018)
            .description(longDescription)
            .build();

    Set<ConstraintViolation<Book>> violations = validator.validate(book);

    assertThat(violations).hasSize(1);
    assertThat(violations.iterator().next().getMessage())
        .isEqualTo("Description must be at most 500 characters");
  }
}
