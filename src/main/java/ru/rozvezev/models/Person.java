package ru.rozvezev.models;


import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.List;

@Entity
@Table(name = "Person")
public class Person {

    @Id
    @Column(name = "person_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int personId;


    @Size(min = 1, max = 100, message = "Name length should be between 1 and 100")
    @Pattern(regexp = "[A-ZА-ЯЁa-zа-яё ]+", message = "Incorrect format of name. Please start every word" +
            " with capital letter.")
    @Column(name = "full_name")
    private String fullName;


    @Min(value = 1900, message = "Birth year should be greater than 1900")
    @Column(name = "birth_year")
    private int birthYear;

    @OneToMany(mappedBy = "owner", cascade = {CascadeType.PERSIST, CascadeType.REFRESH})
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    private List<Book> books;

    public Person() {}

    public Person(String fullName, int birthYear) {
        this.fullName = fullName;
        this.birthYear = birthYear;
    }

    /* Setters and getters */

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(int birthYear) {
        this.birthYear = birthYear;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
}
