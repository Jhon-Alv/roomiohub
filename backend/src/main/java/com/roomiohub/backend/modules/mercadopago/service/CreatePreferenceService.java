package com.roomiohub.backend.modules.mercadopago.service;

import com.roomiohub.backend.modules.mercadopago.config.MercadoPagoConfig;
import com.roomiohub.backend.modules.mercadopago.dto.request.CreatePreferenceRequest;
import com.roomiohub.backend.modules.mercadopago.dto.response.PreferenceResponse;
import com.roomiohub.backend.security.identity.model.PlanEntity;
import com.roomiohub.backend.security.identity.repository.CuentaRepository;
import com.roomiohub.backend.security.identity.repository.PlanRepository;
import com.roomiohub.backend.shared.exceptions.RestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreatePreferenceService {

    private final PlanRepository planRepository;
    private final CuentaRepository cuentaRepository;
    private final MercadoPagoConfig mpConfig;

    @Value("${landing.base-url}")
    private String landingBaseUrl;

    @Transactional
    public PreferenceResponse execute(CreatePreferenceRequest request) {

        log.info("Creando preferencia de pago para RUC: {}", request.getRuc());

        if (cuentaRepository.existsByRuc(request.getRuc())) {
            throw new RestException(HttpStatus.CONFLICT, "Ya existe una cuenta con el mismo RUC");
        }

        UUID planId = UUID.fromString(request.getPlanId());
        PlanEntity plan = planRepository.findById(planId)
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "Plan no encontrado"));

        String idempotencyKey = "reg-" + request.getRuc();

        Map<String, Object> item = new LinkedHashMap<>();
        item.put("id", plan.getId().toString());
        item.put("title", "Plan " + plan.getNombre() + " - RoomioHub");
        item.put("description", "Suscripcion mensual. Max " + plan.getMaxHabitaciones() + " habitaciones, " + plan.getMaxUsuarios() + " usuarios.");
        item.put("quantity", 1);
        item.put("currency_id", "PEN");
        item.put("unit_price", plan.getPrecioMensual().doubleValue());

        Map<String, Object> backUrls = new LinkedHashMap<>();
        backUrls.put("success", landingBaseUrl + "/exito");
        backUrls.put("failure", landingBaseUrl + "/?status=failure");
        backUrls.put("pending", landingBaseUrl + "/?status=pending");

        Map<String, Object> metadata = new LinkedHashMap<>();
        metadata.put("ruc", request.getRuc());
        metadata.put("plan_id", request.getPlanId());
        metadata.put("nombre_comercial", request.getNombreComercial());
        metadata.put("nombre", request.getNombre());
        metadata.put("username", request.getUsername());

        Map<String, Object> preferenceData = new LinkedHashMap<>();
        preferenceData.put("items", List.of(item));
        preferenceData.put("external_reference", idempotencyKey);
        preferenceData.put("back_urls", backUrls);
        preferenceData.put("auto_return", "approved");
        preferenceData.put("metadata", metadata);

        log.info("Enviando preferencia a MercadoPago. back_urls: {}", backUrls);

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(mpConfig.getAccessToken());
        headers.set("X-Idempotency-Key", idempotencyKey);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(preferenceData, headers);

        ResponseEntity<Map> response = restTemplate.exchange(
            "https://api.mercadopago.com/checkout/preferences",
            HttpMethod.POST,
            entity,
            Map.class
        );

        if (response.getStatusCode() == HttpStatus.CREATED && response.getBody() != null) {
            String initPoint = (String) response.getBody().get("init_point");
            String preferenceId = (String) response.getBody().get("id");

            log.info("Preferencia creada: {} para RUC: {}", preferenceId, request.getRuc());

            return PreferenceResponse.builder()
                    .initPoint(initPoint)
                    .preferenceId(preferenceId)
                    .build();
        }

        throw new RestException(HttpStatus.INTERNAL_SERVER_ERROR, "No se pudo crear la preferencia de pago");
    }

}
