package ru.rozvezev.util;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.rozvezev.DAO.LibraryDao;
import ru.rozvezev.models.Book;

import java.util.Calendar;

@Component
public class BookValidator implements Validator {

    private final LibraryDao libraryDao;

    public BookValidator(LibraryDao libraryDao) {
        this.libraryDao = libraryDao;
    }

    //Define which classes will be validated.
    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(Book.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Book book = (Book) o;

        if (book.getYear() > Calendar.getInstance().get(Calendar.YEAR))
            errors.rejectValue("year", "", "Year should be less than current year");
    }
}
