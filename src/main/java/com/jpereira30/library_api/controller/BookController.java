package com.jpereira30.library_api.controller;

import com.jpereira30.library_api.entity.Book;
import com.jpereira30.library_api.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/books")
@Tag(name = "Books", description = "API endpoints for managing books")
public class BookController {

  private final BookService bookService;

  public BookController(BookService bookService) {
    this.bookService = bookService;
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
    return ResponseEntity.ok(bookService.retrieveBookById(id));
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
}
