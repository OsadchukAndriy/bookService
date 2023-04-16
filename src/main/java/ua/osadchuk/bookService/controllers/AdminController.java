package ua.osadchuk.bookService.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.osadchuk.bookService.models.Person;
import ua.osadchuk.bookService.services.PaginationService;
import ua.osadchuk.bookService.services.PeopleService;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final PeopleService peopleService;
    private final PaginationService paginationService;

    @Autowired
    public AdminController(PeopleService peopleService, PaginationService paginationService) {
        this.peopleService = peopleService;
        this.paginationService = paginationService;
    }

    @GetMapping("/{id}")
    public String admin(Model model ,@PathVariable("id") int id,
                        @RequestParam(value = "page", defaultValue = "1") int pageNumber) {

        int peoplePerPage = 5;
        model.addAttribute("person", peopleService.findOne(id));
        List<Person> people = peopleService.findAll();
        paginationService.paginate(people, pageNumber, peoplePerPage, model, "people");
        return "admin";
    }

    @PatchMapping("/{id}")
    public String updateRole(@PathVariable("id") int id, Model model) {
        Person person = peopleService.findOne(id);
        if (person.getRole().equals("ROLE_USER")) {
            person.setRole("ROLE_ADMIN");
        } else if (person.getRole().equals("ROLE_ADMIN")) {
            person.setRole("ROLE_USER");
        }
        peopleService.save(person);
        return "redirect:/";
    }
}
