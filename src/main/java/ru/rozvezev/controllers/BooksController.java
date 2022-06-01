package ru.rozvezev.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.rozvezev.DAO.LibraryDao;
import ru.rozvezev.models.Book;
import ru.rozvezev.models.Person;
import ru.rozvezev.util.BookValidator;

import javax.validation.Valid;
import java.util.Optional;


@Controller
@RequestMapping("/books")
public class BooksController {

    private final LibraryDao libraryDAO;
    private final BookValidator bookValidator;

    @Autowired
    public BooksController(LibraryDao libraryDAO, BookValidator bookValidator) {
        this.libraryDAO = libraryDAO;
        this.bookValidator = bookValidator;
    }

    @GetMapping()
    public String showBooks(Model model){
        model.addAttribute("books", libraryDAO.getBookList());
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

        libraryDAO.saveBook(book);
        return "redirect:/books";
    }

    @GetMapping("/{bookId}")
    public String showBook(@PathVariable("bookId") int bookId, Model model, @ModelAttribute("person") Person person){
        Book book = libraryDAO.getBookById(bookId);
        model.addAttribute("book", book);

        Optional<Person> bookOwner = libraryDAO.getBookOwner(bookId);
        if (bookOwner.isPresent()){
            model.addAttribute("owner", bookOwner.get());
        } else {
            model.addAttribute("persons", libraryDAO.getPersonList());
        }
        return "/books/book_info_view";
    }

    @GetMapping("/{bookId}/edit")
    public String bookEditPage(@PathVariable("bookId") int bookId, Model model){
        model.addAttribute("book", libraryDAO.getBookById(bookId));
        return "/books/edit_book_view";
    }

    @PatchMapping("/{bookId}")
    public String editBook(@PathVariable("bookId") int bookId, @ModelAttribute("book") @Valid Book book,
                           BindingResult bindingResult){
        bookValidator.validate(book, bindingResult);
        if (bindingResult.hasErrors())
            return "/books/edit_book_view";

        libraryDAO.updateBook(bookId, book);
        return "redirect:/books/" + bookId;
    }


    @PatchMapping("/{bookId}/release")
    public String release(@PathVariable("bookId") int bookId){
        libraryDAO.release(bookId);
        return "redirect:/books/" + bookId;
    }

    @PatchMapping("/{bookId}/assign")
    public String assign(@PathVariable("bookId") int bookId, @ModelAttribute("person") Person person){
        libraryDAO.assign(bookId, person.getPersonId());
        return "redirect:/books/" + bookId;
    }

    @DeleteMapping("/{bookId}")
    public String deleteBook(@PathVariable("bookId") int bookId){
        libraryDAO.deleteBook(bookId);
        return "redirect:/books";
    }


}
