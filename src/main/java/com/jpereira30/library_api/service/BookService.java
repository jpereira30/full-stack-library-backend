package com.jpereira30.library_api.service;

import com.jpereira30.library_api.entity.Book;
import com.jpereira30.library_api.exception.BookNotFoundException;
import com.jpereira30.library_api.repository.BookRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookService {

  private final BookRepository bookRepository;

  @Autowired
  public BookService(BookRepository bookRepository) {
    this.bookRepository = bookRepository;
  }

  // Create Book
  public Book createBook(Book book) {
    return bookRepository.save(book);
  }

  // Retrieve all Books
  public List<Book> retireveAllBooks() {
    return bookRepository.findAll();
  }

  // Retrieve Book by Id
  public Book retrieveBookById(Long id) {
    return bookRepository.findById(id).orElse(null);
  }

  // Update Book
  public Book updateBook(Long id, Book updatedBook) {
    if (!bookRepository.existsById(id)) {
      throw new BookNotFoundException(id);
    }
    updatedBook.setId(id);
    return bookRepository.save(updatedBook);
  }

  // Delete a book
  public void deleteBook(Long id) {
    if (!bookRepository.existsById(id)) {
      throw new BookNotFoundException(id);
    }
    bookRepository.deleteById(id);
  }

  // Search books by title or author
  public List<Book> searchBooks(String title, String author) {
    return bookRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(
        title, author);
  }
}
