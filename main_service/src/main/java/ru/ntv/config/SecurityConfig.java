package ru.ntv.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import ru.ntv.etc.DatabasePrivilege;
import ru.ntv.security.JwtAuthenticationFilter;
import ru.ntv.security.JwtAuthenticationPoint;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableTransactionManagement
public class SecurityConfig{
    
    @Autowired
    private JwtAuthenticationPoint unauthorizedHandler;
    
    @Autowired
    JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("localhost"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .cors()
                .and()
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling()
                .authenticationEntryPoint(unauthorizedHandler)  //если пользователь не зарегестрирован,то он обрабатывается тут
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests((auth) -> auth
                        .antMatchers("/auth/**").permitAll()
                        
                        .antMatchers(HttpMethod.GET, "/articles/**").permitAll()
                        .antMatchers(HttpMethod.POST, "/articles/**").hasAuthority(DatabasePrivilege.CAN_POST_ARTICLES.name())
                        .antMatchers(HttpMethod.PUT, "/articles/**").hasAuthority(DatabasePrivilege.CAN_PUT_ARTICLES.name())
                        .antMatchers(HttpMethod.DELETE, "/articles/**").hasAuthority(DatabasePrivilege.CAN_DELETE_ARTICLES.name())
                        
                        .antMatchers(HttpMethod.GET, "/themes/**").permitAll()
                        .antMatchers(HttpMethod.POST, "/themes/sub").hasAuthority(DatabasePrivilege.CAN_GET_THEMES.name())
                        .antMatchers(HttpMethod.POST, "/themes/**").hasAuthority(DatabasePrivilege.CAN_POST_THEMES.name())
                        .antMatchers(HttpMethod.DELETE, "/themes/**").hasAuthority(DatabasePrivilege.CAN_DELETE_THEMES.name())

                        .antMatchers(HttpMethod.GET, "/journalists/**").hasAuthority(DatabasePrivilege.CAN_GET_JOURNALISTS.name())
                        .antMatchers(HttpMethod.POST, "/journalists/**").hasAuthority(DatabasePrivilege.CAN_POST_JOURNALISTS.name())
                        .antMatchers(HttpMethod.DELETE, "/journalists/**").hasAuthority(DatabasePrivilege.CAN_DELETE_JOURNALISTS.name())
                        
                        .antMatchers("*").denyAll()
                )
                .build();
    }
}