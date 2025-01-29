package mr.demonid.service.cart.repositories;

import mr.demonid.service.cart.domain.CartItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<CartItemEntity, Long> {

    CartItemEntity findByUserIdAndProductId(String userId, String productId);

    void deleteByUserIdAndProductId(String userId, String productId);

    List<CartItemEntity> findByUserId(String userId);

    void deleteByUserId(String userId);

    void deleteAllByUserId(String userId);

}
