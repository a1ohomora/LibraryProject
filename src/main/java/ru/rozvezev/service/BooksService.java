package ru.rozvezev.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rozvezev.models.Book;
import ru.rozvezev.models.Person;
import ru.rozvezev.repositories.BooksRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BooksService {

    private final BooksRepository booksRepository;

    @Autowired
    public BooksService(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    public List<Book> getAll(boolean sortByYear) {
        if (sortByYear){
            return booksRepository.findAll(Sort.by("year"));
        } else {
            return booksRepository.findAll();
        }
    }

    public List<Book> getAll(PageRequest pageRequest, boolean sortByYear) {
        if (sortByYear){
            return booksRepository.findAll(pageRequest.withSort(Sort.by("year"))).getContent();
        } else {
            return booksRepository.findAll(pageRequest).getContent();
        }
    }

    public Book getBookById(int id) {
        return booksRepository.findById(id).orElse(null);
    }

    public List<Book> getBookByNameStartingWith(String startingWith){
        return booksRepository.findByNameStartingWith(startingWith);
    }

    @Transactional
    public void saveBook(Book book) {
        booksRepository.save(book);
    }

    @Transactional
    public void updateBook(int bookId, Book book) {
        book.setBookId(bookId);
        book.setOwner(booksRepository.findById(bookId).get().getOwner());
        booksRepository.save(book);
    }

    @Transactional
    public void deleteBook(int bookId) {
        booksRepository.deleteById(bookId);
    }

    //TODO: change on SQL query?
    public Optional<Person> getBookOwner(int bookId) {
        Optional<Book> book = booksRepository.findById(bookId);
        return book.map(Book::getOwner);
    }


    //TODO: change on SQL query
    @Transactional
    public void release(int bookId) {
        booksRepository.findById(bookId).ifPresent(b -> {
            b.setOwner(null);
            b.setTakenAt(null);
            b.setExpired(false);
        });
    }

    @Transactional
    public void assign(int bookId, int personId) {
        booksRepository.assign(bookId, personId);
    }

}
