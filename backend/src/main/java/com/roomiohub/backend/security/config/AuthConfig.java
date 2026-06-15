package com.roomiohub.backend.security.config;

import com.roomiohub.backend.security.identity.repository.CuentaRepository;
import com.roomiohub.backend.security.identity.model.CuentaEntity;
import com.roomiohub.backend.security.identity.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class AuthConfig {

    private final UsuarioRepository usuarioRepository;

    @Bean
    public UserDetailsService userDetailsService(CuentaRepository cuentaRepository) {
        return combinedUsername -> {
            String[] parts = combinedUsername.split(":", 2);
            if (parts.length != 2) {
                throw new UsernameNotFoundException("Formato de credenciales inválido. Debe ser RUC:USERNAME");
            }
            String ruc = parts[0];
            String username = parts[1];

            CuentaEntity cuenta = cuentaRepository.findByRuc(ruc)
                    .orElseThrow(() -> new UsernameNotFoundException("Empresa no encontrada"));

            return usuarioRepository.findByUsernameAndTenantIdAndActivoTrue(username, cuenta.getId())
                    .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado o inactivo"));
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(CuentaRepository cuentaRepository){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService(cuentaRepository));
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }


}
