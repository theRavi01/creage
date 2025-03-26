package com.creage.controller;

import com.creage.model.Subscription;
import com.creage.serviceImpl.SubscriptionServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

    @Autowired
    private SubscriptionServiceImpl subscriptionService;

    @PostMapping("/subscribe/{userId}")
    public Subscription subscribe(@PathVariable Long userId, @RequestBody Subscription subscription) {
        return subscriptionService.subscribeUser(userId, subscription);
    }

    @GetMapping("/user/{userId}")
    public Subscription getUserSubscription(@PathVariable Long userId) {
        return subscriptionService.getUserSubscription(userId);
    }

    @PutMapping("/cancel/{userId}")
    public String cancelSubscription(@PathVariable Long userId) {
        subscriptionService.cancelSubscription(userId);
        return "Subscription cancelled successfully.";
    }
}
