package ua.osadchuk.bookService.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.osadchuk.bookService.models.Person;
import ua.osadchuk.bookService.services.PeopleService;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final PeopleService peopleService;

    @Autowired
    public AdminController(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @GetMapping("/")
    public String admin(Model model ,@PathVariable("id") int id) {
        model.addAttribute("people", peopleService.findAll());
        model.addAttribute("person", peopleService.findOne(id));
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
