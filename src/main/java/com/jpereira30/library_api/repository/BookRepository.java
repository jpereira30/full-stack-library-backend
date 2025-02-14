package com.jpereira30.library_api.repository;

import com.jpereira30.library_api.entity.Book;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

  // Custom query to search by title or author
  List<Book> findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(
      String title, String author);
}
