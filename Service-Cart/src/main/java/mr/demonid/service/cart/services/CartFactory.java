package mr.demonid.service.cart.services;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import mr.demonid.service.cart.dto.CartItem;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * Фабрика корзин.
 * Возвращает определенный тип корзины, в зависимости от
 * текущей стратегии (пользователя).
 */
@Service
@AllArgsConstructor
public class CartFactory {

    private final ObjectProvider<AnonCart> anonCartProvider;
    private final ObjectProvider<AuthCart> authCartProvider;
    private final EmptyCart emptyCart;
    /**
     * Возвращает корзину, в зависимости от типа активного пользователя.
     */
    public Cart getCart(HttpServletRequest request) {
        if (isAnonymous()) {
            String id = getAnonymousId(request);
            if (id != null) {
                // создаем новый экземпляр корзины
                AnonCart anonCart = anonCartProvider.getObject();
                anonCart.setUserId(id);
                return anonCart;
            }
            return emptyCart;   // кто-то без идентификатора
        }
        String id = getUserId();
        if (id != null) {
            // создаем новый экземпляр корзины для аутентифицированного пользователя
            AuthCart authCart = authCartProvider.getObject();
            authCart.setUserId(id);
            return authCart;
        }
        return emptyCart;   // кто-то без идентификатора
    }

    /**
     * Корректирует данные авторизировавшегося пользователя, перенося его
     * товары с анонимной корзины в БД.
     * @param anonId Идентификатор до авторизации
     */
    public void registerUser(String anonId, String userId) {
        // инициализируем объекты
        AnonCart anonCart = anonCartProvider.getObject();
        anonCart.setUserId(anonId);
        AuthCart authCart = authCartProvider.getObject();
        authCart.setUserId(userId);

        List<CartItem> source = anonCart.getItems();
        anonCart.clearCart();
        // отправляем в корзину авторизированного пользователя, т.е. в БД
        if (source != null && !source.isEmpty()) {
            source.forEach(e -> authCart.addItem(e.getProductId(), e.getQuantity()));
        }
    }


    /**
     * Проверяет, является ли текущий пользователь анонимом.
     */
    private static boolean isAnonymous() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication == null || authentication instanceof AnonymousAuthenticationToken;
    }

    /**
     * Возвращает ID пользователя из поля "user_id" Jwt-Токена.
     */
    private static String getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        return jwt.getClaim("user_id");
    }

    /**
     * Возвращает ID пользователя из поля "anon_id" куков
     */
    private static String getAnonymousId(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            return Arrays.stream(cookies)
                    .filter(cookie -> "ANON_ID".equals(cookie.getName()))
                    .map(Cookie::getValue)
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }
}
