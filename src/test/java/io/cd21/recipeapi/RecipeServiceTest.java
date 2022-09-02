package io.cd21.recipeapi;

import io.cd21.recipeapi.exception.InvalidRequestException;
import io.cd21.recipeapi.exception.ResourceNotFoundException;
import io.cd21.recipeapi.filter.SpecificationFilter;
import io.cd21.recipeapi.ingredient.Ingredient;
import io.cd21.recipeapi.recipe.Recipe;
import io.cd21.recipeapi.recipe.RecipeRepository;
import io.cd21.recipeapi.recipe.RecipeService;
import io.cd21.recipeapi.tag.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RecipeServiceTest {

    private RecipeService service;

    private RecipeRepository repository = Mockito.mock(RecipeRepository.class);
    private List<SpecificationFilter<Recipe>> availableFilters = Mockito.mock(List.class);


    @BeforeEach
    void initUseCase() {
        service = new RecipeService(repository, availableFilters);
    }

    @Test
    public void addRecipe_whenMissingIngredient_thenException() {
        Recipe recipe = new Recipe("Soup", 4, "Stir it a little");
        assertThatExceptionOfType(InvalidRequestException.class)
                .isThrownBy(() -> service.addNewRecipe(recipe))
                .withMessageContaining("at least 1 ingredient is required");
    }

    @Test
    public void addRecipe_whenIngredientNotUnique_thenException() {
        Recipe recipe = new Recipe("French fries", 1, "Make it nice and cruncy");

        Set<Ingredient> ingredients = new HashSet<>();
        ingredients.add(new Ingredient("Potato", 3, "Kilo"));
        ingredients.add(new Ingredient("Potato", 3, "Kilo"));
        recipe.setIngredients(ingredients);

        assertThatExceptionOfType(InvalidRequestException.class)
                .isThrownBy(() -> service.addNewRecipe(recipe))
                .withMessageContaining("every ingredient in a recipe needs to be unique");
    }


    @Test
    public void addRecipe_whenTagNotUnique_thenException() {
        Recipe recipe = new Recipe("French fries", 1, "Make it nice and cruncy");

        Set<Ingredient> ingredients = new HashSet<>();
        ingredients.add(new Ingredient("Potato", 3, "Kilo"));
        recipe.setIngredients(ingredients);

        Set<Tag> tags = new HashSet<>();
        tags.add(new Tag("Vegetarian"));
        tags.add(new Tag("Vegetarian"));
        recipe.setTags(tags);

        assertThatExceptionOfType(InvalidRequestException.class)
                .isThrownBy(() -> service.addNewRecipe(recipe))
                .withMessageContaining("every tag in a recipe needs to be unique");
    }

    @Test
    public void addRecipe_whenNameNotUnique_thenException() {
        Recipe recipe = new Recipe("French fries", 1, "Make it nice and cruncy");

        Set<Ingredient> ingredients = new HashSet<>();
        ingredients.add(new Ingredient("Potato", 3, "Kilo"));
        recipe.setIngredients(ingredients);

        when(repository.findByName(anyString())).thenReturn(List.of(recipe));

        assertThatExceptionOfType(InvalidRequestException.class)
        .isThrownBy(() -> service.addNewRecipe(recipe))
        .withMessageContaining("already a recipe with this name");
    }


    @Test
    public void findRecipe_whenNotExists_thenException() {
        assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() -> service.getRecipe(UUID.randomUUID()))
                .withMessageContaining("does not exist");
    }

}
