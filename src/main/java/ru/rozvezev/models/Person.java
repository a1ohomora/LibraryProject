package ru.rozvezev.models;

import javax.validation.constraints.*;

public class Person {

    private int personId;


    @Size(min = 1, max = 100, message = "Name length should be between 1 and 100")
    @Pattern(regexp = "[A-ZА-ЯЁa-zа-яё ]+", message = "Incorrect format of name. Please start every word" +
            " with capital letter.")
    private String fullName;


    @Min(value = 1900, message = "Birth year should be greater than 1900")
    private int birthYear;

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
}
