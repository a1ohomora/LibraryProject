package ru.rozvezev.service;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rozvezev.models.Person;
import ru.rozvezev.repositories.PeopleRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PeopleService {

    private final PeopleRepository peopleRepository;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    public List<Person> getAll(){
        return peopleRepository.findAll();
    }

    public Person getPersonById(int id) {
        return peopleRepository.findById(id).orElse(null);
    }

    public Person getPersonByIdWithBooks(int id) {
        Optional<Person> person = peopleRepository.findById(id);
        person.ifPresent(p -> Hibernate.initialize(p.getBooks()));
        return person.orElse(null);
    }

    public Optional<Person> getPersonByFullName(String fullName) {
        return peopleRepository.findByFullName(fullName);
    }

    @Transactional
    public void savePerson(Person person) {
        peopleRepository.save(person);
    }

    @Transactional
    public void updatePerson(int personId, Person person) {
        person.setPersonId(personId);
        peopleRepository.save(person);
    }

    @Transactional
    public void deletePerson(int personId) {
        peopleRepository.deleteById(personId);
    }

}
