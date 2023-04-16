package ua.osadchuk.bookService.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.osadchuk.bookService.models.Book;
import ua.osadchuk.bookService.security.PersonDetails;
import ua.osadchuk.bookService.services.PaginationService;
import ua.osadchuk.bookService.services.PeopleService;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping()
public class PeopleController {

    private final PeopleService peopleService;
    private final AdminController adminController;
    private final PaginationService paginationService;
    private final BlockController blockController;

    @Autowired
    public PeopleController(PeopleService peopleService, AdminController adminController, PaginationService paginationService, BlockController blockController) {
        this.peopleService = peopleService;
        this.adminController = adminController;
        this.paginationService = paginationService;
        this.blockController = blockController;
    }

    @GetMapping("/")
    public String index(@ModelAttribute("book") Book book, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        model.addAttribute("people", personDetails.getPerson());
        int a = personDetails.getPerson().getId();

        if (personDetails.getPerson().getRole().equals("ROLE_ADMIN")) {
            return adminController.admin(model, a, 1);
        }
        if (personDetails.getPerson().isBlock() == true) {
            return blockController.block();
        }

        return show(book, a, 1, null, model);
    }

    @GetMapping("people/{id}")
    public String show(@ModelAttribute("book") Book book,
                       @PathVariable("id") int id,
                       @RequestParam(value = "page", defaultValue = "1") int pageNumber,
                       @RequestParam(value = "searchTerm", required = false) String searchTerm,
                       Model model) {

        int booksPerPage = 8;
        model.addAttribute("person", peopleService.findOne(id));
        List<Book> books = peopleService.getBooksByPersonId(id);
        if (searchTerm != null && !searchTerm.isEmpty()) {
            books = books.stream()
                    .filter(b -> b.getName().contains(searchTerm) || b.getAuthor().contains(searchTerm))
                    .collect(Collectors.toList());
        }
        paginationService.paginate(books, pageNumber, booksPerPage, model, "books");
        model.addAttribute("book", new Book());
        return "people/show";
    }
}
