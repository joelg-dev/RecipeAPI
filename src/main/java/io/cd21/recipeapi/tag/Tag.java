package io.cd21.recipeapi.tag;

import io.cd21.recipeapi.entity.AbstractEntity;
import io.cd21.recipeapi.recipe.Recipe;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.persistence.*;

@Entity
@Table(name="tags")
public class Tag extends AbstractEntity {
    @Schema(type="string", example="Vegetarian")
    @Column(nullable = false)
    private String name;

    @ManyToOne()
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    public Tag() {    }

    public Tag(String name) {
        this.name = name;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
