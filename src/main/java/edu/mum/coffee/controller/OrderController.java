package edu.mum.coffee.controller;

import edu.mum.coffee.domain.Order;
import edu.mum.coffee.domain.OrderLine;
import edu.mum.coffee.domain.Product;
import edu.mum.coffee.repository.DiskonRepository;
import edu.mum.coffee.repository.ProductRepository;
import edu.mum.coffee.service.OrderService;
import edu.mum.coffee.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    ProductRepository productRepository;
    
    @Autowired
    DiskonRepository diskonRepository;
    
    @Autowired
    OrderService orderService;

    @Autowired
    PersonService personService;

    @ModelAttribute("products")
    public List<Product> populateProducts() {
        return productRepository.findAll();
    }

    @ModelAttribute("diskons")
    public List<Product> populateDiskons() {
        return productRepository.findAll();
    }

    /*
     * Show Form Add Order
     */
    @GetMapping("/add")
    public String showOrderForm(Model model) {
        Order order = new Order();
        order.setOrderDate(new Date());
        order.addOrderLine(new OrderLine());

        model.addAttribute("order", order);

        return "order/add";
    }

    /*
     * Show View List Order
     */
    @GetMapping
    public String listOrders(Model model) {
        model.addAttribute("orders", orderService.findAll());
        return "order/list";
    }

    @PostMapping(value = "/add", params = "addOrderLine")
    public String addOrderLine(Order order, BindingResult result) {
        OrderLine orderLine = new OrderLine();
        order.addOrderLine(orderLine);

        return "order/add";
    }

    @PostMapping(value = "/add", params = "removeOrderLine")
    public String removeOrderLine(Order order, BindingResult result, HttpServletRequest request) {
        int orderLineId = Integer.valueOf(request.getParameter("removeOrderLine"));
        order.getOrderLines().remove(orderLineId);

        return "order/add";
    }

    @PostMapping("/add")
    public String saveOrder(@Valid Order order, BindingResult result, Principal principal, RedirectAttributes redirectAttributes) {
        if (!result.hasErrors()) {
            try {
                List<OrderLine> orderLines = order.getOrderLines();
                int lastOrderLineIndex = orderLines.size() - 1;

                if (orderLines.get(lastOrderLineIndex).getProduct() == null) {
                    orderLines.remove(lastOrderLineIndex);
                }

                if (orderLines.size() == 0) {
                    order.addOrderLine(new OrderLine());
                    throw new Exception("At least one product must be added to the Order");
                }

                for (OrderLine orderLine : orderLines) {
                    if (orderLine.getQuantity() == 0) {
                        throw new Exception("Quantity of product " + orderLine.getProduct().getProductName() + " must be positive");
                    }
                }

                order.setPerson(personService.findByUsername(principal.getName()));
                order.getOrderLines().forEach(orderLine -> {
                    orderLine.setOrder(order);
                });

                orderService.save(order);

                redirectAttributes.addFlashAttribute("message", "Your order has been placed successfully.");

                return "redirect:/orders/add";
            } catch (Exception e) {
                result.reject(null, e.getMessage());
            }
        }

        return "order/add";
    }

    /**
     * Delete Order  
     */
    @DeleteMapping("/delete/{id}")
    public String deleteOrder(@PathVariable Long id) {
        orderService.delete(id);
        return "redirect:/orders";
    }
}
