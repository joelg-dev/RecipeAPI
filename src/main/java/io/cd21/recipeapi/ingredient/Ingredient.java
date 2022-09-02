package io.cd21.recipeapi.ingredient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.cd21.recipeapi.entity.AbstractEntity;
import io.cd21.recipeapi.recipe.Recipe;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name="ingredients")
public class Ingredient extends AbstractEntity {
    @Schema(type="string", example="Red pepper flakes")
    @NotBlank
    @Column(nullable = false)
    private String name;

    @Schema(type="string", example="Tablespoons")
    @NotBlank
    @Column(nullable = false)
    private String unit;

    @Schema(type="string", example="2")
    @NotBlank
    @Column(nullable = false)
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    public Ingredient() {    }

    public Ingredient(String name, Integer quantity, String unit){
        this.name = name;
        this.unit = unit;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

}
