package com.Mini.Mini.controller;

import com.Mini.Mini.Entity.*;
import com.Mini.Mini.Enam.OrderStatus;
import com.Mini.Mini.service.*;
import com.Mini.Mini.service.Impl.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Controller
public class AdminController {

    @Autowired
    CategoryService categoryService;

    @Autowired
    UserService userService;
    @Autowired
    OrdersService ordersService;

    public static String uploadDir = System.getProperty("user.dir") + "/Mini/src/main/resources/static/productImages";

    @GetMapping("/admin")
    public String adminPage(Model model) {
        LocalDateTime now = LocalDateTime.now();
        List<Orders> orderDates = ordersService.getAllOrderDate();
        List<Orders> orderPrice = ordersService.getAllOrderPrice();
//        double oneWeekPrice = 0.0;
        double oneMonthPrice = 0.0;
        double[] dailyPrices = new double[7];
        double[] weeklyPrices = new double[4];

        for (Orders order : orderDates) {
            LocalDateTime orderDate = order.getOrderDate();
            int price = (int) calculateOrderPriceForDay(order, orderPrice);
            long weekIndex = (now.toLocalDate().toEpochDay() - orderDate.toLocalDate().toEpochDay()) / 7;
            if (weekIndex >= 0 && weekIndex < 4) {
                weeklyPrices[Math.toIntExact(weekIndex)] += price;
            }
            if (now.minusMonths(1).isBefore(orderDate)) {
                oneMonthPrice += price;
            }
            long dayIndex = now.toLocalDate().toEpochDay() - orderDate.toLocalDate().toEpochDay();
            if (dayIndex >= 0 && dayIndex < 7) {
                dailyPrices[Math.toIntExact(dayIndex)] += price;
            }
        }
        model.addAttribute("dailyPrices", dailyPrices);
        model.addAttribute("weeklyPrices", weeklyPrices);

        return "adminPage";
    }


    private double calculateOrderPriceForDay(Orders order, List<Orders> orderPrice) {
        double price = 0.0;
        for (Orders orderItem : orderPrice) {
            if (orderItem.equals(order)) {
                price += orderItem.getTotalAmount();
            }
        }
        return price;
    }
    @GetMapping("/admin/AdminYearsChart")
    public String getYearChart(Model model) {
        LocalDateTime now = LocalDateTime.now();
        List<Orders> orderDates = ordersService.getAllOrderDate();
        List<Orders> orderPrice = ordersService.getAllOrderPrice();

        double[] monthlyPrices = new double[12]; 

        for (Orders order : orderDates) {
            LocalDateTime orderDate = order.getOrderDate();
            double price = calculateOrderPriceForDay(order, orderPrice);

            int monthIndex = orderDate.getMonthValue() - 1;
            if (now.minusMonths(12).isBefore(orderDate)) {
                monthlyPrices[monthIndex] += price;
            }
        }
        model.addAttribute("monthlyPrices", monthlyPrices);

        return "AdminYearsChart";

}

    //------------------------------------- Category management-----------------------------------------
    //--------------------------------------------------------------------------------------------------

    @GetMapping("/admin/categories")
    public String getCategory(Model model) {
        model.addAttribute("categories", categoryService.getAllCategory());
        return "Categories";
    }

    @GetMapping("/admin/categories/add")
    public String getCategoryAdd(Model model) {
        model.addAttribute("category", new Category());
        return "categoriesAdd";
    }

    @PostMapping("/admin/categories/add")
    public String postCategoryAdd(@ModelAttribute("category") Category category, BindingResult bindingResult,Model model) {
        if (bindingResult.hasErrors()){
           return "categoriesAdd";
        }
        try {
            category.setDiscountPercentage(0.0);
            categoryService.addCategories(category);
            model.addAttribute("successMessage", "Category added successfully.");
            return "redirect:/admin/categories";

        } catch (DataIntegrityViolationException e) {
            model.addAttribute("categoryError", "Category with this name already exists.");
            return "categoriesAdd";
        }
    }

    @GetMapping("/admin/categories/delete/{id}")
    public String deleteCategories(@PathVariable int id) {
        categoryService.removeCategoryById(id);
        return "redirect:/admin/categories";
    }

    @GetMapping("/admin/categories/update/{id}")
    public String updateCategory(@PathVariable int id, Model model) {
        Optional<Category> category = categoryService.findCategoryById(id);
        if (category.isPresent()) {
            model.addAttribute("category", category.get());
            return "categoriesAdd";
        } else {
            return "404";
        }
    }

    //---------------------------------------- User management----------------------------------------------------
    //------------------------------------------------------------------------------------------------------------

    @GetMapping("/admin/userManagement")
    public String getUser(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("userManagement", users);
        return "userManagement";
    }

    @GetMapping("/admin/userManagement/Active/{id}")
    public String BlockUser(@PathVariable Long id) {
        User user = userService.getUserById(id).orElse(null);
        user.setActive(true);
        userService.saveUser(user);
        return "redirect:/admin/userManagement";

    }

    @GetMapping("/admin/userManagement/unActive/{id}")
    public String unBlockUser(@PathVariable Long id){
        User user = userService.getUserById(id).orElse(null);
        user.setActive(false);
        userService.saveUser(user);
        return "redirect:/admin/userManagement";
    }

    @GetMapping("/admin/userManagement/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.removeUserById(id);
        return "redirect:/admin/userManagement";
    }


    // -------------------------------ORDER MANAGEMENT------------------------------------------------
//----------------------------------------------------------------------------------------------------
    @PostMapping("/admin/updateStatus")
    public String updateOrderStatus(@RequestParam("orderId") Long orderId,
                                    @RequestParam("newStatus") String newStatus) {

        Orders order = ordersService.getOrderById(orderId);
        order.setStatus(OrderStatus.valueOf(newStatus));
        ordersService.saveOrders(order);
        return "redirect:/admin/orders";
    }




    @GetMapping("/admin/orderDetails/{orderId}")
    public String showOrderDetails(@PathVariable Long orderId, Model model) {
        Orders order = ordersService.getOrderById(orderId);
        if (order == null) {
            return "OrderNotFound";
        }
        model.addAttribute("order", order);
        return "OrderDetails";
    }
}
