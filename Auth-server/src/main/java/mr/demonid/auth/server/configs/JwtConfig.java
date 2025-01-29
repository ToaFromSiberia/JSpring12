package mr.demonid.auth.server.configs;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;

/**
 * Устанавливаем ключ для подписи Jwt-токенов.
 */

@Configuration
public class JwtConfig {

//    /**
//     * Создаем бин кодирования (подписания) Jwt-токена. Для чего используем
//     * приватный ключ из файла.
//     */
//    @Bean
//    public JwtEncoder jwtEncoder() throws Exception {
//        // загрузка приватного ключа из PEM-файла
//        RSAPrivateKey privateKey = loadPrivateKey("private_key.pem");
//        RSAPublicKey rsaPublicKey = generatePublicKeyFromPrivate(privateKey);  // генерируем публичный ключ из приватного
//
//        System.out.println("Public key: " + Base64.getEncoder().encodeToString(rsaPublicKey.getEncoded()));
//
//        RSAKey rsaKey = new RSAKey.Builder(rsaPublicKey)                       // создаем RSAKey
//                .keyID(UUID.randomUUID().toString()) // Уникальный идентификатор ключа
//                .privateKey(privateKey)
//                .build();
//        // оборачиваем в JWKSet
//        ImmutableJWKSet<SecurityContext> jwkSet = new ImmutableJWKSet<>(new JWKSet(rsaKey));
//        return new NimbusJwtEncoder(jwkSet);
//    }
//
//    /**
//     * Загрузка приватного ключа из PEM-файла.
//     * @param filename Имя файла, находящегося в папке ресурсов.
//     */
//    private RSAPrivateKey loadPrivateKey(String filename) throws Exception {
//        ClassPathResource resource = new ClassPathResource(filename);
//        String key = new String(resource.getInputStream().readAllBytes());
//        String privateKeyPEM = key                                          // убираем все лишнее
//                .replace("-----BEGIN PRIVATE KEY-----", "")
//                .replace("-----END PRIVATE KEY-----", "")
//                .replaceAll("\\s", "");
//        // на оставшихся данных создаем объект RSAPrivateKey
//        byte[] keyBytes = Base64.getDecoder().decode(privateKeyPEM);
//        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
//        KeyFactory kf = KeyFactory.getInstance("RSA");
//        return (RSAPrivateKey) kf.generatePrivate(spec);
//    }
//
//    //    @Bean
////    public JWKSet jwkSet() throws Exception {
////        RSAPrivateKey privateKey = loadPrivateKey("private_key.pem");
////        RSAPublicKey rsaPublicKey = generatePublicKeyFromPrivate(privateKey);
////
////        RSAKey rsaKey = new RSAKey.Builder(rsaPublicKey)
////                .keyID(UUID.randomUUID().toString()) // Уникальный идентификатор ключа
////                .build();
////
////        return new JWKSet(rsaKey);
////    }
//
//
//    /**
//     * Генерация публичного ключа из приватного.
//     */
//    public RSAPublicKey generatePublicKeyFromPrivate(RSAPrivateKey privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
//        BigInteger modulus = privateKey.getModulus();
//        BigInteger publicExponent = BigInteger.valueOf(65537);      // общий публичный экспонент для RSA
//        // собственно создание публичного ключа
//        RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(modulus, publicExponent);
//        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//        return (RSAPublicKey) keyFactory.generatePublic(publicKeySpec);
//    }
//
//    /**
//     * Загрузка публичного ключа из файла.
//     * Файл должен быть в папке ресурсов.
//     */
//    private RSAPublicKey loadPublicKeyFromClassPath(String fileName) throws Exception {
//        ClassPathResource resource = new ClassPathResource(fileName);
//        byte[] keyBytes = Files.readAllBytes(resource.getFile().toPath());
//        // обрезаем лишнее и декодируем из Base64
//        byte[] decodedKey = Base64.getDecoder().decode(new String(keyBytes)
//                .replaceAll("-----\\w+ PUBLIC KEY-----", "")
//                .replaceAll("\\s+", ""));
//
//        // собственно генерируем публичный ключ
//        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedKey);
//
//        return (RSAPublicKey) keyFactory.generatePublic(keySpec);
//    }
//
//


}
