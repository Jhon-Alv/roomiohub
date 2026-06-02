package com.roomiohub.backend.modules.admin.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChangeSedeStatusRequest {

    @NotNull(message = "El estado es obligatorio")
    private Boolean activo;

}
