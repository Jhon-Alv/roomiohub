package com.roomiohub.backend.modules.master.controller;

import com.roomiohub.backend.modules.master.dto.tarifa.request.ChangeTarifaStatusRequest;
import com.roomiohub.backend.modules.master.dto.tarifa.request.CreateTarifaRequest;
import com.roomiohub.backend.modules.master.dto.tarifa.request.UpdateTarifaRequest;
import com.roomiohub.backend.modules.master.dto.tarifa.response.CreateTarifaResponse;
import com.roomiohub.backend.modules.master.dto.tarifa.response.FindTarifaResponse;
import com.roomiohub.backend.modules.master.dto.tarifa.response.FindTarifasResponse;
import com.roomiohub.backend.modules.master.service.tarifa.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/master/tarifas")
@RequiredArgsConstructor
public class TarifaController {

    private final CreateTarifaService createTarifaService;
    private final FindTarifasService findTarifasService;
    private final FindTarifaService findTarifaService;
    private final UpdateTarifaService updateTarifaService;
    private final ChangeTarifaStatusService changeTarifaStatusService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CreateTarifaResponse> createTarifa(@Valid @RequestBody CreateTarifaRequest request) {
        CreateTarifaResponse response = createTarifaService.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<FindTarifasResponse>> obtenerTarifas(
            @RequestParam(value = "categoriaId", required = false) UUID categoriaId,
            @RequestParam(value = "planTiempoId", required = false) UUID planTiempoId,
            @RequestParam(value = "activo", required = false) Boolean activo
    ) {
        List<FindTarifasResponse> response = findTarifasService.execute(categoriaId, planTiempoId, activo);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FindTarifaResponse> obtenerTarifaPorId(@PathVariable UUID id) {
        FindTarifaResponse response = findTarifaService.execute(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> updateTarifa(@PathVariable UUID id, @Valid @RequestBody UpdateTarifaRequest request) {
        updateTarifaService.execute(id, request);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> changeTarifaStatus(@PathVariable UUID id, @Valid @RequestBody ChangeTarifaStatusRequest request) {
        changeTarifaStatusService.execute(id, request);
        return ResponseEntity.noContent().build();
    }
}
