package com.proyecto.configuration;

import org.springframework.context.annotation.*;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.*;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import com.proyecto.service.UsuarioDetallesService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration {
  @Bean
  public WebSecurityCustomizer configure() {
    return web -> web.ignoring().requestMatchers("/css/**", "/js/**", "/images/**");
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf().disable()
        .authorizeHttpRequests(a -> a.requestMatchers("/**").authenticated())
        .formLogin(t -> t.loginPage("/login")
            .usernameParameter("email")
            .loginProcessingUrl("/login")
            .defaultSuccessUrl("/")
            .permitAll());

    return http.build();
  }

  @Bean
  public DaoAuthenticationProvider authenticationProvider(UsuarioDetallesService usuarioDetallesService) {
    final DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

    authProvider.setUserDetailsService(usuarioDetallesService);
    authProvider.setPasswordEncoder(encoder());

    return authProvider;
  }

  @Bean
  public PasswordEncoder encoder() {
    return new BCryptPasswordEncoder(11);
  }
}