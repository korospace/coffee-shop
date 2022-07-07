package edu.mum.coffee.controller;

import edu.mum.coffee.domain.Person;
import edu.mum.coffee.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/person")
public class PersonController {

    @Autowired
    PersonRepository personRepository;
    
    @Autowired
    PasswordEncoder passwordEncoder;

    /*
     * Show View List person
     */
    @GetMapping
    public String listAdmin(Model model) {
        model.addAttribute("persons", personRepository.findAll());
        return "person/list";
    }

    /*
     * Show View Add person
     */
    @GetMapping("/add")
    public String showPersonAdd(Model model) {
        model.addAttribute("person", new Person());
        return "person/crud";
    }

    /*
     * Show View Edit person
     */
    @GetMapping("/edit/{id}")
    public String showPersonEdit(@PathVariable Long id, Model model) {
        Person person = personRepository.findOne(id);
        model.addAttribute("person", person);
        return "person/crud";
    }

    /*
     * Save Add person
     */
    @PostMapping("/add")
    public String saveAddPerson(@Valid Person person, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "person/crud";
        }

        person.setEncryptedPassword(passwordEncoder.encode(person.getUsername()));

        personRepository.save(person);

        redirectAttributes.addFlashAttribute("message", "Person has been created successfully.");

        return "redirect:/person/add";
    }

    /*
     * Save Edit person
     */
    @PutMapping("/edit/{id}")
    public String saveEditPerson(@Valid Person person, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "person/crud";
        }

        if (person.getPassword() != null && !person.getPassword().isEmpty()) {
            person.setEncryptedPassword(passwordEncoder.encode(person.getPassword()));
        }
        else {
            Person personDb = personRepository.findOne(person.getId());
            person.setEncryptedPassword(personDb.getEncryptedPassword());
        }

        personRepository.save(person);

        redirectAttributes.addFlashAttribute("message", "Person has been updated successfully.");

        return "redirect:/person/edit/" + person.getId();
    }

    /**
     * Delete person 
     */
    @DeleteMapping("/delete/{id}")
    public String deletePerson(@PathVariable Long id) {
        personRepository.delete(id);
        return "redirect:/person";
    }

}
