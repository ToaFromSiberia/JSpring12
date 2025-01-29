package mr.demonid.gateway.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    /**
     * Настройка потока безопасности (цепочки фильтров). Обрабатывает все входящие запросы.
     * @param http Объект, предоставляющий API для настройки безопасности.
     */
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
                .authorizeExchange(exchange -> exchange
                        .pathMatchers("/auth/**").permitAll()       // разрешаем доступ к эндпоинтам аутентификации
                        .anyExchange().authenticated()                // остальные запросы требуют аутентификации
                )
                .oauth2ResourceServer(oauth2 -> oauth2                // настраиваем обработку OAuth2-токенов, в данном случае Jwt
                        .jwt(jwt -> jwt
                                .jwtAuthenticationConverter(reactiveJwtAuthenticationConverter()) // Настройка преобразователя токенов
                        )
                )
                .csrf(ServerHttpSecurity.CsrfSpec::disable);    // отключаем для всех запросов

        return http.build();
    }

    /**
     * Извлекает из токена список привилегий и возвращает их в виде объекта GrantedAuthority.
     * Если дошли сюда, то токен уже проверен на подлинность через JWK или секретный ключ.
     *
     * После этого метода роли добавляются в SecurityContext и их можно использовать
     * для проверки авторизации, например через аннотации:
     * '@PreAuthorize("hasRole('USER')")'
     * '@GetMapping("/user")' ... или на уровне фильтров доступных адресов.
     */
    private Converter<Jwt, Mono<AbstractAuthenticationToken>> reactiveJwtAuthenticationConverter() {
        JwtAuthenticationConverter delegate = new JwtAuthenticationConverter();

        // Настраиваем преобразование ролей, если нужно
        delegate.setJwtGrantedAuthoritiesConverter(jwt -> {
            List<String> roles = jwt.getClaimAsStringList("roles");
            return roles == null
                    ? List.of()
                    : roles.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        });

        // Адаптируем для реактивного использования
        return new ReactiveJwtAuthenticationConverterAdapter(delegate);
    }

}
