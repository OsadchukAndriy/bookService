package ua.osadchuk.bookService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import ua.osadchuk.bookService.models.Book;
import ua.osadchuk.bookService.models.Person;
import ua.osadchuk.bookService.repositories.PeopleRepository;
import ua.osadchuk.bookService.services.PeopleService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
public class PeopleServiceTest {

    @Mock
    private PeopleRepository peopleRepository;

    @InjectMocks
    private PeopleService peopleService;

    @Test
    public void testFindAll() {
        List<Person> peopleList = new ArrayList<>();
        peopleList.add(new Person("John"));
        peopleList.add(new Person("Jane"));

        Mockito.when(peopleRepository.findAll()).thenReturn(peopleList);

        List<Person> result = peopleService.findAll();

        assertEquals(result.size(), 2);
        assertEquals(result.get(0).getName(), "John");
        assertEquals(result.get(1).getName(), "Jane");
    }

    @Test
    public void findOne() {
        Person person = new Person("Andrii");
        person.setId(1);
        Mockito.when(peopleRepository.findById(1)).thenReturn(Optional.of(person));
        Person foundPerson = peopleService.findOne(1);
        assertNotNull(foundPerson);
        assertEquals(foundPerson.getName(), "Andrii");

    }

    @Test
    public void getPersonByName() {
        Person person = new Person("Andrii");
        person.setId(1);

        Mockito.when(peopleRepository.findByName("Andrii")).thenReturn(Optional.of(person));
        Optional<Person> foundPerson = peopleService.getPersonByName("Andrii");

        assertTrue(foundPerson.isPresent());
        assertEquals(foundPerson.get().getName(), "Andrii");
    }

    @Test
    public void save() {
        Person person = new Person("Andrii");

        peopleService.save(person);

        Mockito.verify(peopleRepository, Mockito.times(1)).save(person);

    }

    @Test
    public void getBooksByPersonId() {
        Optional<Person> expectedPerson = Optional.of(new Person("Andrii"));
        List<Book> books = new ArrayList<>();
        books.add(new Book("title", "author", 1995));
        books.add(new Book("title2", "author2", 1995));

        expectedPerson.get().setBooks(books);
        Mockito.when(peopleRepository.findById(1)).thenReturn(expectedPerson);

        //Act
        List<Book> actualBooks = peopleService.getBooksByPersonId(1);

        //Assert
        assertEquals(books, actualBooks);
    }
}
