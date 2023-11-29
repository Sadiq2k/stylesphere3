package com.Mini.Mini.controller;

import com.Mini.Mini.Enam.OrderStatus;
import com.Mini.Mini.Entity.Orders;
import com.Mini.Mini.Entity.Rating;
import com.Mini.Mini.Entity.User;
import com.Mini.Mini.Entity.Wallet;
import com.Mini.Mini.repository.OrderRepository;
import com.Mini.Mini.repository.WalletRepository;
import com.Mini.Mini.service.OrdersItemService;
import com.Mini.Mini.service.OrdersService;
import com.Mini.Mini.service.UserService;
import com.Mini.Mini.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.*;

@Controller
public class OrdersController {

    @Autowired
    private OrdersService ordersService;
    @Autowired
    OrdersItemService ordersItemService;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    UserService userService;
    @Autowired
    WalletService walletService;
    @Autowired
    WalletRepository walletRepository;

    @GetMapping("admin/orders")
    public String showAllOrders(Model model) {
        List<Orders> orders = ordersService.getAllOrders();
        Collections.reverse(orders);
            model.addAttribute("orders", orders);
        return "Orders";
    }

    @GetMapping("/UserOrders")
    public String UserOrdersShow(Model model, Principal principal){
        User user = userService.getUserByEmail(principal.getName()).orElse(null);
        List<Orders> orders = ordersService.getOrderByUser(user);
        Collections.reverse(orders);
        model.addAttribute("rating",new Rating());
        model.addAttribute("orders", orders);
        model.addAttribute("username",user);
        return "UserOrders";
    }


    @PostMapping("/UserOrders/updateStatus")//user request to return order
    public String updateOrderStatus(@RequestParam("orderId") Long orderId) {
        Orders order = ordersService.getOrderById(orderId);
        order.setStatusChangeCondition(true);
        ordersService.saveOrders(order);
        return "redirect:/UserOrders";
    }
    @GetMapping("/UserOrder/Cancel/{id}")
    public String CancelOrders(@PathVariable("id")Long id){
        Orders order = ordersService.getOrderById(id);
            order.setStatus(OrderStatus.CANCELLED);
            ordersService.saveOrders(order);
           return "redirect:/UserOrders";
    }
    @GetMapping("/admin/orderReturn/accept/{id}")
    public String ReturnOrder(@PathVariable("id") Long orderId) {
        Orders order = ordersService.getOrderById(orderId);
            order.setStatus(OrderStatus.RETURN);
             Wallet wallet=walletRepository.findWalletByUserId(order.getUser().getId());
             wallet.setWalletAmount(wallet.getWalletAmount()+order.getTotalAmount());
             walletRepository.save(wallet);
             ordersService.saveOrders(order);
        return "redirect:/admin/ReturnOrders";
    }
    @GetMapping("/admin/orderReturn/reject/{id}")
    public String RejectOrder(@PathVariable("id")Long orderId, Model model){
        Orders order = ordersService.getOrderById(orderId);
            order.setStatus(OrderStatus.REJECT);
            ordersService.saveOrders(order);
        return "redirect:/admin/ReturnOrders";
    }
    @GetMapping("/admin/ReturnOrders")
    public String ReturnOrderPage(Model model){
        List<Orders> orders = ordersService.getAllOrders();
        Collections.reverse(orders);
        for (Orders orders1:orders) {
            orders1.getStatus().equals("RETURN");
            model.addAttribute("orders", orders);
        }
        return "ReturnOrders";
    }
}
