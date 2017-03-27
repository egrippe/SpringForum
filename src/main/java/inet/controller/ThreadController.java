package inet.controller;

import inet.entity.Post;
import inet.entity.Thread;
import inet.repository.PostRepository;
import inet.repository.ThreadRepository;
import inet.repository.UserRepository;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Date;
import java.util.ArrayList;
import java.util.Collections;

/**
 * @author Edward Grippe
 */
@RestController
@RequestMapping(path = "/thread")
public class ThreadController {

    private final ThreadRepository threadRepository;
    private final PostRepository   postRepository;
    private final UserRepository   userRepository;

    // chooses how many trending threads are supposed to be shown, low number set for demonstartion purpose
    private final int nrOfTrendingThreads = 1;

    @Autowired
    public ThreadController(ThreadRepository threadRepository, PostRepository postRepository, UserRepository userRepository) {
        this.threadRepository = threadRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }


    private static class ThreadResponse {
        public int    id;
        public int    user_id;
        public String title;
        public String description;

        public String username;
        public String date_string;

        public ThreadResponse(String username, Thread thread) {
            this.id = thread.getId();
            this.user_id = thread.getUserId();
            this.title = thread.getTitle();
            this.description = thread.getDescription();

            this.username = username;
            this.date_string = thread.getDate().toString().substring(0, 16);
        }
    }

    @GetMapping(path = "/all")
    public List<ThreadResponse> getAllThreads() {
        Iterable<Thread> threads = threadRepository.findAll();

        List<ThreadResponse> response = new ArrayList<>();
        for (Thread t : threads) {
            String username = userRepository.findOne(t.getUserId()).getName();
            response.add(new ThreadResponse(username, t));
        }

        return response;
    }

    /**
     * Add a new thread to the forum.
     * @param thread the thread to add
     * @param request requestObject
     * @return the added thread
     */
    @PostMapping(path = "/add")
    public Thread addThread(@RequestBody Thread thread, HttpServletRequest request) {
        if (thread.getTitle() == null || thread.getDescription() == null) {
            throw new RuntimeException("Invalid thread object");
        }
        Integer userId = (Integer) request.getAttribute("userId");
        thread.setUserId(userId);
        thread.setDate(new Date());
        thread = threadRepository.save(thread);
        return thread;
    }

    @PostMapping(path = "/{threadId}/delete")
    public Thread deleteThread(@PathVariable Integer threadId, HttpServletRequest request) {
        // get user credentials
        Claims claims = (Claims) request.getAttribute("claims");
        List<String> roles = (List<String>) claims.get("roles");
        if (!roles.contains("admin")) {
            throw new RuntimeException("Authentication failed, no admin privileges");
        }

        // delete the specified thread
        Thread thread = threadRepository.findOne(threadId);
        threadRepository.delete(threadId);
        return thread;
    }


    private static class PostResponse {
        public String username;
        public Post   post;
        public String date_string;
        public PostResponse(String username, Post post) {
            this.username = username;
            this.post = post;
            date_string = post.getDate().toString().substring(0,16);  // trim away seconds
        }
    }


    /**
     * Get all posts in a given thread.
     * TODO: throw error if thread not found
     * @param threadId the thread id.
     * @return posts in the given thread
     */
    @GetMapping(path = "/{threadId}/posts")
    public Iterable<PostResponse> getThreadPosts(@PathVariable Integer threadId) {
        Iterable<Post> posts = postRepository.findByThreadId(threadId);

        List<PostResponse> response = new ArrayList<>();
        for (Post p : posts) {
            String username = userRepository.findOne(p.getUserId()).getName();
            response.add(new PostResponse(username ,p));
        }

        return response;
    }

    /**
     * Add a post to a given thread.
     * @param threadId the thread id.
     * @param post the post to add.
     * @param request request object
     * @return the post that was added.
     */
    @PostMapping(path = "/{threadId}/addPost")
    public Post addPost(@PathVariable Integer threadId, @RequestBody Post post, HttpServletRequest request) {

        // check if thread exists
        if (!threadRepository.exists(threadId)) throw new RuntimeException("Thread not found");

        // get user credentials
        Integer userId = (Integer) request.getAttribute("userId");
        post.setUserId(userId);
        post.setDate(new Date());
        post = postRepository.save(post);

        return post;
    }

    /**
    * Calculates and returns the nrOfTrendingThreads most trending threads, that is : 
    * the threads with the most number of posts the last day
    * OBS:: ugly solution..................
    */
    @GetMapping(path = "/trending")
    public List<ThreadResponse> getTrendingThreads() {
        Iterable<Thread> allThreads = threadRepository.findAll();
        // 
        List<ThreadTuple> ttArray = new ArrayList<ThreadTuple>();
        List<Thread> threadArrayList = new ArrayList<Thread>();
        List<Thread> mostTrending = new ArrayList<Thread>(); // will 
        Thread tmpThread = null;
        for(Thread thread : allThreads){
            threadArrayList.add(thread);
            ttArray.add(new ThreadTuple(thread.getId(), getNumberOfPostLastDay(thread)));
        }
            
        Collections.sort(ttArray);
        Collections.reverse(ttArray); //most popular first in list
        int nextMostTrendingIdx = 0;
        Thread nextThread = null;
        while(nextMostTrendingIdx < nrOfTrendingThreads && nextMostTrendingIdx < threadArrayList.size()){
            for(Thread thread : threadArrayList){
                if(ttArray.get(nextMostTrendingIdx).threadId == thread.getId()){
                    mostTrending.add(thread);
                    break; 
                }
            }
            nextMostTrendingIdx++;
        }

        List<ThreadResponse> response = new ArrayList<>();
        for (Thread t : mostTrending) {
            String username = userRepository.findOne(t.getUserId()).getName();
            response.add(new ThreadResponse(username, t));
        }

        //Iterable<Thread> ret = mostTrending;
        return response;
    }
    //tuple only used for getTrendingThreads function
    private class ThreadTuple implements Comparable<ThreadTuple>{
        int threadId = 0;
        int nrOfPosts = 0;
        ThreadTuple(int threadId, int nrOfPosts){
            this.threadId = threadId;
            this.nrOfPosts = nrOfPosts;
        }

        @Override
        public int compareTo(ThreadTuple tt){
            if(tt == null){
                 throw new IllegalArgumentException("param cannot be null.");
            }
            if(this.nrOfPosts ==  tt.nrOfPosts) return 0;
            if(this.nrOfPosts < tt.nrOfPosts) return -1;

            return 1;
        }
    }

    //TODO:: should probably more easily be calulated by using sql from threadRepository?
    public int getNumberOfPostLastDay (Thread thread){
        if(thread == null){
                 throw new IllegalArgumentException("param cannot be null.");
        }
        int threadId = thread.getId();
        // variable to compare if post was written last day, 86400000 is the number of milliseconds in one day
        Date lastDayDate = new Date((new Date().getTime() - 86400000)); 
        int nrOfPosts = 0;

        Iterable<Post> threadPosts = postRepository.findByThreadId(threadId);

        for(Post post: threadPosts){
            //after returns true if obj is later than lastDayDate
            if(post.getDate().after(lastDayDate)){
                nrOfPosts += 1;
            }
        }
        return nrOfPosts;
    }

    /*
    @GetMapping(path = "/{threadId}")
    public @ResponseBody Thread getThread(@PathVariable Integer threadId) {
        Thread thread = threadRepository.findOne(threadId);

        if (thread == null) throw new RuntimeException("Thread not found");

        return threadRepository.findOne(threadId);
    }
    */
}
