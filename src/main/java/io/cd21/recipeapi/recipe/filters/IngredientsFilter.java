package io.cd21.recipeapi.recipe.filters;

import io.cd21.recipeapi.filter.AbstractFilter;
import io.cd21.recipeapi.filter.FilterType;
import io.cd21.recipeapi.filter.SpecificationFilter;
import io.cd21.recipeapi.ingredient.Ingredient_;
import io.cd21.recipeapi.recipe.Recipe;
import io.cd21.recipeapi.recipe.Recipe_;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
@Component
public class IngredientsFilter extends AbstractFilter implements SpecificationFilter<Recipe> {

    public IngredientsFilter(){
        super(FilterType.INGREDIENTS);
    }

    @Override
    public Specification<Recipe> doFilter(Specification<Recipe> spec, String filterValue) {
        if (filterValue == null)
            return null;

        return (root, query, cb) -> cb.equal(cb.lower(root.join(Recipe_.INGREDIENTS, JoinType.INNER).get(Ingredient_.NAME)), filterValue.toLowerCase());
    }


}
