package ru.rozvezev.DAO;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.rozvezev.models.Book;
import ru.rozvezev.models.Person;

import java.util.List;
import java.util.Optional;


@Component
public class LibraryDao {

    private final static PersonPropertyMapper personPropertyMapper = new PersonPropertyMapper();
    private final static BookPropertyMapper bookPropertyMapper = new BookPropertyMapper();
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public LibraryDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    //Methods for Person

    public List<Person> getPersonList() {
        String sqlQuery = "select * from person";
        return jdbcTemplate.query(sqlQuery, personPropertyMapper);
    }

    public Person getPersonById(int id){
        String sqlQuery = "select * from person where person_id=?";
        return jdbcTemplate.queryForObject(sqlQuery, personPropertyMapper, id);
    }

    public void savePerson(Person person) {
        String sqlQuery = "insert into person(full_name, birth_year) values(?, ?)";
        jdbcTemplate.update(sqlQuery, person.getFullName(), person.getBirthYear());
    }

    public void updatePerson(int personId, Person person) {
        String sqlQuery = "update person set full_name=?, birth_year=? where person_id=?";
        jdbcTemplate.update(sqlQuery, person.getFullName(), person.getBirthYear(), personId);
    }

    public void deletePerson(int personId) {
        String sqlQuery = "delete from person where person_id=?";
        jdbcTemplate.update(sqlQuery, personId);
    }

    public Optional<Person> getPersonByFullName(String name) {
        String sqlQuery = "select * from person where full_name=?";
        return jdbcTemplate.queryForStream(sqlQuery, personPropertyMapper, name).findAny();
    }

    //Methods for Book

    public List<Book> getBookList() {
        String sqlQuery = "select * from book";
        return jdbcTemplate.query(sqlQuery, bookPropertyMapper);
    }

    public List<Book> getBookList(int personId) {
        String sqlQuery = "select * from book where person_id=?";
        return jdbcTemplate.query(sqlQuery, bookPropertyMapper, personId);
    }

    public Book getBookById(int id) {
        String sqlQuery = "select * from book where book_id=?";
        return jdbcTemplate.queryForObject(sqlQuery, bookPropertyMapper, id);
    }

    public void saveBook(Book book) {
        String sqlQuery = "insert into book(person_id, name, author, year) values(null, ?, ?, ?)";
        jdbcTemplate.update(sqlQuery, book.getName(), book.getAuthor(), book.getYear());
    }

    public void updateBook(int bookId, Book book) {
        String sqlQuery = "update book set name=?, author=?, year=? where book_id=?";
        jdbcTemplate.update(sqlQuery,book.getName(), book.getAuthor(), book.getYear(), bookId);
    }

    public void deleteBook(int bookId) {
        String sqlQuery = "delete from book where book_id=?";
        jdbcTemplate.update(sqlQuery, bookId);
    }

    public Optional<Person> getBookOwner(int bookId) {
        String sqlQuery = "select p.* from book join person p on p.person_id = book.person_id where book_id=?";
        return jdbcTemplate.queryForStream(sqlQuery, personPropertyMapper, bookId).findAny();

    }

    public void release(int bookId) {
        String sqlQuery = "update book set person_id=null where book_id=?";
        jdbcTemplate.update(sqlQuery, bookId);
    }

    public void assign(int bookId, int personId) {
        String sqlQuery = "update book set person_id=? where book_id=?";
        jdbcTemplate.update(sqlQuery, personId, bookId);
    }

    //Join query

}
