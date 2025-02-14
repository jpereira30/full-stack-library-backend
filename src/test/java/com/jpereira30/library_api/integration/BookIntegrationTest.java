package com.jpereira30.library_api.integration;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpereira30.library_api.entity.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class BookIntegrationTest {

  @Autowired private TestRestTemplate restTemplate;

  @Autowired private ObjectMapper objectMapper;

  private Book testBook;

  @BeforeEach
  void setup() {
    testBook =
        new Book(
            null, "Integration Test Book", "Test Author", "1234567890", 2022, "Test Description");
  }

  @Test
  void testCreateAndGetBook() {
    // Create a new book
    ResponseEntity<Book> createResponse =
        restTemplate.postForEntity("/books", testBook, Book.class);
    assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    Book createdBook = createResponse.getBody();
    assertThat(createdBook).isNotNull();
    assertThat(createdBook.getTitle()).isEqualTo("Integration Test Book");

    // Retrieve the created book
    ResponseEntity<Book> getResponse =
        restTemplate.getForEntity("/books/" + createdBook.getId(), Book.class);
    assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(getResponse.getBody()).isNotNull();
    assertThat(getResponse.getBody().getTitle()).isEqualTo("Integration Test Book");
  }

  @Test
  void testUpdateBook() {
    // Create a new book first
    ResponseEntity<Book> createResponse =
        restTemplate.postForEntity("/books", testBook, Book.class);
    Book createdBook = createResponse.getBody();

    // Update the book details
    assert createdBook != null;
    Book updatedBook =
        new Book(
            createdBook.getId(),
            "Updated Title",
            "Updated Author",
            "1234567890",
            2023,
            "Updated Description");
    HttpEntity<Book> requestEntity = new HttpEntity<>(updatedBook);
    ResponseEntity<Book> updateResponse =
        restTemplate.exchange(
            "/books/" + createdBook.getId(), HttpMethod.PUT, requestEntity, Book.class);

    // Verify the update
    assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(updateResponse.getBody()).isNotNull();
    assertThat(updateResponse.getBody().getTitle()).isEqualTo("Updated Title");
  }

  @Test
  void testGetAllBooks() {
    // Add a book
    restTemplate.postForEntity("/books", testBook, Book.class);

    // Retrieve all books
    ResponseEntity<Book[]> response = restTemplate.getForEntity("/books", Book[].class);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isNotEmpty();
  }

  @Test
  void testSearchBooks() {
    // Add a book with a specific title
    Book book =
        new Book(null, "Spring Boot Guide", "John Doe", "1234567890", 2021, "Learn Spring Boot");
    restTemplate.postForEntity("/books", book, Book.class);

    // Search for the book
    ResponseEntity<Book[]> response =
        restTemplate.getForEntity("/books/search?title=Spring", Book[].class);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isNotEmpty();
    assertThat(response.getBody()[0].getTitle()).contains("Spring Boot Guide");
  }

  @Test
  void testDeleteBook() {
    // Create a new book
    ResponseEntity<Book> createResponse =
        restTemplate.postForEntity("/books", testBook, Book.class);
    Book createdBook = createResponse.getBody();

    // Delete the book
    assert createdBook != null;
    int id = createdBook.getId().intValue();
    restTemplate.delete("/books/" + createdBook.getId());

    // Attempt to retrieve the deleted book (expect 404)
    ResponseEntity<String> getResponse =
        restTemplate.getForEntity("/books/" + createdBook.getId(), String.class);
    assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    assertThat(getResponse.getBody()).contains("Book with ID " + id + " not found.");
  }

  @Test
  void testGetBookById_NotFound() {
    // Attempt to get a book that doesn't exist
    int id = 999;
    ResponseEntity<String> response = restTemplate.getForEntity("/books/" + id, String.class);

    // Validate the response
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    assertThat(response.getBody()).contains("Book with ID " + id + " not found.");
  }

  @Test
  void testUpdateBook_NotFound() {
    // Attempt to update a non-existent book
    Book book =
        new Book(
            999L, "Nonexistent Book", "Ghost Author", "1234567890", 2000, "This shouldn't exist");
    HttpEntity<Book> requestEntity = new HttpEntity<>(book);
    ResponseEntity<String> response =
        restTemplate.exchange("/books/999", HttpMethod.PUT, requestEntity, String.class);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }
}
