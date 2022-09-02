package io.cd21.recipeapi.recipe;

import io.cd21.recipeapi.entity.AbstractEntity;
import io.cd21.recipeapi.ingredient.Ingredient;
import io.cd21.recipeapi.tag.Tag;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
@Table(name="recipes")
public class Recipe extends AbstractEntity {
    @NotBlank
    @Schema(type = "integer", example = "Spaghetti and meatballs")
    @Column(nullable = false)
    private String name;

    @NotBlank
    @Schema(type = "integer", example = "3")
    @Size(max = 100)
    @Column(nullable = false)
    private Integer servings;

    @NotBlank
    @Schema(type="string", example="In a large bowl, combine beef with bread crumbs, parsley, Parmesan, egg, garlic, 1 teaspoon salt, and red pepper flakes. Mix until just combined then form into 16 balls.")
    @Column(columnDefinition = "TEXT", nullable = false)
    private String instructions;

    @NotBlank
    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
    private Set<Ingredient> ingredients;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
    private Set<Tag> tags;

    public Recipe() {    }
    public Recipe(String name, Integer servings, String instructions) {
        this.name = name;
        this.instructions = instructions;
        this.servings = servings;
    }
    public Set<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(Set<Ingredient> ingredients) {
        if (ingredients == null)
            return;

        for (Ingredient ingredient : ingredients) {
            ingredient.setRecipe(this);
        }
        this.ingredients = ingredients;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public Set<Tag> getTags() {
        return tags;
    }
    public void setTags(Set<Tag> tags) {
        if (tags == null)
            return;

        for (Tag tag : tags) {
            tag.setRecipe(this);
        }
        this.tags = tags;
    }
}
