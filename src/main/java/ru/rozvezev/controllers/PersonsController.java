package ru.rozvezev.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.rozvezev.DAO.LibraryDao;
import ru.rozvezev.models.Person;
import ru.rozvezev.service.PeopleService;
import ru.rozvezev.util.PersonValidator;

import javax.validation.Valid;


@Controller
@RequestMapping("/persons")
public class PersonsController {

    private final PeopleService peopleService;
    private final PersonValidator personValidator;

    @Autowired
    public PersonsController(PeopleService peopleService, PersonValidator personValidator) {
        this.peopleService = peopleService;
        this.personValidator = personValidator;
    }

    @GetMapping()
    public String showPeople(Model model){
        model.addAttribute("persons", peopleService.getAll());
        return "persons/persons_view";
    }

    @GetMapping("/new")
    public String personCreatePage(@ModelAttribute("person") Person person){
        return "/persons/create_person_view";
    }

    @PostMapping()
    public String createPerson(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult){
        personValidator.validate(person, bindingResult);
        if (bindingResult.hasErrors())
            return "/persons/create_person_view";


        peopleService.savePerson(person);
        return "redirect:/persons";
    }

    @GetMapping("/{personId}")
    public String showPerson(@PathVariable("personId") int personId, Model model){
        Person person = peopleService.getPersonByIdWithBooks(personId);
        model.addAttribute("person", person);
        model.addAttribute("books", person.getBooks());
        return "/persons/person_info_view";
    }


    @GetMapping("/{personId}/edit")
    public String personEditPage(@PathVariable("personId") int personId, Model model){
        model.addAttribute("person", peopleService.getPersonById(personId));
        return "/persons/edit_person_view";
    }

    @PatchMapping("/{personId}")
    public String editPerson(@PathVariable("personId") int personId, @ModelAttribute("person") @Valid Person person,
                             BindingResult bindingResult){
        personValidator.validate(person, bindingResult);
        if (bindingResult.hasErrors())
            return "/persons/edit_person_view";

        peopleService.updatePerson(personId, person);
        return "redirect:/persons/" + personId;
    }

    @DeleteMapping("/{personId}")
    public String deletePerson(@PathVariable("personId") int personId){
        peopleService.deletePerson(personId);
        return "redirect:/persons";
    }

}
