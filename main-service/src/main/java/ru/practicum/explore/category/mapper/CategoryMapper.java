package ru.practicum.explore.category.mapper;


import org.mapstruct.Mapper;
import ru.practicum.explore.category.dto.CategoryDto;
import ru.practicum.explore.category.dto.NewCategoryDto;
import ru.practicum.explore.category.model.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryDto map(Category category);

    CategoryDto mapToCategoryDto(Category category);

    Category toMapCategory(NewCategoryDto newCategoryDto);
}
