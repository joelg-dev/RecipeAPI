package io.cd21.recipeapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cd21.recipeapi.ingredient.Ingredient;
import io.cd21.recipeapi.recipe.Recipe;
import io.cd21.recipeapi.recipe.RecipeRepository;
import io.cd21.recipeapi.tag.Tag;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = RecipeapiApplication.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class RecipeControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private RecipeRepository repository;

    @Test
    public void whenValidRequest_thenCreateRecipe() throws Exception {

        Recipe testRecipe = getTestRecipe("Baked beans"
                , 3
                , "Just bake them"
                , new String[] {"Beans"}
                , new String[] {});

        postRecipe(testRecipe);

        List<Recipe> recipes = repository.findAll();

        assertThat(recipes).extracting(Recipe::getName).containsOnly("Baked beans");
    }

    @Test
    public void whenGetRecipe_theValidJSON()
            throws Exception {

        Recipe testRecipe = getTestRecipe("Baked beans"
                , 3
                , "Just bake them"
                ,  new String[] {"Beans"}
                , new String[] {});

        repository.save(testRecipe);

        mvc.perform(get("/api/v1/recipes")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$[0].name", is("Baked beans")));
    }


    @Test
    public void whenFilterRecipe_includeIngredients_thenFilteredResult()
            throws Exception {

        postRecipe(getTestRecipe("Baked beans", 3, "Just bake them", new String[] {"Beans", "Baking soda"}, new String[] {}));
        postRecipe(getTestRecipe("Potato mash", 2, "Just mash them", new String[] {"Potatoes", "Butter"}, new String[] {}));
        postRecipe(getTestRecipe("French fries", 1, "Just fry them", new String[] {"Potatoes", "Salt"}, new String[] {}));

        mvc.perform(get("/api/v1/recipes")
                        .param("ingredients", "Potatoes")
                        .param("ingredients-exclude", "Butter")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("French fries")));
    }


    @Test
    public void whenFilterRecipe_includeTags_thenFilteredResult()
            throws Exception {

        postRecipe(getTestRecipe("Meatballs Deluxe", 3, "Just bake them", new String[] {"Beans"}, new String[] {"Delicious"}));
        postRecipe(getTestRecipe("Fancy broccoli", 1, "Make it fancy", new String[] {"Potatoes"}, new String[] {"Vegetarian","Wierd"}));
        postRecipe(getTestRecipe("Green peas", 1, "Just fry them", new String[] {"Potatoes"}, new String[] {"Vegetarian","Delicious"}));

        mvc.perform(get("/api/v1/recipes")
                        .param("tags", "Vegetarian")
                        .param("tags-exclude", "Wierd")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Green peas")));
    }


    @Test
    public void whenFilterRecipe_servings_thenFilteredResult()
            throws Exception {

        postRecipe(getTestRecipe("Meatballs Deluxe", 3, "Just bake them", new String[] {"Beans"}, new String[] {""}));
        postRecipe(getTestRecipe("Broccoli", 1, "Just fry them", new String[] {"Potatoes"}, new String[] {""}));

        mvc.perform(get("/api/v1/recipes")
                        .param("servings", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Broccoli")));
    }


    @Test
    public void whenFilterRecipe_instructions_thenFilteredResult()
            throws Exception {

        postRecipe(getTestRecipe("Meatballs Deluxe", 3, "Just bake them", new String[] {"Beans"}, new String[] {""}));
        postRecipe(getTestRecipe("Broccoli", 1, "Just fry them", new String[] {"Potatoes"}, new String[] {""}));

        mvc.perform(get("/api/v1/recipes")
                        .param("instructions", "bake")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Meatballs Deluxe")));
    }

    @Test
    public void whenFilterRecipe_withTooManyParams_thenStatus400()
            throws Exception {

        String[] tags = new String[20];
        for (int i = 0; i < 20; i ++)
            tags[i] = "tag"+i;

        mvc.perform(get("/api/v1/recipes")
                        .param("tags", String.join(",",tags))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason(containsString("Too many of parameter")));
    }

    @Test
    public void whenSaveRecipe_thenHasCreateDate()
            throws Exception {

        Recipe testRecipe = getTestRecipe("Baked beans"
                , 3
                , "Just bake them"
                ,  new String[] {"Beans"}
                , new String[] {});

        Recipe savedRecipe = repository.save(testRecipe);

        assertThat(savedRecipe.getCreateTime()).isNotNull();
        assertThat(savedRecipe.getUpdateTime()).isNotNull();
    }

    private Recipe getTestRecipe(String name, Integer servings, String instructions, String[] ingredientNames, String[] tagNames){
        Recipe recipe = new Recipe(name, servings, instructions);

        Set<Ingredient> ingredients = new HashSet<>();
        for (String ingredientName : ingredientNames)
            ingredients.add(new Ingredient(ingredientName, 3, "Gram"));

        recipe.setIngredients(ingredients);

        Set<Tag> tags = new HashSet<>();
        for (String tagName : tagNames)
            tags.add(new Tag(tagName));

        recipe.setTags(tags);

        return recipe;
    }

    private void postRecipe(Recipe recipe) throws Exception {
        mvc.perform(post("/api/v1/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recipe)))
                .andExpect(status().isCreated());
    }
}
