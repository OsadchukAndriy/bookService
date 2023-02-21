package ua.osadchuk.bookService.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.osadchuk.bookService.models.Book;
import ua.osadchuk.bookService.models.Person;
import ua.osadchuk.bookService.services.BooksService;
import ua.osadchuk.bookService.services.PeopleService;

import javax.validation.Valid;


@Controller
@RequestMapping("/books")
public class BookController {

    private final BooksService booksService;
    private final PeopleService peopleService;

    public BookController(BooksService booksService, PeopleService peopleService) {
        this.booksService = booksService;
        this.peopleService = peopleService;
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model,
                       @ModelAttribute("person") Person person) {
        model.addAttribute("book", booksService.findOne(id));

        Person bookOwner = booksService.getBookOwner(id);
        if (bookOwner != null)
            model.addAttribute("owner", bookOwner);
        else
            model.addAttribute("people", peopleService.findAll());

        return "books/show";

    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("books", booksService.findAll());
        return "books/index";
    }

    @GetMapping("/new")
    public String newBook(@ModelAttribute("book") @Valid Book book) {
        return "books/new";
    }

    @PostMapping("/{id}")
    public String create(@PathVariable("id") int id, @ModelAttribute("book") @Valid Book book,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "books/new";
        }
        book.setOwner(peopleService.findOne(id));
        book.setId(0);
        booksService.save(book);
        return "redirect:/";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        int i = booksService.getBookOwner(id).getId();
        booksService.delete(id);
        return "redirect:/people/" + i;
    }
}
