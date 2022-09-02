package io.cd21.recipeapi.recipe;

import io.cd21.recipeapi.exception.InvalidParameterException;
import io.cd21.recipeapi.exception.InvalidRequestException;
import io.cd21.recipeapi.exception.ResourceNotFoundException;
import io.cd21.recipeapi.filter.FilterType;
import io.cd21.recipeapi.tag.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@RestController
@RequestMapping(path = "${app.base-path}/recipes")
public class RecipeController {
    private final RecipeService recipeService;

    @Autowired
    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @Parameter(in = ParameterIn.QUERY, description = "Amount of servings, for example '3'"
            , name = "servings", schema = @Schema(type = "integer", defaultValue = ""))
    @Parameter(in = ParameterIn.QUERY, description = "Any part of the instructions, for example 'oven'"
            , name = "instructions", schema = @Schema(type = "string"))
    @Parameter(in = ParameterIn.QUERY, description = "Tags (comma seperated) to include, for example 'Vegetarian'"
            , name = "tags", schema = @Schema(type = "string", defaultValue = ""))
    @Parameter(in = ParameterIn.QUERY, description = "Tags (comma seperated) to exclude, for example 'Vegetarian'"
            , name = "tags-exclude", schema = @Schema(type = "string", defaultValue = ""))
    @Parameter(in = ParameterIn.QUERY, description = "Ingredients (comma seperated) to include, for example 'Potatoes'"
            , name = "ingredients", schema = @Schema(type = "string", defaultValue = ""))
    @Parameter(in = ParameterIn.QUERY, description = "Ingredients (comma seperated) to exclude, for example 'Potatoes'"
            , name = "ingredients-exclude", schema = @Schema(type = "string", defaultValue = ""))
    @Parameter(name = "params", hidden = true)
    @GetMapping
    public List<Recipe> getRecipes(@RequestParam Map<String,String> params) {
        Map<FilterType, String[]> filters = new HashMap<>();

        filters.put(FilterType.SERVINGS, params.get("servings") == null ? null : new String[] { params.get("servings") });
        filters.put(FilterType.INSTRUCTIONS, params.get("instructions") == null ? null : new String[] { params.get("instructions") } );
        filters.put(FilterType.TAGS, params.get("tags") == null ? null : params.get("tags").split(","));
        filters.put(FilterType.TAGSEXCLUDE, params.get("tags-exclude") == null ? null : params.get("tags-exclude").split(","));
        filters.put(FilterType.INGREDIENTS, params.get("ingredients") == null ? null : params.get("ingredients").split(","));
        filters.put(FilterType.INGREDIENTSEXCLUDE, params.get("ingredients-exclude") == null ? null : params.get("ingredients-exclude").split(","));

        try {
            return recipeService.getRecipes(filters);
        } catch (InvalidParameterException e) {
            throw  new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getLocalizedMessage(), e);
        }
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED, reason = "OK")
    public void createNewRecipe(@RequestBody Recipe recipe) {
        try {
            recipeService.addNewRecipe(recipe);
        } catch (InvalidRequestException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @GetMapping(path = "{recipeId}")
    @Parameter(name="recipeId", schema = @Schema(type = "string", format="uuid"))
    public Recipe getRecipes(@PathVariable("recipeId") UUID recipeId) {
        try {
            return recipeService.getRecipe(recipeId);
        } catch (ResourceNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getLocalizedMessage(), e);
        }
    }

    @PutMapping(path = "{recipeId}")
    @Parameter(name="recipeId", schema = @Schema(type = "string", format="uuid"))
    public void updateRecipe(@PathVariable("recipeId") UUID recipeId, @RequestBody Recipe recipe) {
        try {
            recipeService.modifyRecipe(recipeId, recipe);
        } catch (ResourceNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getLocalizedMessage(), e);
        }
    }

    @DeleteMapping(path = "{recipeId}")
    @Parameter(name="recipeId", schema = @Schema(type = "string", format="uuid"))
    public void deleteRecipe(@PathVariable("recipeId") UUID recipeId) {
        try {
            recipeService.deleteRecipe(recipeId);
        } catch (ResourceNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getLocalizedMessage(), e);
        }
    }
}
