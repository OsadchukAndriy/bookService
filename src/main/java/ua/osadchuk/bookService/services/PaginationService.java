package ua.osadchuk.bookService.services;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import ua.osadchuk.bookService.models.Book;
import ua.osadchuk.bookService.models.Person;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Service
public class PaginationService {

    public <T> List<T> paginate(List<T> items, int pageNumber, int itemsPerPage, Model model, String attributeName) {
        int totalItems = items.size();
        int totalPages = (int) Math.ceil((double) totalItems / itemsPerPage);
        int startIndex = (totalItems - (pageNumber - 1) * itemsPerPage) - 1;
        int endIndex = Math.max(startIndex - itemsPerPage, -1) + 1;

        List<T> pageItems = items.subList(endIndex, startIndex + 1);

        model.addAttribute(attributeName, pageItems);
        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("start", endIndex);

        return pageItems;
    }
}
