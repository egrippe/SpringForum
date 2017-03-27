package inet;

import inet.entity.*;
import inet.entity.Thread;
import inet.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Add data to the forum when spring boot starts.
 *
 * @author Edward Grippe
 */
@Component
public class ForumApplicationDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final ThreadRepository threadRepository;
    private final RoleRepository roleRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public ForumApplicationDataLoader(UserRepository userRepository, PostRepository postRepository,
                                      ThreadRepository threadRepository, RoleRepository roleRepository,
                                      CategoryRepository categoryRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.threadRepository = threadRepository;
        this.roleRepository = roleRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        // create some forum categories
        categoryRepository.save(new Category(1,"Ponies"));
        categoryRepository.save(new Category(2,"Kittens"));
        categoryRepository.save(new Category(3,"Doges"));
        categoryRepository.save(new Category(4,"Other"));

        // Create forum roles
        Role adminRole = new Role(1, "admin");
        roleRepository.save(adminRole);

        Role userRole = new Role(2, "user");
        roleRepository.save(userRole);

        SCryptPasswordEncoder scrypt = new SCryptPasswordEncoder();

        // Create forum users
        User edward = new User(1, "edward", scrypt.encode("kladdkaka"));
        edward.addRole(adminRole);
        edward.addRole(userRole);
        userRepository.save(edward);

        User johan = new User(2, "johan", scrypt.encode("apa11"));
        johan.addRole(userRole);
        userRepository.save(johan);

        //  create a forum thread
        Thread t = new Thread(1, "The first forum thread about ponies.", "Magnificanat description", 1, 1);
        threadRepository.save(t);

        t = new Thread(2, "This is a second forum thread about kittens", "Here you can talk about kittens", 2, 2);
        threadRepository.save(t);

        // create forum posts
        Post p = new Post(1, "The first forum post by edward", 1, 1);
        postRepository.save(p);

        p = new Post(2, "Hello i'm Johan this is my first post", 1, 2);
        postRepository.save(p);

        p = new Post(3, "Today am building a forum application with angular and bootstrap.", 2, 1);
        postRepository.save(p);
    }
}
