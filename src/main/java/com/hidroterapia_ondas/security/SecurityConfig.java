package com.hidroterapia_ondas.security;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.List;

import org.springframework.http.HttpMethod;


import com.hidroterapia_ondas.service.UserDetailsServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtFilter;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;


    @Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        // 🔒 Desactivamos CSRF (requerido para APIs y H2)
        .csrf(csrf -> csrf.disable())

        // ✅ habilita CORS globalmente
        .cors(cors -> {})

            // 👁️ Permitir el uso de frames (necesario para H2 Console)
        .headers(headers -> headers.frameOptions(frame -> frame.disable()))

        .authorizeHttpRequests(auth -> auth
            // ✅ Endpoints públicos (sin token)
            .requestMatchers("/api/auth/**", "/api/products", "/h2-console/**").permitAll()

            // 👉 Hacer público el catálogo de servicios
            .requestMatchers(HttpMethod.GET, "/api/service/all").permitAll()

                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()  // <— permitir preflight global

            // 🔐 Endpoints solo para ADMIN
            .requestMatchers("/api/admin/**", "/api/orders/**").hasAnyAuthority("ROLE_ADMIN", "ADMIN")


            // 🔒 Cualquier otra ruta necesita estar autenticada
            .anyRequest().authenticated()
        )

        // 🚫 Desactivar autenticación por formulario y HTTP Basic
        .formLogin(form -> form.disable())
        .httpBasic(basic -> basic.disable())
        .logout(logout -> logout.disable())

        // ⚙️ No usamos sesiones (JWT es stateless)
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

        // 🧩 Manejadores de errores personalizados
        .exceptionHandling(ex -> ex
            .authenticationEntryPoint((request, response, authException) -> {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\": \"Unauthorized\", \"message\": \"Token inválido o ausente\"}");
            })
            .accessDeniedHandler((request, response, accessDeniedException) -> {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\": \"Forbidden\", \"message\": \"Acceso denegado\"}");
            })
        );

    // 🧱 Aplicamos el filtro JWT antes del filtro estándar de autenticación
    http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

    // ⚙️ Añadimos el AuthenticationProvider configurado
    http.authenticationProvider(authenticationProvider());

    return http.build();
}

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public org.springframework.security.authentication.AuthenticationProvider authenticationProvider() {
        var provider = new org.springframework.security.authentication.dao.DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cfg = new CorsConfiguration();
        cfg.setAllowedOriginPatterns(List.of(
                "http://localhost:*",                 // pruebas locales (Live Server/IDE)
                "http://127.0.0.1:*",
                "https://marshallgomez1103.github.io" // tu frontend en GH Pages
        ));
        cfg.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));
        cfg.setAllowedHeaders(List.of("Content-Type","Authorization"));
        cfg.setExposedHeaders(List.of("Authorization"));
        cfg.setAllowCredentials(true);
        cfg.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cfg);
        return source;
    }




}