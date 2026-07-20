package com.roomiohub.backend.modules.mercadopago.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PreferenceResponse {

    private String initPoint;
    private String preferenceId;

}
