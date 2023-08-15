package ru.practicum.ewm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.mapper.CategoryMapper;
import ru.practicum.ewm.models.category.Category;
import ru.practicum.ewm.models.category.CategoryDto;
import ru.practicum.ewm.models.category.NewCategoryDto;
import ru.practicum.ewm.repository.CategoryRepository;
import ru.practicum.ewm.repository.EventRepository;
import ru.practicum.ewm.service.interfaces.ICategoryService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class CategoryService implements ICategoryService {
    private final CategoryRepository repository;
    private final EventRepository eventRepository;

    @Autowired
    public CategoryService(CategoryRepository repository, EventRepository eventRepository) {
        this.repository = repository;
        this.eventRepository = eventRepository;
    }

    @Override
    public CategoryDto add(NewCategoryDto dto) {
        try {
            Category category = repository.save(CategoryMapper.newCategoryToCategory(dto));
            return CategoryMapper.categoryToDto(category);
        } catch (RuntimeException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    @Override
    public void delete(Long id) {
        Category category = repository.findById(id).orElseThrow(() -> new NoSuchElementException());
        if (eventRepository.findAllByCategory(category).isEmpty()) {
            repository.delete(category);
        } else {
            throw new IllegalStateException(String.format("Event with category=%s exist", category.getId()));
        }
    }

    @Override
    public CategoryDto update(Long id, NewCategoryDto dto) {
        Category category = repository.findById(id).orElseThrow(() -> new NoSuchElementException());
        category.setName(dto.getName());
        return CategoryMapper.categoryToDto(repository.save(category));
    }

    @Override
    public List<CategoryDto> getAll(int form, int size) {
        List<Category> categories = repository.findAll(PageRequest.of(form, size)).getContent();
        return categories.stream().map(CategoryMapper::categoryToDto).collect(Collectors.toList());
    }

    @Override
    public CategoryDto get(Long id) {
        Category category = repository.findById(id).orElseThrow(()
                -> new NoSuchElementException(String.format("Category with ID=%s not found.", id)));
        return CategoryMapper.categoryToDto(category);
    }
}
