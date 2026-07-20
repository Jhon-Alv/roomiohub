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
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

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

        Map<String, Object> preferenceData = Map.of(
            "items", List.of(
                Map.of(
                    "id", plan.getId().toString(),
                    "title", "Plan " + plan.getNombre() + " - RoomioHub",
                    "description", "Suscripcion mensual. Max " + plan.getMaxHabitaciones() + " habitaciones, " + plan.getMaxUsuarios() + " usuarios.",
                    "quantity", 1,
                    "currency_id", "PEN",
                    "unit_price", plan.getPrecioMensual().doubleValue()
                )
            ),
            "external_reference", idempotencyKey,
            "back_urls", Map.of(
                "success", "http://18.221.126.33/exito",
                "failure", "http://18.221.126.33/?status=failure",
                "pending", "http://18.221.126.33/?status=pending"
            ),
            "auto_return", "approved",
            "metadata", Map.of(
                "ruc", request.getRuc(),
                "plan_id", request.getPlanId(),
                "nombre_comercial", request.getNombreComercial(),
                "nombre", request.getNombre(),
                "username", request.getUsername()
            )
        );

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
