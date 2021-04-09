package uz.pdp.app_jwt_real_email.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.app_jwt_real_email.Entity.Role;
import uz.pdp.app_jwt_real_email.Entity.enums.RoleName;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByRoleName(RoleName roleName);
}
