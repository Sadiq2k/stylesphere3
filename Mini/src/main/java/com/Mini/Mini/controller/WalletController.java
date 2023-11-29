package com.Mini.Mini.controller;

import com.Mini.Mini.Entity.*;
import com.Mini.Mini.repository.WalletRepository;
import com.Mini.Mini.repository.WalletTransactionRepository;
import com.Mini.Mini.service.UserService;
import com.Mini.Mini.service.WalletService;
import com.Mini.Mini.service.WalletTransactionService;
import com.razorpay.Order;
import com.razorpay.Payment;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
@Controller
public class WalletController {

    @Autowired
    UserService userService;
    @Autowired
    WalletService walletService;
    @Autowired
    private WalletRepository walletRepository;
    @Autowired
    WalletTransactionRepository walletTransactionRepository;
    @Autowired
    WalletTransactionService walletTransactionService;

    @GetMapping("/wallet")
    public String getWallet(Model model, Principal principal){
        User user = userService.getUserByEmail(principal.getName()).orElseThrow();
        Wallet wallet=walletRepository.findWalletByUserId(user.getId());
        model.addAttribute("walletAmount", wallet.getWalletAmount());
        model.addAttribute("username",user);
         return"Wallet";

    }

    @PostMapping("/wallet/add")
    public String getWalletAdd(@RequestParam("addAmount") double walletDto, Principal principal){
        User user = userService.getUserByEmail(principal.getName()).orElseThrow();
        Wallet wallet=walletRepository.findWalletByUserId(user.getId());
        double newAmount = wallet.getWalletAmount() + walletDto;
        wallet.setWalletAmount(newAmount);
        walletService.saveWallet(wallet);

        return "redirect:/wallet";
    }

    @PostMapping("/razorpayWallet")
    public String razorpay(@RequestParam("addAmount") double walletAmount,
                           Principal principal,
                           Model model) throws RazorpayException {
        User user = userService.getUserByEmail(principal.getName()).orElse(null);
        RazorpayClient razorpayClient = new RazorpayClient("rzp_test_oCQnrrG0G7HqHN", "4rzdcya1IF8PlisgFnrPVRG4");
        Wallet wallet=walletRepository.findWalletByUserId(user.getId());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("amount", walletAmount * 100);
        jsonObject.put("currency", "INR");
        Order order = razorpayClient.orders.create(jsonObject);
        model.addAttribute("totalAmount", walletAmount * 100);
        model.addAttribute("orderId", order.get("id"));
        return "razorpayWallet";

    }

    @GetMapping("/razorpayWalletAmount/{id}")
    public String razorpayProcess(@PathVariable String id,
                                  @RequestParam("totalAmount") double walletAmount,
                                  RedirectAttributes redirectAttributes,
                                  Model model,
                                  Principal principal) throws RazorpayException {
        try {
        RazorpayClient razorpayClient = new RazorpayClient("rzp_test_oCQnrrG0G7HqHN", "4rzdcya1IF8PlisgFnrPVRG4");
        Payment payment = razorpayClient.payments.fetch(id);

        User user = userService.getUserByEmail(principal.getName()).orElse(null);
        Wallet wallet=walletRepository.findWalletByUserId(user.getId());
            double actual = walletAmount/100.0;

        WalletTransaction walletTransaction= new WalletTransaction();
            LocalDateTime currentDateTime = LocalDateTime.now();
            walletTransaction.setWallet(wallet);
        walletTransaction.setTransaction("Added Amount");
        walletTransaction.setAmount(actual);
        walletTransaction.setTransactionType("RAZORPAY");
        walletTransaction.setDate(String.valueOf(currentDateTime));
        walletTransactionRepository.save(walletTransaction);

         wallet.setWalletAmount(wallet.getWalletAmount() + actual);
         walletRepository.save(wallet);

        model.addAttribute("totalAmount", actual);
        redirectAttributes.addFlashAttribute("success",true);
            return "redirect:/wallet";
        } catch (RazorpayException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "An error occurred");
        }
        return "redirect:/wallet";
    }
    @GetMapping("/walletTransactions")
    public String showWalletTransactions(Model model, Principal principal) {
        User user = userService.getUserByEmail(principal.getName()).orElse(null);
        if (user != null) {
            Wallet wallet1=walletRepository.getWalletByUserId(user.getId());
            if (wallet1==null){
                return "walletTransactionNull";
            }
            List<WalletTransaction> walletTransactions = walletTransactionService.getTransactionsByWallet(wallet1.getId());
            model.addAttribute("walletTransactions", walletTransactions);
            return "walletTransaction";
        }
        return "walletTransaction";
    }

}
