package ua.osadchuk.bookService.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.osadchuk.bookService.models.Book;

@Repository
public interface BooksRepository extends JpaRepository<Book, Integer> {
}
