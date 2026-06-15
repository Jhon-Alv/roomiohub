package com.roomiohub.backend.modules.master.controller;

import com.roomiohub.backend.modules.master.dto.catalog.OptionDto;
import com.roomiohub.backend.modules.master.service.catalog.CategoriaCatalogService;
import com.roomiohub.backend.modules.master.service.catalog.PisoCatalogService;
import com.roomiohub.backend.modules.master.service.catalog.PlanesTiempoCatalogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/master/catalog")
@RequiredArgsConstructor
public class CatalogController {

    private final CategoriaCatalogService categoriaCatalogService;
    private final PisoCatalogService pisoCatalogService;
    private final PlanesTiempoCatalogService planesTiempoCatalogService;

    @GetMapping("/categorias")
    public ResponseEntity<List<OptionDto>> getCategoriasCatalog() {
        return ResponseEntity.ok(categoriaCatalogService.execute());
    }

    @GetMapping("/pisos")
    public ResponseEntity<List<OptionDto>> getPisosCatalog(
            @RequestParam(required = false) UUID sedeId
    ) {
        return ResponseEntity.ok(pisoCatalogService.execute(sedeId));
    }

    @GetMapping("/planes-tiempo")
    public ResponseEntity<List<OptionDto>> getPlanesTiempoCatalog() {
        return ResponseEntity.ok(planesTiempoCatalogService.execute());
    }
}
