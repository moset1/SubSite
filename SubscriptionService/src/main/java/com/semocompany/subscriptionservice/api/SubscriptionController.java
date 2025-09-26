package com.semocompany.subscriptionservice.api;

import com.semocompany.subscriptionservice.domain.subscription.dto.SubscriptionDTO;
import com.semocompany.subscriptionservice.domain.subscription.service.SubscriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @PostMapping
    public ResponseEntity<SubscriptionDTO.Response> createSubscription(
            @Valid @RequestBody SubscriptionDTO.CreateRequest request,
            @RequestHeader("X-USER-ID") UUID userId) {

        SubscriptionDTO.Response response = subscriptionService.createSubscription(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<SubscriptionDTO.Response>> getUserSubscriptions(
            @RequestHeader("X-USER-ID") UUID userId) {
        List<SubscriptionDTO.Response> responses = subscriptionService.getUserSubscriptions(userId);
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{subscriptionId}")
    public ResponseEntity<SubscriptionDTO.Response> updateSubscription(
            @PathVariable UUID subscriptionId,
            @Valid @RequestBody SubscriptionDTO.UpdateRequest request,
            @RequestHeader("X-USER-ID") UUID userId) {
        SubscriptionDTO.Response response = subscriptionService.updateSubscription(subscriptionId, request, userId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{subscriptionId}")
    public ResponseEntity<Void> deleteSubscription(
            @PathVariable UUID subscriptionId,
            @RequestHeader("X-USER-ID") UUID userId) {
        subscriptionService.deleteSubscription(subscriptionId, userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/check-updates")
    public ResponseEntity<Void> checkUpdates(
            @RequestHeader("X-USER-ID") UUID userId) {
        subscriptionService.checkUpdatesForUser(userId);
        return ResponseEntity.accepted().build();
    }
}
