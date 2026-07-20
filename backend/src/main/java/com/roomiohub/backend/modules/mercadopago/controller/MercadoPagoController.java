package com.roomiohub.backend.modules.mercadopago.controller;

import com.roomiohub.backend.modules.mercadopago.dto.request.CreatePreferenceRequest;
import com.roomiohub.backend.modules.mercadopago.dto.response.PreferenceResponse;
import com.roomiohub.backend.modules.mercadopago.service.CreatePreferenceService;
import com.roomiohub.backend.modules.mercadopago.service.HandleWebhookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/mercadopago")
@RequiredArgsConstructor
public class MercadoPagoController {

    private final CreatePreferenceService createPreferenceService;
    private final HandleWebhookService handleWebhookService;

    @PostMapping("/create-preference")
    public ResponseEntity<PreferenceResponse> createPreference(
            @Valid @RequestBody CreatePreferenceRequest request) {
        PreferenceResponse response = createPreferenceService.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/webhook")
    public ResponseEntity<Void> handleWebhook(@RequestBody Map<String, Object> body) {
        String type = (String) body.getOrDefault("type", "");
        @SuppressWarnings("unchecked")
        Map<String, Object> data = (Map<String, Object>) body.getOrDefault("data", Map.of());
        String paymentId = (String) data.getOrDefault("id", "");

        if (!type.isEmpty() && !paymentId.isEmpty()) {
            handleWebhookService.execute(type, paymentId);
        }

        return ResponseEntity.ok().build();
    }

}
