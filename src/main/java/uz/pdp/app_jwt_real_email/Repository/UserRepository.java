package uz.pdp.app_jwt_real_email.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.app_jwt_real_email.Entity.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByEmail(String email);


    Optional<User> findByEmailAndEmailCode(String email, String emailCode);
}
