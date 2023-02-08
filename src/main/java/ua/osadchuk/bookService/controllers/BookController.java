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
import java.util.Optional;

@Controller
@RequestMapping("/")
public class BookController {

    private final BooksService booksService;
    private final PeopleService peopleService;

    public BookController(BooksService booksService, PeopleService peopleService) {
        this.booksService = booksService;
        this.peopleService = peopleService;
    }
    @GetMapping("books/{id}")
    public String show(@PathVariable("id") int id, Model model,
                       @ModelAttribute("person")Person person){
        model.addAttribute("book", booksService.findOne(id));

        Person bookOwner = booksService.getBookOwner(id);
        if (bookOwner != null)
            model.addAttribute("owner", bookOwner);
        else
            model.addAttribute("people", peopleService.findAll());

        return "books/show";

    }

    @GetMapping("/books")
    public String index(Model model){
        model.addAttribute("books", booksService.findAll());
        return "books/index";
    }

    @GetMapping("books/new")
    public String newBook(@ModelAttribute("book") @Valid Book Book){
        return "books/new";
    }

    @PostMapping("people/books")
    public String create(@ModelAttribute("book") @Valid Book book,
    BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "books/new";
        }
        booksService.cave(book);
        return "redirect:/books";
    }
    @DeleteMapping("books/{id}")
    public String delete(@PathVariable("id") int id){
        booksService.delete(id);
        return "redirect:/books";
    }
}
