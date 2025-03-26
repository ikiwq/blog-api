package com.ikiwq.blog.api.model.mapper;


import com.ikiwq.blog.api.model.dto.response.CategoryResponse;
import com.ikiwq.blog.api.model.dto.request.CategoryCreationRequest;
import com.ikiwq.blog.api.model.entity.Category;
import org.mapstruct.*;

import java.time.Instant;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryResponse toResponse(Category category);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "articles", ignore = true)
    })
    Category toEntity(CategoryCreationRequest request);

    @AfterMapping
    default void afterToEntityMapping(@MappingTarget Category category){
        category.setCreatedAt(Instant.now());
    }
}
