package com.roomiohub.backend.modules.admin.controller;

import com.roomiohub.backend.modules.admin.dto.sede.request.ChangeSedeStatusRequest;
import com.roomiohub.backend.modules.admin.dto.sede.request.CreateSedeRequest;
import com.roomiohub.backend.modules.admin.dto.sede.request.UpdateSedeRequest;
import com.roomiohub.backend.modules.admin.dto.sede.response.CreateSedeResponse;
import com.roomiohub.backend.modules.admin.dto.sede.response.FindSedeResponse;
import com.roomiohub.backend.modules.admin.service.sede.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/admin/sedes")
@RequiredArgsConstructor
public class SedeController {

    private final FindSedesService findSedesService;
    private final FindSedeService findSedeService;
    private final CreateSedeService createSedeService;
    private final UpdateSedeService updateSedeService;
    private final ChangeSedeStatusService changeSedeStatusService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCION', 'LIMPIEZA')")
    public ResponseEntity<List<FindSedeResponse>> getSedes() {
        List<FindSedeResponse> response = findSedesService.execute();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<FindSedeResponse> getSedeById(@PathVariable UUID id) {
        FindSedeResponse response = findSedeService.execute(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CreateSedeResponse> createSede(@Valid @RequestBody CreateSedeRequest request) {
        CreateSedeResponse response = createSedeService.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> updateSede(@PathVariable UUID id, @Valid @RequestBody UpdateSedeRequest request) {
        updateSedeService.execute(id, request);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> changeSedeStatus(@PathVariable UUID id, @Valid @RequestBody ChangeSedeStatusRequest request) {
        changeSedeStatusService.execute(id, request);
        return ResponseEntity.noContent().build();
    }
}
