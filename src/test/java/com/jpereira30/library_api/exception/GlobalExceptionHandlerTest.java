package com.jpereira30.library_api.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpereira30.library_api.entity.Book;
import com.jpereira30.library_api.service.BookService;
import java.util.Objects;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest
class GlobalExceptionHandlerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private BookService bookService;

  @Autowired private ObjectMapper objectMapper;
  private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

  @Test
  void testHandleRuntimeException() {
    RuntimeException ex = new RuntimeException("Something went wrong");
    ResponseEntity<String> response = handler.handleRuntimeException(ex);
    assertEquals(500, response.getStatusCode().value());
    assertEquals("Internal server error: Something went wrong", response.getBody());
  }

  @Test
  void testHandleBookNotFoundException() {
    BookNotFoundException ex = new BookNotFoundException(1L);
    ResponseEntity<String> response = handler.handleRuntimeException(ex);
    assertEquals(500, response.getStatusCode().value());
    assertEquals("Internal server error: Book with ID 1 not found.", response.getBody());
  }

  @Test
  void testHandleValidationException() throws Exception {
    // Create an invalid Book object (title missing)
    Book invalidBook =
        Book.builder()
            .title("") // invalid
            .author("Author")
            .isbn("1234567890")
            .publicationYear(2023)
            .description("Description")
            .build();

    // Perform POST request to trigger validation error
    mockMvc
        .perform(
            post("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidBook)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.title").value("Title is required"));
  }

  // Test for generic Exception
  @Test
  void testHandleGenericException() {
    Exception ex = new Exception("Unexpected error occurred");

    ResponseEntity<String> response = handler.handleGenericException(ex);

    assertEquals(500, response.getStatusCode().value());
    assertTrue(
        Objects.requireNonNull(response.getBody())
            .contains("Unexpected error: Unexpected error occurred"));
  }
}
