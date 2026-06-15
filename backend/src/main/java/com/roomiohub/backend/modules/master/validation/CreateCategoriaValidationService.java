package com.roomiohub.backend.modules.master.validation;

import com.roomiohub.backend.modules.master.model.CategoriaEntity;
import com.roomiohub.backend.modules.master.repository.CategoriaRepository;
import com.roomiohub.backend.shared.exceptions.RestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class CreateCategoriaValidationService {

    private final CategoriaRepository categoriaRepository;

    public void validate(CategoriaEntity categoria) {

        if (categoriaRepository.existsByNombreAndTenantId(categoria.getNombre(), categoria.getTenantId())) {
            throw new RestException(HttpStatus.CONFLICT, "Ya existe una categoría con el nombre especificado");
        }

    }

}
