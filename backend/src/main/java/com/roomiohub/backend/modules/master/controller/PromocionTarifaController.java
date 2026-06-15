package com.roomiohub.backend.modules.master.controller;

import com.roomiohub.backend.modules.master.dto.promociontarifa.request.BatchCreatePromocionTarifaRequest;
import com.roomiohub.backend.modules.master.dto.promociontarifa.request.UpdatePromocionTarifaPriceRequest;
import com.roomiohub.backend.modules.master.dto.promociontarifa.response.PromocionTarifaResponse;
import com.roomiohub.backend.modules.master.service.promociontarifa.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/master/promociones/{promocionId}/tarifas")
@RequiredArgsConstructor
public class PromocionTarifaController {

    private final BatchCreatePromocionTarifaService batchCreateService;
    private final FindPromocionTarifasService findService;
    private final UpdatePromocionTarifaService updateService;
    private final ChangePromocionTarifaStatusService changeStatusService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PromocionTarifaResponse>> getTarifasByPromocion(@PathVariable UUID promocionId) {
        return ResponseEntity.ok(findService.execute(promocionId));
    }

    @PostMapping("/batch")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> batchCreateTarifas(
            @PathVariable UUID promocionId,
            @Valid @RequestBody BatchCreatePromocionTarifaRequest request
    ) {
        batchCreateService.execute(promocionId, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/{tarifaAsociacionId}/precio")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> updatePrecio(
            @PathVariable UUID promocionId,
            @PathVariable UUID tarifaAsociacionId,
            @Valid @RequestBody UpdatePromocionTarifaPriceRequest request
    ) {
        updateService.execute(promocionId, tarifaAsociacionId, request);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{tarifaAsociacionId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> changeStatus(
            @PathVariable UUID promocionId,
            @PathVariable UUID tarifaAsociacionId,
            @RequestBody Map<String, Boolean> request
    ) {
        if (!request.containsKey("activo")) {
            return ResponseEntity.badRequest().build();
        }
        changeStatusService.execute(promocionId, tarifaAsociacionId, request.get("activo"));
        return ResponseEntity.noContent().build();
    }
}
