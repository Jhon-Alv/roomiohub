package com.roomiohub.backend.modules.master.controller;

import com.roomiohub.backend.modules.master.dto.piso.request.ChangePisoStatusRequest;
import com.roomiohub.backend.modules.master.dto.piso.request.CreatePisoRequest;
import com.roomiohub.backend.modules.master.dto.piso.request.UpdatePisoRequest;
import com.roomiohub.backend.modules.master.dto.piso.response.CreatePisoResponse;
import com.roomiohub.backend.modules.master.dto.piso.response.FindPisoResponse;
import com.roomiohub.backend.modules.master.dto.piso.response.FindPisosResponse;
import com.roomiohub.backend.modules.master.service.piso.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/master/pisos")
@RequiredArgsConstructor
public class PisoController {

    private final CreatePisoService createPisoService;
    private final FindPisosService findPisosService;
    private final FindPisoService findPisoService;
    private final UpdatePisoService updatePisoService;
    private final ChangePisoStatusService changePisoStatusService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CreatePisoResponse> createPiso(@Valid @RequestBody CreatePisoRequest request) {
        CreatePisoResponse response = createPisoService.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<FindPisosResponse>> obtenerPisos(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "activo", required = false) Boolean activo,
            @RequestParam(value = "sedeId") UUID sedeId
    ) {
        List<FindPisosResponse> response = findPisosService.execute(search, activo, sedeId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FindPisoResponse> obtenerPisoPorId(@PathVariable UUID id) {
        FindPisoResponse response = findPisoService.execute(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> updatePiso(@PathVariable UUID id, @Valid @RequestBody UpdatePisoRequest request) {
        updatePisoService.execute(id, request);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> changePisoStatus(@PathVariable UUID id, @Valid @RequestBody ChangePisoStatusRequest request) {
        changePisoStatusService.execute(id, request);
        return ResponseEntity.noContent().build();
    }
}
