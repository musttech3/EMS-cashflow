package com.alphasoft.EMS.repository;

import com.alphasoft.EMS.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("SELECT c FROM Category c WHERE c.user.id = :userId OR c.user.id = 1")
    Optional<List<Category>> findAllByUserId(@Param("userId") Long userId);

    @Query("SELECT c FROM Category c WHERE c.user.id = :userId AND c.categoryName = :categoryName")
    Optional<Category> findCategoryByUserIdAndCategoryName(
            @Param("userId") Long userId,
            @Param("categoryName") String categoryName
    );

}
