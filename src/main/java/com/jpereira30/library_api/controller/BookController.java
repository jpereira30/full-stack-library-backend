package com.jpereira30.library_api.controller;

import com.jpereira30.library_api.entity.Book;
import com.jpereira30.library_api.service.AIService;
import com.jpereira30.library_api.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/books")
@Tag(name = "Books", description = "API endpoints for managing books")
public class BookController {

  private final BookService bookService;

  private final AIService aiService;

  public BookController(BookService bookService, AIService aiService) {
    this.bookService = bookService;
    this.aiService = aiService;
  }

  // Create new book
  @PostMapping
  @Operation(summary = "Create a new book", description = "Adds a new book to the library")
  public ResponseEntity<Book> createBook(@Valid @RequestBody Book book) {
    Book newBook = bookService.createBook(book);
    return ResponseEntity.ok(newBook);
  }

  // Retrieve all books
  @GetMapping
  @Operation(
      summary = "Retrieve all books",
      description = "Fetches a list of all books in the library")
  public ResponseEntity<List<Book>> retrieveAllBooks() {
    return ResponseEntity.ok(bookService.retrieveAllBooks());
  }

  // Retrieve single  book by ID
  @GetMapping("/{id}")
  @Operation(
      summary = "Retrieve a book by ID",
      description = "Fetches the details of a single book by its ID")
  public ResponseEntity<Book> getBookById(@PathVariable Long id) {
    return bookService
        .retrieveBookById(id)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  // Update book
  @PutMapping("/{id}")
  @Operation(
      summary = "Update a book",
      description = "Updates the details of an existing book by its ID")
  public ResponseEntity<Book> updateBook(@PathVariable Long id, @Valid @RequestBody Book book) {
    try {
      Book updatedBook = bookService.updateBook(id, book);
      return ResponseEntity.ok(updatedBook);
    } catch (RuntimeException e) {
      return ResponseEntity.notFound().build();
    }
  }

  // Delete a book
  @DeleteMapping("/{id}")
  @Operation(summary = "Delete a book", description = "Deletes an existing book by its ID")
  public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
    try {
      bookService.deleteBook(id);
      return ResponseEntity.noContent().build();
    } catch (RuntimeException e) {
      return ResponseEntity.notFound().build();
    }
  }

  // Search books by title or author
  @GetMapping("/search")
  @Operation(
      summary = "Search for books",
      description = "Searches for books by title and/or author")
  public ResponseEntity<List<Book>> searchBooks(
      @RequestParam(required = false, defaultValue = "") String title,
      @RequestParam(required = false, defaultValue = "") String author) {
    return ResponseEntity.ok(bookService.searchBooks(title, author));
  }

  @GetMapping("/{id}/ai-insights")
  @Operation(
      summary = "Get AI-generated insights for a book",
      description = "Retrieves an AI-generated tagline based on the book's description.")
  public ResponseEntity<Map<String, Object>> getAIInsights(@PathVariable Long id) {
    Optional<Book> optionalBook = bookService.retrieveBookById(id);

    if (optionalBook.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    Book book = optionalBook.get();
    String description = book.getDescription();
    String insights = aiService.generateInsights(description).block();

    Map<String, Object> response =
        Map.of("book", book, "insights", insights != null ? insights : "No insights available");

    return ResponseEntity.ok(response);
  }
}
