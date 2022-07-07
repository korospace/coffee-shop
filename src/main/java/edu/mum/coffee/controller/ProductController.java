package edu.mum.coffee.controller;

import edu.mum.coffee.domain.Product;
import edu.mum.coffee.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/products")
public class ProductController {
    
    @Autowired  
    ProductRepository pRepository;

    /*
     * Show View Add Product
     */
    @GetMapping("/add")
    public String showProductAdd(Model model) {
        model.addAttribute("product", new Product());
        return "product/crud";
    }

    /*
     * Show View Edit Product
     */
    @GetMapping("/edit/{id}")
    public String showProductEdit(@PathVariable Long id, Model model) {
        model.addAttribute("product", pRepository.findById(id));
        return "product/crud";
    }

    /*
     * Save Add Product
     */
    @PostMapping("/add")
    public String saveAddProduct(@Valid Product product, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "product/crud";
        }

        pRepository.save(product);

        redirectAttributes.addFlashAttribute("message", "Your product has been created successfully.");

        return "redirect:/products/add";
    }

    /*
     * Save Edit Product
     */
    @PutMapping("/edit/{id}")
    public String saveEditProduct(@Valid Product product, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "product/crud";
        }

        pRepository.save(product);

        redirectAttributes.addFlashAttribute("message", "Your product has been updated successfully.");
        return "redirect:/products/edit/" + product.getId();
    }

    /*
     * Delete Product
     */
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        pRepository.delete(id);
        return "redirect:/";
    }
}
