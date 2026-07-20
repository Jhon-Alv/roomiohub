package com.roomiohub.backend.modules.mercadopago.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "mercadopago")
public class MercadoPagoConfig {

    private String accessToken;
    private String publicKey;
    private String webhookSecret;

}
