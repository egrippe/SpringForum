package inet.repository;

import inet.entity.User;
import org.springframework.data.repository.CrudRepository;

/**
 * @author Edward Grippe
 */
public interface UserRepository extends CrudRepository<User, Integer> {

    /**
     * Find a User by name.
     * @param name
     * @return
     */
    User findByName(String name);

}
