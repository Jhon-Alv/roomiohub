package com.roomiohub.backend.modules.mercadopago.service;

import com.roomiohub.backend.modules.auth.dto.request.RegisterRequest;
import com.roomiohub.backend.modules.auth.dto.response.RegisterResponse;
import com.roomiohub.backend.modules.auth.service.RegisterService;
import com.roomiohub.backend.modules.mercadopago.config.MercadoPagoConfig;
import com.roomiohub.backend.security.identity.repository.CuentaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class HandleWebhookService {

    private final RegisterService registerService;
    private final CuentaRepository cuentaRepository;
    private final MercadoPagoConfig mpConfig;

    @Transactional
    public void execute(String type, String paymentId) {

        log.info("Webhook recibido: type={}, paymentId={}", type, paymentId);

        if (!"payment".equals(type)) {
            log.info("Webhook ignorado: tipo no es payment");
            return;
        }

        PaymentInfo paymentInfo = fetchPaymentInfo(paymentId);

        if (!"approved".equals(paymentInfo.status())) {
            log.info("Pago no aprobado: status={}", paymentInfo.status());
            return;
        }

        Map<String, Object> metadata = paymentInfo.metadata();
        if (metadata == null) {
            log.warn("Webhook sin metadata, ignorando");
            return;
        }

        String ruc = (String) metadata.get("ruc");
        if (cuentaRepository.existsByRuc(ruc)) {
            log.info("Cuenta ya existe para RUC: {}, ignorando", ruc);
            return;
        }

        RegisterRequest registerRequest = RegisterRequest.builder()
                .nombreComercial((String) metadata.get("nombre_comercial"))
                .ruc(ruc)
                .nombre((String) metadata.get("nombre"))
                .username((String) metadata.get("username"))
                .planId((String) metadata.get("plan_id"))
                .build();

        RegisterResponse registerResponse = registerService.execute(registerRequest);

        log.info("Registro completado via webhook: usuario={}", registerResponse.getUsername());
    }

    private PaymentInfo fetchPaymentInfo(String paymentId) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(mpConfig.getAccessToken());

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(
            "https://api.mercadopago.com/v1/payments/" + paymentId,
            HttpMethod.GET,
            entity,
            Map.class
        );

        if (response.getBody() != null) {
            String status = (String) response.getBody().get("status");
            @SuppressWarnings("unchecked")
            Map<String, Object> metadata = (Map<String, Object>) response.getBody().get("metadata");
            return new PaymentInfo(status, metadata);
        }

        return new PaymentInfo("unknown", null);
    }

    private record PaymentInfo(String status, Map<String, Object> metadata) {}

}
