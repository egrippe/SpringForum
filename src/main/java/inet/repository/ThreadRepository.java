package inet.repository;

import inet.entity.Thread;
import org.springframework.data.repository.CrudRepository;


public interface ThreadRepository extends CrudRepository<Thread, Integer> {
    Iterable<Thread> findByCategoryId(Integer categoryId);
}
