package io.cd21.recipeapi.recipe.filters;

import io.cd21.recipeapi.filter.AbstractFilter;
import io.cd21.recipeapi.filter.FilterType;
import io.cd21.recipeapi.filter.SpecificationFilter;
import io.cd21.recipeapi.ingredient.Ingredient_;
import io.cd21.recipeapi.recipe.Recipe;
import io.cd21.recipeapi.recipe.Recipe_;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.*;

@Component
public class IngredientsExcludeFilter extends AbstractFilter implements SpecificationFilter<Recipe> {

    public IngredientsExcludeFilter() {
        super(FilterType.INGREDIENTSEXCLUDE);
    }

    @Override
    public Specification<Recipe> doFilter(Specification<Recipe> spec, String filterValue) {
        if (filterValue == null)
            return null;

        return (root, query, cb) -> {
            Join<Object, Object> join = root.join(Recipe_.INGREDIENTS, JoinType.LEFT);
            join = join.on(cb.equal(cb.lower(join.get(Ingredient_.NAME)),filterValue.toLowerCase()));
            return cb.isNull(join.get(Ingredient_.NAME));
        };
    }

}
