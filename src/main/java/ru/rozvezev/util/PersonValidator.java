package ru.rozvezev.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.rozvezev.DAO.LibraryDao;
import ru.rozvezev.models.Person;
import ru.rozvezev.service.PeopleService;

import java.util.Calendar;
import java.util.Optional;

@Component
public class PersonValidator implements Validator {

    private final PeopleService peopleService;

    @Autowired
    public PersonValidator( PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    //Define which classes will be validated.
    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(Person.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Person target = (Person) o;
        Optional<Person> personFromDB = peopleService.getPersonByFullName(target.getFullName());

        //Validate if the entered year < current year.
        if (target.getBirthYear() > Calendar.getInstance().get(Calendar.YEAR))
            errors.rejectValue("birthYear", "", "Year should be less than current year");

        //Validate if the entered email address is in the database, for another person.
        if (personFromDB.isPresent() && personFromDB.get().getPersonId() != target.getPersonId())
            errors.rejectValue("fullName", "", "Name already exist");
    }
}
