package ru.practicum.model.category;

import org.springframework.web.bind.annotation.PathVariable;
import ru.practicum.model.category.categoryDto.CategoryDto;
import ru.practicum.model.category.categoryDto.NewCategoryDto;

import java.util.List;

public interface CategoryService {

    CategoryDto addCategory(NewCategoryDto newCategoryDto);

    void deleteCategory(Long id);

    CategoryDto updateCategory(Long id, String newName);

    List<CategoryDto> getAllCategory(Integer from, Integer size);

    CategoryDto getCategoryById(@PathVariable Long catId);
}
