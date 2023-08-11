package ru.practicum.ewm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.mapper.CategoryMapper;
import ru.practicum.ewm.models.category.Category;
import ru.practicum.ewm.models.category.CategoryDto;
import ru.practicum.ewm.models.category.NewCategoryDto;
import ru.practicum.ewm.repository.CategoryRepository;
import ru.practicum.ewm.service.interfaces.ICategoryService;

import java.util.NoSuchElementException;

@Service
public class CategoryService implements ICategoryService {
    private final CategoryRepository repository;

    @Autowired
    public CategoryService(CategoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public CategoryDto add(NewCategoryDto dto) {
        Category category = repository.save(CategoryMapper.newCategoryToCategory(dto));
       return CategoryMapper.categoryToDto(category);
    }

    @Override
    public void delete(Long id) {
        repository.findById(id).orElseThrow(() -> new NoSuchElementException());
    }

    @Override
    public CategoryDto update(Long id, NewCategoryDto dto) {
        Category category = repository.findById(id).orElseThrow(() -> new NoSuchElementException());
        category.setName(dto.getName());
        return CategoryMapper.categoryToDto(repository.save(category));
    }
}
