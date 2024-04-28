package ctoutweb.lalamiam.config;

import ctoutweb.lalamiam.security.authentication.CustomAuthProvider;
import ctoutweb.lalamiam.security.authentication.JwtAuthenticationFilter;
import ctoutweb.lalamiam.security.authentication.UserPrincipalDetailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class WebSecurity {

  private final  UserPrincipalDetailService userPrincipalDetailService;
  private final CustomAuthProvider customAuthProvider;
  private final JwtAuthenticationFilter jwtAuthenticationFilter;

  public WebSecurity(UserPrincipalDetailService userPrincipalDetailService, CustomAuthProvider customAuthProvider, JwtAuthenticationFilter jwtAuthenticationFilter) {
    this.userPrincipalDetailService = userPrincipalDetailService;
    this.customAuthProvider = customAuthProvider;
    this.jwtAuthenticationFilter = jwtAuthenticationFilter;
  }

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
      .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    http
      .cors(cors-> cors.configurationSource(corsConfiguration()))
      .csrf(AbstractHttpConfigurer::disable)
      .exceptionHandling(exception->exception.authenticationEntryPoint(
              (request, response, authException) -> {
                String message = authException.getMessage();
                response.sendError(response.getStatus(), "Pas bien");
              }
      ))
      .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
      .formLogin(AbstractHttpConfigurer::disable)
      .authorizeHttpRequests(httpRequest->httpRequest
        .requestMatchers("/api/v1/auth/**").permitAll()
        .requestMatchers("/api/v1/pro/**").hasAnyRole("PRO", "EMPLOYE", "ADMIN")
        .requestMatchers("/api/v1/client/**").hasAnyRole("CLIENT", "PRO", "EMPLOYE", "ADMIN")
        .anyRequest().authenticated());
    return http.build();
  }

  @Bean
  CorsConfigurationSource corsConfiguration() {
    CorsConfiguration corsConfiguration = new CorsConfiguration();
    corsConfiguration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
    corsConfiguration.setAllowedMethods(Arrays.asList("POST", "GET", "DELETE", "PUT", "PATCH"));
    corsConfiguration.setAllowedHeaders(Arrays.asList("Content-Type", "Authorization"));
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", corsConfiguration);
    return  source;
  }

  @Bean
  AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
    return http.getSharedObject(AuthenticationManagerBuilder.class)
            .authenticationProvider(customAuthProvider)
            .userDetailsService(userPrincipalDetailService)
            .and().build();
  }

}
