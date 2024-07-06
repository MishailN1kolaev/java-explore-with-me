package ru.practicum.model.category;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import ru.practicum.model.category.categoryDto.CategoryDto;
import ru.practicum.model.category.categoryDto.NewCategoryDto;
import ru.practicum.model.event.EventRepository;
import ru.practicum.model.exeption.AlreadyExistException;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    @Transactional
    @Override
    public CategoryDto addCategory(NewCategoryDto newCategoryDto) {
        if (categoryRepository.findAll().stream()
                .anyMatch(category -> category.getName().equals(newCategoryDto.getName()))) {
            throw new AlreadyExistException("Категория с таким именем уже существует");
        }
        return CategoryMapper.mapToCategoryDto(categoryRepository.save(CategoryMapper.mapToCategory(newCategoryDto)));
    }

    @Transactional
    @Override
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Категории не существует"));
        if (eventRepository.findAll().stream()
                .anyMatch(event -> event.getCategory().equals(category))) {
            throw new AlreadyExistException("Невозможно удалить категория, так как к ней привязаны события");
        }
        categoryRepository.deleteById(id);
    }

    @Transactional
    @Override
    public CategoryDto updateCategory(Long id, String newName) {
        Category updateCategory = categoryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Категории не существует"));
        if (categoryRepository.findAll().stream()
                .anyMatch(category -> category.getName().equals(newName)) && !updateCategory.getName().equals(newName)) {
            throw new AlreadyExistException("Категория с таким названием уже существует");
        } else {
            updateCategory.setName(newName);
            return CategoryMapper.mapToCategoryDto(categoryRepository.save(updateCategory));
        }
    }

    @Override
    public List<CategoryDto> getAllCategory(Integer from, Integer size) {
        PageRequest page = PageRequest.of(from / size, size);
        return categoryRepository.findAll(page).stream()
                .map(CategoryMapper::mapToCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategoryById(@PathVariable Long catId) {
        return CategoryMapper.mapToCategoryDto(categoryRepository.findById(catId).orElseThrow(() -> new EntityNotFoundException("Категория не существует")));
    }

}
