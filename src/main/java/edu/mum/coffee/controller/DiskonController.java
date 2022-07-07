package edu.mum.coffee.controller;

import org.springframework.beans.factory.annotation.Autowired;
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

import edu.mum.coffee.domain.Diskon;
import edu.mum.coffee.repository.DiskonRepository;

import java.util.Objects;

import javax.validation.Valid;

@Controller
@RequestMapping("/diskon")
public class DiskonController {

    @Autowired
    DiskonRepository dRepository;

    /**
     * Show View List Diskon 
     */
    @GetMapping
    public String listDiskons(Model model) {
        model.addAttribute("diskon", dRepository.findAll());
        return "diskon/list";
    }
 
    /**
     * Show View Add Diskon
     */
    @GetMapping({"/add"})
    public String showPersonAdd(Model model) {
        model.addAttribute("diskon", new Diskon());
        return "diskon/crud";
    }

    /**
     * Show View Edit Diskon
     */
    @GetMapping("/edit/{id}")
    public String showDiskonEdit(@PathVariable Long id, Model model) {
        model.addAttribute("diskon", dRepository.findById(id));
        return "diskon/crud";
    }

    /**
     * Save Add
     */
    @PostMapping("/add")
    public String saveAddDiskon(@Valid Diskon diskon, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            return "diskon/crud";
        }
        
        Diskon diskonDb = dRepository.findByCode(diskon.getCode());

        // Check Code Is Exist
        if (Objects.isNull(diskonDb) == false) {
            redirectAttributes.addFlashAttribute("messageError", "Code sudah dipakai");
            return "redirect:/diskon/add";
        }

        // Check Persen Must More Than 0
        if (diskon.getPersen() == 0) {
            redirectAttributes.addFlashAttribute("messageError", "Persen tidak boleh 0");
            return "redirect:/diskon/add";
        }

        dRepository.save(diskon);

        redirectAttributes.addFlashAttribute("message", "Diskon has been created successfully.");
        return "redirect:/diskon/add";
    }

    /**
     * Save Edit
     */
    @PutMapping("/edit/{id}")
    public String saveEditDiskon(@Valid Diskon diskon, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        Diskon diskonDb = dRepository.findByCodeExcept(diskon.getCode(), diskon.getId());

        if (result.hasErrors()) {
            return "diskon/crud";
        }

        // Check Code Is Exist
        if (Objects.isNull(diskonDb) == false) {
            redirectAttributes.addFlashAttribute("messageError", "Code sudah dipakai");
            return "redirect:/diskon/edit/" + diskon.getId();
        }

        // Check Persen Must More Than 0
        if (diskon.getPersen() == 0) {
            redirectAttributes.addFlashAttribute("messageError", "Persen tidak boleh 0");
            return "redirect:/diskon/edit/" + diskon.getId();
        }

        dRepository.save(diskon);

        redirectAttributes.addFlashAttribute("message", "Diskon has been updated successfully.");
        return "redirect:/diskon/edit/" + diskon.getId();
    }

    /**
     * Delete Diskon 
     */
    @DeleteMapping("/delete/{id}")
    public String deletePerson(@PathVariable Long id) {
        dRepository.delete(id);
        return "redirect:/diskon";
    }
}
