package com.AppRH.AppRH;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;


@Configuration
@EnableWebSecurity
public class WebConfig {

    // Método que configura usuários em memória
    @Bean
    public UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withDefaultPasswordEncoder()
            .username("rafael")
            .password("rafael")
            .roles("USER")
            .build());
        manager.createUser(User.withDefaultPasswordEncoder()
            .username("admin")
            .password("admin")
            .roles("ADMIN")
            .build());
        return manager;
    }

    // Configuração da segurança HTTP
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
            		.requestMatchers("/css/**", "/js/**", "/images/**", "/bootstrap/**").permitAll()
            		.requestMatchers("/").permitAll() // Permitir acesso à página inicial
            		.requestMatchers("/vagas**").permitAll()
            		.requestMatchers("/vaga/**").permitAll()
                .anyRequest().authenticated() // Requerer autenticação para outras requisições
            )
            .formLogin(form -> form
                .permitAll() // Permitir acesso ao formulário de login
            )
            .logout(logout -> logout
                .permitAll() // Permitir logout para todos
            )
            .csrf(csrf -> csrf
                .disable()); // Desativar CSRF (não recomendado para produção)

        return http.build();
    }
}
