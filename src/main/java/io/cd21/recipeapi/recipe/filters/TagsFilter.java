package io.cd21.recipeapi.recipe.filters;

import io.cd21.recipeapi.filter.AbstractFilter;
import io.cd21.recipeapi.filter.FilterType;
import io.cd21.recipeapi.filter.SpecificationFilter;
import io.cd21.recipeapi.recipe.Recipe;
import io.cd21.recipeapi.recipe.Recipe_;
import io.cd21.recipeapi.tag.Tag_;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.*;
@Component
public class TagsFilter extends AbstractFilter implements SpecificationFilter<Recipe> {

    public TagsFilter(){
        super(FilterType.TAGS);
    }

    @Override
    public Specification<Recipe> doFilter(Specification<Recipe> spec, String filterValue) {
        if (filterValue == null)
            return null;

        return (root, query, cb) -> cb.equal(cb.lower(root.join(Recipe_.TAGS, JoinType.INNER).get(Tag_.NAME)), filterValue.toLowerCase());
    }
}
