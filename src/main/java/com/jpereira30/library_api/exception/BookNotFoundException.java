package com.jpereira30.library_api.exception;

public class BookNotFoundException extends RuntimeException {

  public BookNotFoundException(Long id) {
    super("Book with ID " + id + " not found.");
  }
}
