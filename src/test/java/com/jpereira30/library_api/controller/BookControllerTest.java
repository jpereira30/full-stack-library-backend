package com.jpereira30.library_api.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpereira30.library_api.entity.Book;
import com.jpereira30.library_api.exception.BookNotFoundException;
import com.jpereira30.library_api.service.BookService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(BookController.class)
class BookControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private BookService bookService;

  @Autowired private ObjectMapper objectMapper;

  @Test
  void testCreateBook() throws Exception {
    Book book = new Book(null, "Book1", "Author1", "123489123213", 2022, "Desc");
    Book savedBook = new Book(1L, "Book1", "Author1", "123489123213", 2022, "Desc");
    when(bookService.createBook(any(Book.class))).thenReturn(savedBook);
    mockMvc
        .perform(
            post("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(book)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.title").value("Book1"));
  }

  @Test
  void testGetAllBooks() throws Exception {
    List<Book> books = List.of(new Book(1L, "Book1", "Author1", "1112321311", 2021, "Desc1"));
    when(bookService.retrieveAllBooks()).thenReturn(books);

    mockMvc
        .perform(get("/books"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].title").value("Book1"))
        .andExpect(jsonPath("$[0].author").value("Author1"));
  }

  @Test
  void testGetBookById_Found() throws Exception {
    Book book = new Book(1L, "Book1", "Author1", "123", 2022, "Desc");
    when(bookService.retrieveBookById(1L)).thenReturn(book);
    mockMvc
        .perform(get("/books/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.title").value("Book1"));
  }

  @Test
  void testGetBookById_NotFound() throws Exception {
    when(bookService.retrieveBookById(1L)).thenThrow(new BookNotFoundException(1L));
    mockMvc.perform(get("/books/1")).andExpect(status().isNotFound());
  }

  @Test
  void testUpdateBook_Success() throws Exception {
    Book updatedBook = new Book(1L, "Updated Title", "Author", "12331312123", 2023, "Desc");
    when(bookService.updateBook(eq(1L), any(Book.class))).thenReturn(updatedBook);
    mockMvc
        .perform(
            put("/books/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedBook)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.title").value("Updated Title"));
  }

  @Test
  void testUpdateBook_NotFound() throws Exception {
    // Arrange
    Long bookId = 1L;
    Book book =
        new Book(bookId, "Updated Title", "Author", "12331312123", 2023, "Updated Description");

    // Simulate a RuntimeException being thrown by the service layer
    when(bookService.updateBook(eq(bookId), any(Book.class)))
        .thenThrow(new RuntimeException("Book not found"));

    // Act & Assert
    mockMvc
        .perform(
            put("/books/{id}", bookId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(book)))
        .andExpect(status().isNotFound());
  }

  @Test
  void testDeleteBook_Success() throws Exception {
    Mockito.doNothing().when(bookService).deleteBook(1L);
    mockMvc.perform(delete("/books/1")).andExpect(status().isNoContent());
  }

  @Test
  void testDeleteBook_RuntimeException() throws Exception {
    Long bookId = 1L;

    // Mock the service to throw a RuntimeException
    Mockito.doThrow(new BookNotFoundException(bookId)).when(bookService).deleteBook(bookId);

    // Perform the DELETE request
    mockMvc
        .perform(delete("/books/{id}", bookId).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }

  @Test
  void testSearchBooks() throws Exception {
    List<Book> books = List.of(new Book(1L, "Spring Boot", "Josh", "12331312123", 2021, "Desc"));
    when(bookService.searchBooks("Spring", "")).thenReturn(books);
    mockMvc
        .perform(get("/books/search?title=Spring"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].title").value("Spring Boot"));
  }
}
