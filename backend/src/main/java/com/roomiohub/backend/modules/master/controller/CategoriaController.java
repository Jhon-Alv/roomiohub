package com.roomiohub.backend.modules.master.controller;

import com.roomiohub.backend.modules.master.dto.categoria.request.ChangeCategoriaStatusRequest;
import com.roomiohub.backend.modules.master.dto.categoria.request.CreateCategoriaRequest;
import com.roomiohub.backend.modules.master.dto.categoria.request.UpdateCategoriaRequest;
import com.roomiohub.backend.modules.master.dto.categoria.response.CreateCategoriaResponse;
import com.roomiohub.backend.modules.master.dto.categoria.response.FindCategoriaResponse;
import com.roomiohub.backend.modules.master.dto.categoria.response.FindCategoriasResponse;
import com.roomiohub.backend.modules.master.service.categoria.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/master/categorias")
@RequiredArgsConstructor
public class CategoriaController {

    private final CreateCategoriaService createCategoriaService;
    private final FindCategoriasService findCategoriasService;
    private final FindCategoriaService findCategoriaService;
    private final UpdateCategoriaService updateCategoriaService;
    private final ChangeCategoriaStatusService changeCategoriaStatusService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CreateCategoriaResponse> createCategoria(@Valid @RequestBody CreateCategoriaRequest request) {
        CreateCategoriaResponse response = createCategoriaService.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<FindCategoriasResponse>> obtenerCategorias(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "activo", required = false) Boolean activo
    ) {
        List<FindCategoriasResponse> response = findCategoriasService.execute(search, activo);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FindCategoriaResponse> obtenerCategoriaPorId(@PathVariable UUID id) {
        FindCategoriaResponse response = findCategoriaService.execute(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> updateCategoria(@PathVariable UUID id, @Valid @RequestBody UpdateCategoriaRequest request) {
        updateCategoriaService.execute(id, request);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> changeCategoriaStatus(@PathVariable UUID id, @Valid @RequestBody ChangeCategoriaStatusRequest request) {
        changeCategoriaStatusService.execute(id, request);
        return ResponseEntity.noContent().build();
    }
}
