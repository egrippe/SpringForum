package inet.controller;

import inet.entity.Post;
import inet.repository.PostRepository;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Iterator;
import java.util.List;

/**
 * @author Edward Grippe
 */
@RestController
@RequestMapping(path = "/post")
public class PostController {
    private final PostRepository postRepository;

    @Autowired
    public PostController(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @GetMapping(path = "/all")
    public Iterable<Post> getAllPosts() {

        return postRepository.findAll();
    }

    @PostMapping(path = "/{postId}/delete")
    public Post deletePost(@PathVariable Integer postId, HttpServletRequest request) {

        // check user is admin
        Claims claims = (Claims) request.getAttribute("claims");
        List<String> roles = (List<String>) claims.get("roles");
        if (!roles.contains("admin")) {
            throw new RuntimeException("Authentication failed, no admin privileges");
        }

        // delete the specified thread
        Post post = postRepository.findOne(postId);
        postRepository.delete(postId);

        return post;
    }

    @GetMapping(path = "/user/{userId}")
    public Iterable<Post> getPostsByUserId(@PathVariable Integer userId) {
        return postRepository.findByUserId(userId);
    }
}
