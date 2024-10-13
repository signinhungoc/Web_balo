package com.example.demo.controller;

import com.example.demo.entity.Category;
import com.example.demo.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/categories")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:3000"})
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }


    @GetMapping("/all")
    public Iterable<Category> getAllCategories() {
        return categoryService.findAll();
    }

    // Get category by ID
    @GetMapping("/getById/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Integer id) {
        Optional<Category> category = categoryService.findById(id);
        return category.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @PostMapping("/create")
    public ResponseEntity<String> createCategory(@RequestBody Category category) {
        if (categoryService.existsByCategoryName(category.getCategoryName(), null)) {
            return ResponseEntity.badRequest().body("Danh mục đã tồn tại");
        }
        Category savedCategory = categoryService.save(category);
        return ResponseEntity.ok("Danh mục đã được tạo thành công: " + savedCategory.getCategoryName());
    }

    // Update an existing category by ID
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateCategory(@PathVariable Integer id, @RequestBody Category category) {
        if (categoryService.findById(id).isPresent()) {
            if (categoryService.existsByCategoryName(category.getCategoryName(), id)) {
                return ResponseEntity.badRequest().body("Danh mục đã tồn tại");
            }
            category.setCategoryID(id);
            categoryService.save(category);
            return ResponseEntity.ok("Danh mục đã được cập nhật thành công: " + category.getCategoryName());
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Integer id) {
        if (categoryService.findById(id).isPresent()) {
            categoryService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
