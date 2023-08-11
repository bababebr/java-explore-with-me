package ru.practicum.ewm.mapper;

import ru.practicum.ewm.models.category.Category;
import ru.practicum.ewm.models.category.CategoryDto;
import ru.practicum.ewm.models.category.NewCategoryDto;

public class CategoryMapper {

    public static CategoryDto categoryToDto(Category category) {
        return CategoryDto.create(category.getId(), category.getName());
    }

    public static Category dtoToCategory(CategoryDto dto) {
        return Category.create(0L, dto.getName());
    }

    public static Category newCategoryToCategory(NewCategoryDto newDto) {
        return Category.create(0L, newDto.getName());
    }

}
