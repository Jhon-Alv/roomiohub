package com.roomiohub.backend.modules.booking.service;

import com.roomiohub.backend.modules.booking.dto.response.FindClienteResponse;
import com.roomiohub.backend.modules.booking.mapper.ClienteMapper;
import com.roomiohub.backend.modules.booking.repository.ClienteRepository;
import com.roomiohub.backend.security.tenant.TenantContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FindClienteService {

    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;

    @Transactional(readOnly = true)
    public FindClienteResponse execute(String numeroDocumento) {
        return clienteRepository.findByNumeroDocumentoAndTenantId(numeroDocumento, TenantContextHolder.getRequiredTenantId())
                .map(clienteMapper::toFindResponse)
                .orElse(FindClienteResponse.builder().encontrado(false).build());
    }
}
