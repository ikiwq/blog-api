package com.ikiwq.blog.api.model.mapper;


import com.ikiwq.blog.api.model.dto.request.CategoryPayloadRequest;
import com.ikiwq.blog.api.model.dto.response.CategoryResponse;
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
    Category toEntity(CategoryPayloadRequest request);

    @AfterMapping
    default void afterToEntityMapping(@MappingTarget Category category){
        category.setCreatedAt(Instant.now());
    }

    @Mappings({
            @Mapping(target = "id", source = "category.id"),
            @Mapping(target = "name", source = "request.name"),
            @Mapping(target = "description", source = "request.description"),
            @Mapping(target = "slug", source = "request.slug"),
            @Mapping(target = "image", source = "request.image"),
            @Mapping(target = "articles", ignore = true),
            @Mapping(target = "createdAt", source = "category.createdAt")
    })
    Category merge(Category category, CategoryPayloadRequest request);
}
