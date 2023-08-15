package ru.practicum.ewm.service.interfaces;

import ru.practicum.ewm.models.category.CategoryDto;
import ru.practicum.ewm.models.category.NewCategoryDto;

import java.util.List;

public interface ICategoryService {

    CategoryDto add(NewCategoryDto dto);

    void delete(Long id);

    CategoryDto update(Long id, NewCategoryDto dto);

    List<CategoryDto> getAll(int from, int size);
}
