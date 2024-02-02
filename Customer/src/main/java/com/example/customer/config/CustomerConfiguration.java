package com.example.customer.config;

import com.example.library.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AndRequestMatcher;

@Configuration
@EnableWebSecurity
public class CustomerConfiguration {

    private final CustomerRepository customerRepository;



    private final CustomLogoutHandler customLogoutHandler;

    private final AuthFilter authFilter;


    public CustomerConfiguration(CustomerRepository customerRepository,  CustomLogoutHandler customLogoutHandler, AuthFilter authFilter) {
        this.customerRepository = customerRepository;
        this.customLogoutHandler = customLogoutHandler;
        this.authFilter = authFilter;
    }
    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomerServiceConfig(customerRepository);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.addFilterBefore(authFilter, BasicAuthenticationFilter.class)
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests((authorize) ->
                        authorize.requestMatchers("/css/**", "/imgs/**", "/login/**", "/js/**", "/fonts/**", "/sass/**", "/register", "/do-register", "/home","/forgot-password/**", "/my-account?**","/passwordResetPage", "/page-account**","/enter-username**", "/otpScreenEmail**", "/otpScreen**").permitAll()
                                .requestMatchers("/customer/**").hasAuthority("CUSTOMER")
                                .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                        .invalidSessionUrl("/login")
                        .maximumSessions(1)
                        .maxSessionsPreventsLogin(false))
                .formLogin(
                        form -> form
                                .loginPage("/login")
                                .loginProcessingUrl("/do-login")
                                .defaultSuccessUrl("/index")
                                .failureUrl("/login?error")
                                .permitAll()
                );
//                .logout(
//                        logout -> logout
//                                .logoutRequestMatcher(new AndRequestMatcher("/logout"))
//                                .invalidateHttpSession(true)
//                                .logoutSuccessUrl("/login?logout")
//                                .permitAll());
        return http.build();
    }
}
