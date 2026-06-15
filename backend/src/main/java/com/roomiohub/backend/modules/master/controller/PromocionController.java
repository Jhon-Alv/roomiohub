package com.roomiohub.backend.modules.master.controller;

import com.roomiohub.backend.modules.master.dto.promocion.request.ChangePromocionStatusRequest;
import com.roomiohub.backend.modules.master.dto.promocion.request.CreatePromocionRequest;
import com.roomiohub.backend.modules.master.dto.promocion.request.UpdatePromocionRequest;
import com.roomiohub.backend.modules.master.dto.promocion.response.CreatePromocionResponse;
import com.roomiohub.backend.modules.master.dto.promocion.response.FindPromocionResponse;
import com.roomiohub.backend.modules.master.dto.promocion.response.FindPromocionesResponse;
import com.roomiohub.backend.modules.master.service.promocion.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/master/promociones")
@RequiredArgsConstructor
public class PromocionController {

    private final CreatePromocionService createPromocionService;
    private final FindPromocionesService findPromocionesService;
    private final FindPromocionService findPromocionService;
    private final UpdatePromocionService updatePromocionService;
    private final ChangePromocionStatusService changePromocionStatusService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CreatePromocionResponse> createPromocion(@Valid @RequestBody CreatePromocionRequest request) {
        CreatePromocionResponse response = createPromocionService.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<FindPromocionesResponse>> obtenerPromociones(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "activo", required = false) Boolean activo
    ) {
        List<FindPromocionesResponse> response = findPromocionesService.execute(search, activo);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FindPromocionResponse> obtenerPromocionPorId(@PathVariable UUID id) {
        FindPromocionResponse response = findPromocionService.execute(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> updatePromocion(@PathVariable UUID id, @Valid @RequestBody UpdatePromocionRequest request) {
        updatePromocionService.execute(id, request);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> changePromocionStatus(@PathVariable UUID id, @Valid @RequestBody ChangePromocionStatusRequest request) {
        changePromocionStatusService.execute(id, request);
        return ResponseEntity.noContent().build();
    }
}
