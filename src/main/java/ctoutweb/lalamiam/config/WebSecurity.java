package ctoutweb.lalamiam.config;

import ctoutweb.lalamiam.security.authentication.UserPrincipalDetailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class WebSecurity {

  private final  UserPrincipalDetailService userPrincipalDetailService;

  public WebSecurity(UserPrincipalDetailService userPrincipalDetailService) {
    this.userPrincipalDetailService = userPrincipalDetailService;
  }

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
      .cors(cors-> cors.configurationSource(corsConfiguration()))
      .csrf(csrf->csrf.disable())
      .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
      .formLogin(formLogin->formLogin.disable());
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
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
  @Bean
  AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
    return http.getSharedObject(AuthenticationManagerBuilder.class)
            .userDetailsService(userPrincipalDetailService)
            .and().build();
  }

}
