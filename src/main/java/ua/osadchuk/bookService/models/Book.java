package ua.osadchuk.bookService.models;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Entity
@Table(name = "Book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    @Size(min = 2, max = 100, message = "Book name must be between 2 and 100 characters long")
    private String name;

    @NotEmpty(message = "Author must not be empty")
    @Size(min = 2, max = 100, message = "Author name must be between 2 and 100 characters long")
    @Column(name = "author")
    private String author;

    @Min(value = 1500, message = "Year must be greater than 1500")
    @Max(value = 2023, message = "The year must be less than 2023")
    @Column(name = "year_of_release")
    private int yearOfRealise;

    @ManyToOne
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    private Person owner;

    public Book() {
    }

    public Book(String name, String author, int yearOfRealise) {
        this.name = name;
        this.author = author;
        this.yearOfRealise = yearOfRealise;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getYearOfRealise() {
        return yearOfRealise;
    }

    public void setYearOfRealise(int yearOfRealise) {
        this.yearOfRealise = yearOfRealise;
    }

    public Person getOwner() {
        return owner;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", yearOfRealise=" + yearOfRealise +
                '}';
    }
}
