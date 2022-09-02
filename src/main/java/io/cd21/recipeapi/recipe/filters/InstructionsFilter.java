package io.cd21.recipeapi.recipe.filters;

import io.cd21.recipeapi.filter.AbstractFilter;
import io.cd21.recipeapi.filter.FilterType;
import io.cd21.recipeapi.filter.SpecificationFilter;
import io.cd21.recipeapi.recipe.Recipe;
import io.cd21.recipeapi.recipe.Recipe_;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class InstructionsFilter extends AbstractFilter implements SpecificationFilter<Recipe> {

    public InstructionsFilter(){
        super(FilterType.INSTRUCTIONS);
    }

    @Override
    public Specification<Recipe> doFilter(Specification<Recipe> spec, String filterValue) {
        if (filterValue == null)
            return null;

        return (root, query, cb) -> cb.like(cb.lower(root.get(Recipe_.INSTRUCTIONS)), '%' + filterValue.toLowerCase() + '%');
    }
}
