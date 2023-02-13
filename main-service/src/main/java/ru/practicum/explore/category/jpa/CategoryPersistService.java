package ru.practicum.explore.category.jpa;

import org.springframework.data.domain.Page;
import ru.practicum.explore.category.model.Category;

import java.util.Optional;

public interface CategoryPersistService {

    Page<Category> findCategories(Integer from, Integer size);

    Optional<Category> findCategoryById(Long catId);

    Category findCategoryByName(String name);

    Category addCategory(Category category);

    Category updateCategory(Category category);

    void deleteCategory(Long catId);
}
