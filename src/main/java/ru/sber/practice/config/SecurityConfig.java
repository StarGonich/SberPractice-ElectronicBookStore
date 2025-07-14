package ru.sber.practice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import ru.sber.practice.service.ClientService;
import ru.sber.practice.service.EmployeeService;

import java.util.Collections;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private final ClientService clientService;
    private final EmployeeService employeeService;

    public SecurityConfig(@Lazy ClientService clientService, @Lazy EmployeeService employeeService) {
        this.clientService = clientService;
        this.employeeService = employeeService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider clientAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(clientService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationProvider employeeAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(employeeService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationProvider adminAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(username ->
                new User("admin", passwordEncoder().encode("admin"),
                        Collections.singleton(new SimpleGrantedAuthority("ROLE_ADMIN"))));
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

//    @Bean
//    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
//        AuthenticationManagerBuilder auth = http.getSharedObject(AuthenticationManagerBuilder.class);
//        auth.inMemoryAuthentication()
//                .withUser("admin")
//                .password(passwordEncoder().encode("admin"))
//                .roles("ADMIN");
//        auth.userDetailsService(clientService)
//                .passwordEncoder(passwordEncoder());
//        return auth.build();
//    }

//    @Bean
//    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
//        AuthenticationManagerBuilder auth = http.getSharedObject(AuthenticationManagerBuilder.class);
//
//        // Аутентификация для админа (in-memory)
//        auth.inMemoryAuthentication()
//                .withUser("admin")
//                .password(passwordEncoder().encode("admin"))
//                .roles("ADMIN");
//
//        // Аутентификация для клиентов
//        auth.authenticationProvider(clientAuthenticationProvider());
//
//        // Аутентификация для сотрудников
//        auth.authenticationProvider(employeeAuthenticationProvider());
//
//        return auth.build();
//    }

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        return http
//                .csrf(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/register").permitAll()
//                        .anyRequest().authenticated()
//                )
//                .formLogin(AbstractAuthenticationFilterConfigurer::permitAll)
//                .authenticationManager(authManager(http))
//                .build();
//    }

    @Bean
    @Order(1)
    public SecurityFilterChain adminSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .securityMatcher("/admin/**")
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().hasRole("ADMIN")
                )
                .formLogin(form -> form
                        .loginPage("/admin/login")
                        .defaultSuccessUrl("/", true)
//                        .loginProcessingUrl("/admin/login")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/admin/logout")
                        .logoutSuccessUrl("/admin/login?logout")
                        .permitAll()
                )
                .authenticationProvider(adminAuthenticationProvider());

        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain employeeSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .securityMatcher("/employee/**")
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().hasRole("EMPLOYEE")
                )
                .formLogin(form -> form
                        .loginPage("/employee/login")
                        .defaultSuccessUrl("/", true)
//                        .loginProcessingUrl("/employee/login")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/employee/logout")
                        .logoutSuccessUrl("/employee/login?logout")
                        .permitAll()
                )
                .authenticationProvider(employeeAuthenticationProvider());

        return http.build();
    }

    @Bean
    @Order(3)
    public SecurityFilterChain clientSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/register", "/book", "/images/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/", true)
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )
                .authenticationProvider(clientAuthenticationProvider());

        return http.build();
    }
}
