package ru.itcolleg.transaction.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itcolleg.auth.service.RequiresTokenValidation;
import ru.itcolleg.auth.service.TokenService;
import ru.itcolleg.transaction.model.Category;
import ru.itcolleg.transaction.service.CategoryService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/category")
public class CategoryRestController {

    private final CategoryService categoryService;

    private final TokenService tokenService;

    @Autowired
    public CategoryRestController(CategoryService categoryService, TokenService tokenService) {
        this.categoryService = categoryService;
        this.tokenService = tokenService;
    }

    @RequiresTokenValidation
    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        try {
            Optional<Category> category = categoryService.getById(id);
            return category.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }

    @RequiresTokenValidation
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategoryById(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        try {
            categoryService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllCategories(@RequestHeader("Authorization") String token) {
        try {
            Long userId = tokenService.extractUserIdFromToken(token);
            List<Category> categories = categoryService.getAll(userId);
            return ResponseEntity.ok(categories);
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Database Error");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }

    @RequiresTokenValidation
    @PostMapping
    public ResponseEntity<?> saveCategory(@RequestBody Category category, @RequestHeader("Authorization") String token) {
        try {
            Optional<Category> savedCategory = categoryService.save(category);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedCategory);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }

    @RequiresTokenValidation
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(@RequestBody Category category, @PathVariable Long id, @RequestHeader("Authorization") String token) {
        try {
            Optional<Category> updatedCategory = categoryService.update(category, id);
            return updatedCategory.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }
}
