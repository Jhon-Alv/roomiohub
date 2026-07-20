package com.roomiohub.backend.modules.auth.controller;

import com.roomiohub.backend.modules.auth.dto.response.PlanResponse;
import com.roomiohub.backend.modules.auth.service.FindPlanesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/plans")
@RequiredArgsConstructor
public class PlanController {

    private final FindPlanesService findPlanesService;

    @GetMapping
    public ResponseEntity<List<PlanResponse>> getPlans() {
        List<PlanResponse> planes = findPlanesService.execute();
        return ResponseEntity.ok(planes);
    }

}
