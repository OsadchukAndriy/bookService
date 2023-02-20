package ua.osadchuk.bookService.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.osadchuk.bookService.models.Book;
import ua.osadchuk.bookService.models.Person;
import ua.osadchuk.bookService.security.PersonDetails;
import ua.osadchuk.bookService.services.PeopleService;

@Controller
@RequestMapping()
public class PeopleController {

    private final PeopleService peopleService;

    @Autowired
    public PeopleController(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @GetMapping("/")
    public String index(@ModelAttribute("book") Book book, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        model.addAttribute("people", personDetails.getPerson());
        int a = personDetails.getPerson().getId();

        if (personDetails.getPerson().getRole().equals("ROLE_ADMIN")) {
            return admin(model);
        }
        if (personDetails.getPerson().getRole().equals("ROLE_BLOCK")) {
            return block(model);
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

    @GetMapping("/admin")
    public String admin(Model model) {
        model.addAttribute("people", peopleService.findAll());
        return "admin";
    }

    @PatchMapping("/admin/{id}")
    public String updateRole(@PathVariable("id") int id, Model model) {
        Person person = peopleService.findOne(id);
        if (person.getRole().equals("ROLE_USER")) {
            person.setRole("ROLE_ADMIN");
        } else if (person.getRole().equals("ROLE_ADMIN")) {
            person.setRole("ROLE_USER");
        }
        peopleService.save(person);
        return admin(model);
    }

    @GetMapping("/block")
    public String block(Model model) {
        return "block";
    }

    @PatchMapping("/block/{id}")
    public String blockRole(@PathVariable("id") int id, Model model) {
        Person person = peopleService.findOne(id);
        if (person.getRole().equals("ROLE_USER") || person.getRole().equals("ROLE_ADMIN")) {
            person.setRole("ROLE_BLOCK");
        } else if (person.getRole().equals("ROLE_BLOCK")) {
            person.setRole("ROLE_USER");
        }
        peopleService.save(person);
        return admin(model);
    }
}
