package com.roomiohub.backend.modules.master.repository;

import com.roomiohub.backend.modules.master.model.TarifaEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TarifaSpecification {

    public static Specification<TarifaEntity> getFiltros(UUID categoriaId, UUID planTiempoId, Boolean activo) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (categoriaId != null) {
                predicates.add(cb.equal(root.get("categoria").get("id"), categoriaId));
            }
            if (planTiempoId != null) {
                predicates.add(cb.equal(root.get("planTiempo").get("id"), planTiempoId));
            }
            if (activo != null) {
                predicates.add(cb.equal(root.get("activo"), activo));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
