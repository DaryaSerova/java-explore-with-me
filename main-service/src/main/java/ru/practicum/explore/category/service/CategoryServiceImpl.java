package ru.practicum.explore.category.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explore.category.dto.CategoryDto;
import ru.practicum.explore.category.dto.NewCategoryDto;
import ru.practicum.explore.category.jpa.CategoryPersistService;
import ru.practicum.explore.category.mapper.CategoryMapper;
import ru.practicum.explore.exceptions.BadRequestException;
import ru.practicum.explore.exceptions.ConflictException;
import ru.practicum.explore.exceptions.NotFoundException;

import javax.swing.*;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryPersistService categoryPersistService;
    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryDto> getCategories(Integer from, Integer size) {
        var categories = categoryPersistService.findCategories(from, size).getContent();

        if (categories == null || categories.isEmpty()) {
            return Collections.emptyList();
        }

        return categories.stream()
                .map(category -> categoryMapper.map(category)).collect(Collectors.toList());
    }

    public CategoryDto getCategoryById(Long catId) {

        var category = categoryPersistService.findCategoryById(catId);

        if (category.isEmpty()) {
            throw new NotFoundException("The required object was not found.",
                          String.format("Category with %s was not found",catId));
        }

        var categoryResult = category.get();

        return categoryMapper.map(categoryResult);
    }

    @Override
    public CategoryDto addCategory(NewCategoryDto newCategoryDto) {

        if (newCategoryDto.getName() == null) {
            throw new BadRequestException("Bad request body", "Category name is empty");
        }
        var cat = categoryPersistService.findCategoryByName(newCategoryDto.getName());

        if (cat != null && cat.getName().equals(newCategoryDto.getName())) {
            throw new ConflictException("Integrity constraint has been violated.",
                                        "could not execute statement; SQL [n/a]; constraint [uq_category_name]; " +
                                        "nested exception is org.hibernate.exception.ConstraintViolationException: " +
                                        "could not execute statement");
        }

        categoryMapper.toMapCategory(newCategoryDto);

        var category = categoryPersistService.addCategory(categoryMapper.toMapCategory(newCategoryDto));

        return categoryMapper.map(category);
    }

    @Override
    public void deleteCategory(Long catId) {

        var category = categoryPersistService.findCategoryById(catId);

        if (category.isEmpty()) {
            throw new NotFoundException("The required object was not found.",
                          String.format("Category with %s was not found", catId));
        }

        if (category.get().getEvents() != null && category.get().getEvents().size() > 0) {
            throw new ConflictException("For the requested operation the conditions are not met.",
                                        "The category is not empty");
        }

        categoryPersistService.deleteCategory(catId);
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto, Long catId) {

        if (categoryDto.getName() == null) {
            throw new BadRequestException("Bad request body", "Category name is empty");
        }

        var cat = categoryPersistService.findCategoryByName(categoryDto.getName());

        if (cat != null && cat.getName().equals(categoryDto.getName())) {
            throw new ConflictException("Integrity constraint has been violated.",
                                        "could not execute statement; SQL [n/a]; constraint [uq_category_name]; " +
                                        "nested exception is org.hibernate.exception.ConstraintViolationException: " +
                                        "could not execute statement");
        }

        var category = categoryPersistService.findCategoryById(catId).get();
        if (category == null) {
            throw new NotFoundException("The required object was not found.",
                          String.format("Category with %s was not found", catId));
        }

        category.setName(categoryDto.getName());

        return categoryMapper.mapToCategoryDto(categoryPersistService.updateCategory(category));
    }
}
