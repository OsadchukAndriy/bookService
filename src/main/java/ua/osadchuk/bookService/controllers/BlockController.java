package ua.osadchuk.bookService.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.osadchuk.bookService.models.Person;
import ua.osadchuk.bookService.services.PeopleService;

@Controller
@RequestMapping("/block")
public class BlockController {

    private final PeopleService peopleService;
    private final AdminController adminController;

    @Autowired
    public BlockController(PeopleService peopleService, AdminController adminController) {
        this.peopleService = peopleService;
        this.adminController = adminController;
    }

    @GetMapping("/")
    public String block() {
        return "block";
    }

    @PatchMapping("/{id}")
    public String blockRole(@PathVariable("id") int id, Model model) {
        Person person = peopleService.findOne(id);
        if (person.isBlock() == true) {
            person.setBlock(false);
        } else if (person.isBlock() == false) {
            person.setBlock(true);
        }
        peopleService.save(person);
        return "redirect:/";
    }
}
