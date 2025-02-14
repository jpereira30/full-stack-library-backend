package com.jpereira30.library_api.controller;

import com.jpereira30.library_api.entity.Book;
import com.jpereira30.library_api.service.BookService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/books")
public class BookController {

  private final BookService bookService;

  public BookController(BookService bookService) {
    this.bookService = bookService;
  }

  // Create new book
  @PostMapping
  public ResponseEntity<Book> createBook(@Valid @RequestBody Book book) {
    Book newBook = bookService.createBook(book);
    return ResponseEntity.ok(newBook);
  }

  // Retrieve all books
  public ResponseEntity<List<Book>> retireveAllBooks() {
    return ResponseEntity.ok(bookService.retireveAllBooks());
  }

  // Retrieve single  book by ID
  @GetMapping("/{id}")
  public ResponseEntity<Book> getBookById(@PathVariable Long id) {
    return ResponseEntity.ok(bookService.retrieveBookById(id));
  }

  // Update book
  @PutMapping("/{id}")
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
  public ResponseEntity<List<Book>> searchBooks(
      @RequestParam(required = false, defaultValue = "") String title,
      @RequestParam(required = false, defaultValue = "") String author) {
    return ResponseEntity.ok(bookService.searchBooks(title, author));
  }
}
