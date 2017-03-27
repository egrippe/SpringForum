package inet.repository;

import inet.entity.Post;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author Edward Grippe
 */
public interface PostRepository extends CrudRepository<Post, Integer> {
    List<Post> findByUserId(Integer userId);
    List<Post> findByThreadId(Integer threadId);
}
