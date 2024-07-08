package ru.practicum.model.category;

import ru.practicum.model.category.categoryDto.CategoryDto;
import ru.practicum.model.category.categoryDto.NewCategoryDto;

public class CategoryMapper {

    public static Category mapToCategory(NewCategoryDto newCategoryDto) {
        Category category = new Category();
        category.setName(newCategoryDto.getName());
        return category;
    }

    public static CategoryDto mapToCategoryDto(Category category) {
        return new CategoryDto(
                category.getId(),
                category.getName()
        );
    }

}
