package mr.demonid.auth.server.configs;

import mr.demonid.auth.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.web.SecurityFilterChain;

import java.security.PrivateKey;
import java.time.Duration;
import java.util.List;
import java.util.UUID;

import static org.springframework.security.oauth2.core.AuthorizationGrantType.*;
import static org.springframework.security.oauth2.core.ClientAuthenticationMethod.CLIENT_SECRET_BASIC;


/**
 * Конфигурация сервера авторизации
 *
 * http://localhost:8090/.well-known/openid-configuration
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private AuthorizationProperties properties;

    @Autowired
    private UserRepository userRepository;

    /**
     * Включаем в цепочку security свои AuthenticationProvider + UserDetailService + PasswordEncoder
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth, CustomAuthenticationProvider authenticationProvider) throws Exception {
        auth.authenticationProvider(authenticationProvider);
    }

    /**
     * Репозиторий для хранения зарегистрированных клиентов.
     *
     * @param jdbcTemplate объект подключения к БД.
     * @return репозиторий.
     */
    @Bean
    public RegisteredClientRepository registeredClientRepository(JdbcTemplate jdbcTemplate) {
        return new JdbcRegisteredClientRepository(jdbcTemplate);
    }

    /**
     * Регистрируем (добавляем в БД) клиента по умолчанию.
     */
    @Bean
    ApplicationRunner clientRunner(RegisteredClientRepository registeredClientRepository, CustomPasswordEncoder passwordEncoder) {
        return args -> {
            // Создание веб-клиента
            if (registeredClientRepository.findByClientId(properties.getClientId()) == null) {
                registeredClientRepository.save(RegisteredClient
                        .withId(UUID.randomUUID().toString())
                        .clientId(properties.getClientId())
                        .clientSecret(passwordEncoder.encode(properties.getClientSecret()))
                        .authorizationGrantType(AUTHORIZATION_CODE)
                        .authorizationGrantType(CLIENT_CREDENTIALS)
                        .authorizationGrantType(REFRESH_TOKEN)
                        .redirectUris(u -> u.addAll(properties.getClientUrls()))
                        .scope("user.read")
                        .scope("user.write")
                        .scope("openid")
                        .clientAuthenticationMethod(CLIENT_SECRET_BASIC)
                        .clientSettings(ClientSettings.builder()
                                .requireAuthorizationConsent(false)      // запрос пользователя разрешения на "read", "write" и тд.
                                .build())
                        .tokenSettings(TokenSettings.builder()
                                .accessTokenTimeToLive(Duration.ofMinutes(properties.getExpirationTime()))  // время действия токена
                                .build())
                        .build()
                );
            }
            // Создание клиента для программы, работающей без участия пользователя
            if (registeredClientRepository.findByClientId(properties.getApmId()) == null) {
                registeredClientRepository.save(RegisteredClient
                        .withId(UUID.randomUUID().toString())
                        .clientId(properties.getApmId())
                        .clientSecret(passwordEncoder.encode(properties.getApmSecret()))
                        .authorizationGrantType(CLIENT_CREDENTIALS)
                        .scope("read")
                        .scope("write")
                        .clientAuthenticationMethod(CLIENT_SECRET_BASIC)
                        .tokenSettings(TokenSettings.builder()
                                .accessTokenTimeToLive(Duration.ofMinutes(properties.getExpirationTime()))  // время действия токена
                                .build())
                        .build()
                );
            }
        };
    }


    /**
     * Зададим корневой URL нашего сервера.
     */
    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder()
                .issuer(properties.getIssuerUrl())
                .build();
    }

    /**
     * Добавляем в Jwt-токен, свои поля:
     *  - для авторизированных через CLIENT_CREDENTIALS клиентов, роль SERVICE
     *  - для пользователей: имя, id и мыло
     */
    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> tokenCustomizer() {
        return context -> {
            if (context.getAuthorizationGrantType().getValue().equals("client_credentials")) {
                // Добавляем роли в служебный токен
                context.getClaims().claim("authorities", List.of("ROLE_SERVICE"));
            } else { // if (context.getAuthorizationGrantType().getValue().equals("password")) {
                System.out.println("add values to token...");
                // добавляем в токен некоторые данные о пользователе
                String username = context.getPrincipal().getName();
                userRepository.findByUsername(username).ifPresent(user -> {
                    context.getClaims().claim("user_id", user.getId().toString());     // в поле "sub" есть имя пользователя
                    context.getClaims().claim("email", user.getEmail());
                });
            }
        };
    }

}
