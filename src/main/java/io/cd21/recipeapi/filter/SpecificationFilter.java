package io.cd21.recipeapi.filter;

import org.springframework.data.jpa.domain.Specification;

public interface SpecificationFilter<T> {
    Specification<T> doFilter(Specification<T> spec, String filterValue);
    FilterType getFilterType();
}
