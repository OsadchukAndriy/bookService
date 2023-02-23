package ua.osadchuk.bookService.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.osadchuk.bookService.models.Book;
import ua.osadchuk.bookService.security.PersonDetails;
import ua.osadchuk.bookService.services.PeopleService;

@Controller
@RequestMapping()
public class PeopleController {

    private final PeopleService peopleService;
    private final AdminController adminController;

    private final BlockController blockController;

    @Autowired
    public PeopleController(PeopleService peopleService, AdminController adminController, BlockController blockController) {
        this.peopleService = peopleService;
        this.adminController = adminController;
        this.blockController = blockController;
    }

    @GetMapping("/")
    public String index(@ModelAttribute("book") Book book, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        model.addAttribute("people", personDetails.getPerson());
        int a = personDetails.getPerson().getId();

        if (personDetails.getPerson().getRole().equals("ROLE_ADMIN")) {
            return adminController.admin(model, a);
        }
        if (personDetails.getPerson().isBlock() == true) {
            return blockController.block();
        }

        return show(book, a, model);
    }

    @GetMapping("people/{id}")
    public String show(@ModelAttribute("book") Book book,
                       @PathVariable("id") int id, Model model) {
        model.addAttribute("person", peopleService.findOne(id));
        model.addAttribute("books", peopleService.getBooksByPersonId(id));
        model.addAttribute("book");
        return "people/show";
    }
}
