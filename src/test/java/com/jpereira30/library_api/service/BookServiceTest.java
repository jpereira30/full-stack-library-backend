package com.jpereira30.library_api.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.jpereira30.library_api.entity.Book;
import com.jpereira30.library_api.exception.BookNotFoundException;
import com.jpereira30.library_api.repository.BookRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class BookServiceTest {

  @Mock private BookRepository bookRepository;

  private BookService bookService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    bookService = new BookService(bookRepository);
  }

  @Test
  void testCreateBook() {
    Book book = new Book(1L, "Test Book", "Author", "12345", 2023, "Test Description");
    when(bookRepository.save(book)).thenReturn(book);
    Book createdBook = bookService.createBook(book);
    assertNotNull(createdBook);
    assertEquals("Test Book", createdBook.getTitle());
  }

  @Test
  void testGetAllBooks() {
    List<Book> books = List.of(new Book(1L, "Book1", "Author1", "111", 2021, "Desc1"));
    when(bookRepository.findAll()).thenReturn(books);
    List<Book> result = bookService.retrieveAllBooks();
    assertEquals(1, result.size());
    assertEquals("Book1", result.getFirst().getTitle());
  }

  @Test
  void testGetBookById_BookFound() {
    Long id = 1L;
    Book book = new Book(id, "Book", "Author", "111", 2022, "Desc");
    when(bookRepository.findById(id)).thenReturn(Optional.of(book));
    Optional<Book> result = bookService.retrieveBookById(id);
    assertTrue(result.isPresent());
    assertEquals("Book", result.get().getTitle());
  }

  @Test
  void testGetBookById_NotFound() {
    Long id = 111L;
    when(bookRepository.findById(id)).thenReturn(Optional.empty());
    Optional<Book> result = bookService.retrieveBookById(id);
    assertThat(result).isEmpty();
  }

  @Test
  void testUpdateBook_Success() {
    Long id = 1L;
    Book updatedBook = new Book(id, "New Title", "Author", "111", 2020, "New Desc");
    when(bookRepository.existsById(id)).thenReturn(true);
    when(bookRepository.save(updatedBook)).thenReturn(updatedBook);
    Book result = bookService.updateBook(id, updatedBook);
    assertEquals("New Title", result.getTitle());
  }

  @Test
  void testUpdateBook_NotFound() {
    Long id = 2L;
    Book book = new Book(id, "Title", "Author", "222", 2021, "Desc");
    when(bookRepository.existsById(id)).thenReturn(false);
    assertThrows(BookNotFoundException.class, () -> bookService.updateBook(id, book));
  }

  @Test
  void testDeleteBook_Success() {
    Long id = 1L;
    when(bookRepository.existsById(id)).thenReturn(true);
    assertDoesNotThrow(() -> bookService.deleteBook(id));
    verify(bookRepository, times(1)).deleteById(id);
  }

  @Test
  void testDeleteBook_NotFound() {
    Long id = 2L;
    when(bookRepository.existsById(id)).thenReturn(false);
    assertThrows(BookNotFoundException.class, () -> bookService.deleteBook(id));
  }

  @Test
  void testSearchBooks() {
    String title = "Spring";
    String author = "Josh";
    List<Book> expectedBooks =
        List.of(new Book(1L, "Spring Boot", "Josh Long", "123", 2021, "Desc"));
    when(bookRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(title, author))
        .thenReturn(expectedBooks);
    List<Book> result = bookService.searchBooks(title, author);
    assertEquals(1, result.size());
    assertEquals("Spring Boot", result.get(0).getTitle());
  }
}
