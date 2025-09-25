package com.semocompany.subscriptionservice.web.controller;

import com.semocompany.subscriptionservice.service.SubscriptionService;
import com.semocompany.subscriptionservice.web.dto.SubscriptionDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @PostMapping
    public ResponseEntity<SubscriptionDto.Response> createSubscription(
            @Valid @RequestBody SubscriptionDto.CreateRequest request,
            @RequestHeader("X-USER-ID") String userId) {
        SubscriptionDto.Response response = subscriptionService.createSubscription(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<SubscriptionDto.Response>> getUserSubscriptions(
            @RequestHeader("X-USER-ID") String userId) {
        List<SubscriptionDto.Response> responses = subscriptionService.getUserSubscriptions(userId);
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{subscriptionId}")
    public ResponseEntity<SubscriptionDto.Response> updateSubscription(
            @PathVariable Long subscriptionId,
            @Valid @RequestBody SubscriptionDto.UpdateRequest request,
            @RequestHeader("X-USER-ID") String userId) {
        SubscriptionDto.Response response = subscriptionService.updateSubscription(subscriptionId, request, userId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{subscriptionId}")
    public ResponseEntity<Void> deleteSubscription(
            @PathVariable Long subscriptionId,
            @RequestHeader("X-USER-ID") String userId) {
        subscriptionService.deleteSubscription(subscriptionId, userId);
        return ResponseEntity.noContent().build();
    }
}