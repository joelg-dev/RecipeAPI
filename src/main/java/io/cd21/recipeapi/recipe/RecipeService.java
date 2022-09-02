package io.cd21.recipeapi.recipe;

import io.cd21.recipeapi.exception.InvalidParameterException;
import io.cd21.recipeapi.exception.InvalidRequestException;
import io.cd21.recipeapi.exception.ResourceNotFoundException;
import io.cd21.recipeapi.filter.FilterType;
import io.cd21.recipeapi.filter.SpecificationFilter;
import io.cd21.recipeapi.ingredient.Ingredient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class RecipeService {
    private final RecipeRepository repository;
    private final List<SpecificationFilter<Recipe>> recipeFilters;
    private final Integer PARAM_MULTIPLE_MAX = 6;

    @Autowired
    public RecipeService(RecipeRepository repository, List<SpecificationFilter<Recipe>> recipeFilters) {
        this.repository = repository;
        this.recipeFilters = recipeFilters;
    }

    public List<Recipe> getRecipes(Map<FilterType, String[]> filters) throws InvalidParameterException {
        if (filters == null)
            return repository.findAll();

        Specification<Recipe> spec = Specification.where(null);
        for (var filter : filters.entrySet()) {
            SpecificationFilter<Recipe> specFilter = null;
            String[] filterValues = filter.getValue();

            if (filterValues == null)
                continue;

            if (filterValues.length > PARAM_MULTIPLE_MAX)
                throw new InvalidParameterException("Too many of parameter %s", filter.getKey().toString());

            for (SpecificationFilter<Recipe> recipeFilter: recipeFilters) {
                if (recipeFilter.getFilterType() == filter.getKey()) {
                    specFilter = recipeFilter;
                    break;
                }
            }

            if (specFilter == null)
                continue;

            for (String value : filterValues) {
                spec = spec.and(specFilter.doFilter(spec, value));
            }
        }

        return repository.findAll(spec);
    }

    public Recipe addNewRecipe(Recipe recipe) throws InvalidRequestException {
        /* todo check tags against some enum? or root table */

        if (recipe.getIngredients() == null || recipe.getIngredients().size() == 0 )
            throw new InvalidRequestException("at least 1 ingredient is required");

        Set<String> items = new HashSet<>();
        long doubleIngredients = recipe.getIngredients().stream()
                .filter(n -> !items.add(n.getName()))
                .count();

        if (doubleIngredients > 0)
            throw new InvalidRequestException("every ingredient in a recipe needs to be unique");

        if (recipe.getTags() != null) {
            items.clear();
            long doubleTags = recipe.getTags().stream()
                    .filter(n -> !items.add(n.getName()))
                    .count();

            if (doubleTags > 0)
                throw new InvalidRequestException("every tag in a recipe needs to be unique");
        }

        if (repository.findByName(recipe.getName()).size() > 0)
            throw new InvalidRequestException("there is already a recipe with this name, please choose another");

        return repository.save(recipe);
    }

    public void deleteRecipe(UUID recipeId) throws ResourceNotFoundException {
        Recipe recipe = getRecipe(recipeId);
        repository.delete(recipe);
    }

    public void modifyRecipe(UUID recipeId, Recipe recipe) throws ResourceNotFoundException {
        Recipe recipeData = getRecipe(recipeId);
        recipeData.setName(recipe.getName());
        recipeData.setInstructions(recipe.getInstructions());
        recipeData.setServings(recipe.getServings());
        repository.save(recipeData);
    }

    public Recipe getRecipe(UUID recipeId) throws ResourceNotFoundException {
        if (!repository.existsById(recipeId)){
            throw new ResourceNotFoundException("Recipe with id %s does not exist", recipeId);
        }

        return repository.findById(recipeId).get();
    }

}
