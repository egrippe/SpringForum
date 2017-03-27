package inet.controller;

import inet.entity.Category;
import inet.entity.Thread;
import inet.repository.CategoryRepository;
import inet.repository.ThreadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/categories")
public class CategoryController {

    private final CategoryRepository categoryRepository;
    private final ThreadRepository   threadRepository;

    @Autowired
    public CategoryController(CategoryRepository categoryRepository, ThreadRepository threadRepository) {
        this.categoryRepository = categoryRepository;
        this.threadRepository = threadRepository;
    }

    @GetMapping(path = "/all")
    public Iterable<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @GetMapping(path = "/{categoryId}/threads")
    public Iterable<Thread> getThreadsWithCategoryId(@PathVariable Integer categoryId) {
        return threadRepository.findByCategoryId(categoryId);
    }
}
