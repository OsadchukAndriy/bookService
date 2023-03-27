package ua.osadchuk.bookService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import ua.osadchuk.bookService.models.Book;
import ua.osadchuk.bookService.models.Person;
import ua.osadchuk.bookService.repositories.BooksRepository;
import ua.osadchuk.bookService.services.BooksService;

import java.util.List;


import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class BooksServiceTest {

	@Autowired
	private BooksService booksService;

	@Autowired
	private BooksRepository booksRepository;

	@Test
	@DirtiesContext
	public void testFindAll() {

		booksRepository.deleteAll();
		// створення книжок
		Book book1 = new Book("title1", "author1", 1995);
		Book book2 = new Book("title2", "author2", 1995);

		booksRepository.save(book1);
		booksRepository.save(book2);

		// перевірка результату
		List<Book> books = booksService.findAll();

		assertEquals(2, books.size());
		assertTrue(books.contains(book1));
		assertTrue(books.contains(book2));
	}

	@Test
	public void testFindOne() {
		// створення книжки
		Book book = new Book("title", "author", 1995);
		booksRepository.save(book);

		// перевірка результату
		Book foundBook = booksService.findOne(book.getId());

		assertEquals(book, foundBook);
	}

	@Test
	public void testSave() {
		// створення книжки
		Book book = new Book("title", "author", 1995);

		// збереження книжки
		booksService.save(book);

		// перевірка результату
		Book foundBook = booksRepository.findById(book.getId()).orElse(null);

		assertEquals(book, foundBook);
	}

	@Test
	public void testUpdate() {
		// створення книжки
		Book book = new Book("title", "author", 1995);
		booksRepository.save(book);

		// оновлення книжки
		Book updatedBook = new Book("newTitle", "newAuthor", 1995);
		booksService.update(book.getId(), updatedBook);

		// перевірка результату
		Book foundBook = booksRepository.findById(book.getId()).orElse(null);

		assertEquals(updatedBook.getName(), foundBook.getName());
		assertEquals(updatedBook.getAuthor(), foundBook.getAuthor());
	}

	@Test
	public void testDelete() {
		// створення книжки
		Book book = new Book("title", "author", 1995);
		booksRepository.save(book);

		// видалення книжки
		booksService.delete(book.getId());

		// перевірка результату
		assertFalse(booksRepository.findById(book.getId()).isPresent());
	}

	@Test
	public void testGetBookOwner() {
		// створення книжки з власником
		Person owner = new Person("John Doe");
		Book book = new Book("title", "author",  1995);
		book.setOwner(owner);
		booksRepository.save(book);

		// перевірка результату
		Person foundOwner = booksService.getBookOwner(book.getId());

		assertEquals(owner, foundOwner);
	}
}