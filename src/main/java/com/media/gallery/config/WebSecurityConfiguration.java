package com.media.gallery.config;

import com.media.gallery.constant.EndpointConstants;
import com.media.gallery.filter.JwtFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .cors(withDefaults())
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sess ->
                        sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(request ->
                        request
                                .requestMatchers("/web/api/v1/login",
                                        "/web/api/v1/user/register",
                                        "/web/api/v1/refreshtoken",
                                        "/web/api/v1/public/**",
                                        "/web/api/v1/admin/test",
                                        "/uploads/users/**",
                                        "/web/api/v1/admin/public/media/**").permitAll()
                                .anyRequest().authenticated())
                .exceptionHandling(eh -> eh
                        .authenticationEntryPoint(
                                (req, res, ex) -> {
                                    res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                                    res.setContentType(MediaType.APPLICATION_JSON_VALUE);

                                    String body = """
                                            {
                                              "status": 401,
                                              "error": "Unauthorized",
                                              "message": "%s",
                                              "path": "%s"
                                            }
                                            """.formatted(ex.getMessage(), req.getRequestURI());
                                    res.getWriter().write(body);
                                }
                        ))
                .authenticationProvider(authenticationProvider()) // CUSTOM AUTH PROVIDER
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

    //    @Bean
    // @Bean is commented as we are using `CustomUserDetails` and `CustomUserDetailsService`. All the beans will now be
    // injected of those entities.
    UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails userOne = User.withUsername("a@gmail.com")
                .password(passwordEncoder.encode("12345"))
                .roles("ADMIN", "USER")
                .build();
        UserDetails userTwo = User.withUsername("b@gmail.com")
                .password(passwordEncoder.encode("12345"))
                .roles("ADMIN", "USER")
                .build();
        return new InMemoryUserDetailsManager(userOne, userTwo);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
            throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:3000"));
        config.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
