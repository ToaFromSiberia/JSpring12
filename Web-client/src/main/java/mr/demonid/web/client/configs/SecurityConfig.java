package mr.demonid.web.client.configs;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;


@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

    private AnonymousCookieFilter anonymousCookieFilter;
    private CustomAuthenticationSuccessHandler successHandler;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)                      // Отключаем CSRF для запросов API
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/index/**", "/set-category", "/images/**", "/add-to-cart").permitAll()  // Главная и публичные ресурсы
                        .anyRequest().authenticated()  // Остальные требуют аутентификации
                )
                .anonymous(Customizer.withDefaults()) // Включение анонимных пользователей
                .addFilterBefore(anonymousCookieFilter, UsernamePasswordAuthenticationFilter.class)
//                .sessionManagement(session ->
//                        session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS) // Создавать сессии (для анонимов)
//                )
                .oauth2Login(oauth2 -> oauth2
                        .successHandler(successHandler) // подключаем свой фильтр
                );
//                .oauth2Login(Customizer.withDefaults()); // Настройка OAuth2 Login
        return http.build();
    }

}
