package com.ikiwq.blog.api.model.entity.specification;

import com.ikiwq.blog.api.model.entity.Article;
import org.springframework.data.jpa.domain.Specification;

public class ArticleSpecifications {
    public static Specification<Article> hasCategory(String categoryName) {
        return ((root, query, criteriaBuilder) -> {
            if(categoryName == null || categoryName.isBlank()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("category").get("name"), categoryName);
        });
    }

    public static Specification<Article> isPinned(Boolean pinned){
        return((root, query, criteriaBuilder) -> {
            if(pinned == null){
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("pinned"), pinned);
        });
    }

    public static Specification<Article> combinedSpecifications(String categoryName, Boolean pinned){
        return Specification
                .where(hasCategory(categoryName))
                .and(isPinned(pinned));
    }
}
