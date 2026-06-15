package com.roomiohub.backend.modules.master.controller;

import com.roomiohub.backend.modules.master.dto.plantiempo.request.ChangePlanTiempoStatusRequest;
import com.roomiohub.backend.modules.master.dto.plantiempo.request.CreatePlanTiempoRequest;
import com.roomiohub.backend.modules.master.dto.plantiempo.request.UpdatePlanTiempoRequest;
import com.roomiohub.backend.modules.master.dto.plantiempo.response.CreatePlanTiempoResponse;
import com.roomiohub.backend.modules.master.dto.plantiempo.response.FindPlanTiempoResponse;
import com.roomiohub.backend.modules.master.dto.plantiempo.response.FindPlanesTiempoResponse;
import com.roomiohub.backend.modules.master.service.plantiempo.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/master/planes-tiempo")
@RequiredArgsConstructor
public class PlanTiempoController {

    private final CreatePlanTiempoService createPlanTiempoService;
    private final FindPlanesTiempoService findPlanesTiempoService;
    private final FindPlanTiempoService findPlanTiempoService;
    private final UpdatePlanTiempoService updatePlanTiempoService;
    private final ChangePlanTiempoStatusService changePlanTiempoStatusService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CreatePlanTiempoResponse> createPlanTiempo(@Valid @RequestBody CreatePlanTiempoRequest request) {
        CreatePlanTiempoResponse response = createPlanTiempoService.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<FindPlanesTiempoResponse>> obtenerPlanesTiempo(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "activo", required = false) Boolean activo
    ) {
        List<FindPlanesTiempoResponse> response = findPlanesTiempoService.execute(search, activo);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FindPlanTiempoResponse> obtenerPlanTiempoPorId(@PathVariable UUID id) {
        FindPlanTiempoResponse response = findPlanTiempoService.execute(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> updatePlanTiempo(@PathVariable UUID id, @Valid @RequestBody UpdatePlanTiempoRequest request) {
        updatePlanTiempoService.execute(id, request);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> changePlanTiempoStatus(@PathVariable UUID id, @Valid @RequestBody ChangePlanTiempoStatusRequest request) {
        changePlanTiempoStatusService.execute(id, request);
        return ResponseEntity.noContent().build();
    }
}
