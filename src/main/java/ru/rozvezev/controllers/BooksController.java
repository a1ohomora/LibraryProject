package ru.rozvezev.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.rozvezev.models.Book;
import ru.rozvezev.models.Person;
import ru.rozvezev.service.BooksService;
import ru.rozvezev.service.PeopleService;
import ru.rozvezev.util.BookValidator;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Controller
@RequestMapping("/books")
public class BooksController {

    private final BooksService booksService;
    private final PeopleService peopleService;
    private final BookValidator bookValidator;

    @Autowired
    public BooksController(BooksService booksService, PeopleService peopleService, BookValidator bookValidator) {
        this.booksService = booksService;
        this.peopleService = peopleService;
        this.bookValidator = bookValidator;
    }

    @GetMapping()
    public String showBooks(Model model, @RequestParam(name = "page") Optional<Integer> page,
                            @RequestParam(name = "books_per_page") Optional<Integer> booksPerPage,
                            @RequestParam(name = "sort_by_year", defaultValue = "false") boolean sortByYear){
        List<Book> books; PageRequest pageRequest = null;
        if (page.isPresent()){
            pageRequest = PageRequest.of(page.get(), booksPerPage.orElse(10));
            books = booksService.getAll(pageRequest, sortByYear);
        } else {
            books = booksService.getAll(sortByYear);
        }

        model.addAttribute("books", books);
        model.addAttribute("pageRequest", pageRequest);
        return "books/books_view";
    }

    @GetMapping("/new")
    public String bookCreatePage(@ModelAttribute("book") Book book){
        return "/books/create_book_view";
    }

    @PostMapping()
    public String createBook(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult){
        bookValidator.validate(book, bindingResult);
        if (bindingResult.hasErrors())
            return "/books/create_book_view";

        booksService.saveBook(book);
        return "redirect:/books?page=0";
    }

    @GetMapping("/{bookId}")
    public String showBook(@PathVariable("bookId") int bookId, Model model, @ModelAttribute("person") Person person){
        Book book = booksService.getBookById(bookId);
        model.addAttribute("book", book);

        Person bookOwner = book.getOwner();
        if (Objects.nonNull(bookOwner)){
            model.addAttribute("owner", bookOwner);
        } else {
            model.addAttribute("persons", peopleService.getAll());
        }
        return "/books/book_info_view";
    }

    @GetMapping("/{bookId}/edit")
    public String bookEditPage(@PathVariable("bookId") int bookId, Model model){
        model.addAttribute("book", booksService.getBookById(bookId));
        return "/books/edit_book_view";
    }

    @PatchMapping("/{bookId}")
    public String editBook(@PathVariable("bookId") int bookId, @ModelAttribute("book") @Valid Book book,
                           BindingResult bindingResult){
        bookValidator.validate(book, bindingResult);
        if (bindingResult.hasErrors())
            return "/books/edit_book_view";

        booksService.updateBook(bookId, book);
        return "redirect:/books/" + bookId;
    }


    @PatchMapping("/{bookId}/release")
    public String release(@PathVariable("bookId") int bookId){
        booksService.release(bookId);
        return "redirect:/books/" + bookId;
    }

    @PatchMapping("/{bookId}/assign")
    public String assign(@PathVariable("bookId") int bookId, @ModelAttribute("person") Person person){
        booksService.assign(bookId, person.getPersonId());
        return "redirect:/books/" + bookId;
    }

    @DeleteMapping("/{bookId}")
    public String deleteBook(@PathVariable("bookId") int bookId){
        booksService.deleteBook(bookId);
        return "redirect:/books?page=0";
    }

    @GetMapping("/search")
    public String searchPage(){
        return "/books/search_view";
    }

    @PostMapping("/search")
    public String searchBook(Model model, @RequestParam(name = "starting_with") String startingWith){
        model.addAttribute("books", booksService.getBookByNameStartingWith(startingWith));
        return "/books/search_view";
    }


}
