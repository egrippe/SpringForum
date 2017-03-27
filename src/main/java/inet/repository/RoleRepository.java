package inet.repository;

import inet.entity.Role;
import org.springframework.data.repository.CrudRepository;

/**
 * @author Edward Grippe
 */
public interface RoleRepository extends CrudRepository<Role, Integer> {
    Role findByName(String name);
}
