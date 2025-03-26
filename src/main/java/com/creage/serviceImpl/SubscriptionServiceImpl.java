package com.creage.serviceImpl;

import com.creage.model.Subscription;
import com.creage.model.Users;
import com.creage.repository.SubscriptionRepository;
import com.creage.repository.UsersRepository;
import com.creage.service.SubscriptionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class SubscriptionServiceImpl implements SubscriptionService{

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Override
    public Subscription subscribeUser(Long userId, Subscription subscription) {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        subscription.setUser(user);
        subscription.setSubscriptionStartDate(LocalDate.now());
        subscription.setSubscriptionEndDate(LocalDate.now().plusMonths(1)); // Example: 1-month validity
        subscription.setValid(true);

        return subscriptionRepository.save(subscription);
    }

    public Subscription getUserSubscription(Long userId) {
        return subscriptionRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Subscription not found"));
    }

    public void cancelSubscription(Long userId) {
        Subscription subscription = getUserSubscription(userId);
        subscription.setValid(false);
        subscriptionRepository.save(subscription);
    }
}

