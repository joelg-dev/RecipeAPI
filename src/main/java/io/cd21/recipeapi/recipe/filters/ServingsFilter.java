package io.cd21.recipeapi.recipe.filters;

import io.cd21.recipeapi.filter.AbstractFilter;
import io.cd21.recipeapi.filter.FilterType;
import io.cd21.recipeapi.filter.SpecificationFilter;
import io.cd21.recipeapi.recipe.Recipe;
import io.cd21.recipeapi.recipe.Recipe_;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class ServingsFilter extends AbstractFilter implements SpecificationFilter<Recipe> {

    public ServingsFilter(){
        super(FilterType.SERVINGS);
    }

    @Override
    public Specification<Recipe> doFilter(Specification<Recipe> spec, String filterValue) {
        if (filterValue == null)
            return null;

        Integer servings = Integer.parseInt(filterValue);

        return (root, query, cb) -> cb.equal(root.get(Recipe_.SERVINGS), servings);
    }
}
