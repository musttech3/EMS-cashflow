package com.alphasoft.EMS.rest;

import com.alphasoft.EMS.dto.CategoryRequest;
import com.alphasoft.EMS.dto.UserResponse;
import com.alphasoft.EMS.enums.CategoryType;
import com.alphasoft.EMS.model.Category;
import com.alphasoft.EMS.model.User;
import com.alphasoft.EMS.service.AuthenticationService;
import com.alphasoft.EMS.service.CategoryService;
import com.alphasoft.EMS.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    private final CategoryService categoryService;
    private final AuthenticationService authenticationService;
    private final UserService userService;

    @Autowired
    public CategoryController(
            CategoryService categoryService,
            AuthenticationService authenticationService,
            UserService userService
    ) {
        this.categoryService = categoryService;
        this.authenticationService = authenticationService;
        this.userService = userService;
    }

    @GetMapping("/categories/user") // Test success
    public ResponseEntity<List<CategoryRequest>> getAllCategoriesByUserId(
            @CookieValue(value = "CashFlow-EMS-JWT") String jwt,
            HttpServletRequest httpRequest
    ) {
        UserResponse userResponse = authenticationService.fetchAPI(jwt, httpRequest.getHeader("User-Agent"));
        List<Category> categories = categoryService.findAllByUserId(userResponse.getId());

        if (categories.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        List<CategoryRequest> categoryRequests = categories.stream()
                .map(category -> CategoryRequest.builder()
                        .categoryName(category.getCategoryName())
                        .type(category.getType().name())
                        .build())
                .toList();
        return ResponseEntity.ok().body(categoryRequests);
    }

    @PostMapping("categories/user") // Test success
    public ResponseEntity<String> createCategoryByUserId(
            @RequestBody CategoryRequest categoryRequest,
            @CookieValue(value = "CashFlow-EMS-JWT") String jwt,
            HttpServletRequest httpRequest
    ) {
        UserResponse userResponse = authenticationService.fetchAPI(jwt, httpRequest.getHeader("User-Agent"));
        User user = userService.findUserById(userResponse.getId());

        if (categoryRequest.getCategoryName().matches(".*\\d.*")){
            ResponseEntity.status(403).body("Numbers are not allowed");
        }

        categoryRequest.setCategoryName(categoryRequest.getCategoryName().toLowerCase());
        String categoryName = categoryRequest.getCategoryName().substring(0, 1).toUpperCase() + categoryRequest.getCategoryName().substring(1);
        categoryRequest.setCategoryName(categoryName);

        Category existCategory = categoryService.findCategoryByUserIdAndCategoryName(user.getId(), categoryRequest.getCategoryName());
        if (existCategory == null){
            Category category = Category.builder()
                    .categoryName(categoryRequest.getCategoryName())
                    .type(CategoryType.valueOf(categoryRequest.getType()))
                    .user(user)
                    .build();
            categoryService.save(category);
            return ResponseEntity.ok().body("Category created successfully");
        }

        return ResponseEntity.status(403).body("You already have this category");
    }
}
