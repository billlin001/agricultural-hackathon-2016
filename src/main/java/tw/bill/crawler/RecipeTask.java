package tw.bill.crawler;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by bill33 on 2016/8/7.
 */
public class RecipeTask implements Callable<RecipeDto> {
    private static final String RECIPE_SELECTOR = "div.ingredients .group.group-0 .ingredient-name";
    private static final String RECIPE_NAME_SELECTOR = "article.recipe-detail h1";

    private final String link;
    private final CategoryDto category;

    public RecipeTask(String link, CategoryDto category) {
        this.link = link;
        this.category = category;
    }

    @Override
    public RecipeDto call() throws Exception {
        RecipeDto recipe = new RecipeDto();

        JSoupHelper helper = null;
        try {
            helper = new JSoupHelper(link);
            Long id = Long.valueOf(link.substring(link.lastIndexOf("/") + 1));
            String name = helper.getValue(RECIPE_NAME_SELECTOR);
            List<String> ingredients = helper.getValues(RECIPE_SELECTOR);

            recipe.setId(id);
            recipe.setName(name);
            recipe.setIngredients(ingredients);
            recipe.setCategoryId(category.getId());
            recipe.setCategoryName(category.getName());

            //category.getRecipes().add(recipe);
            System.out.print(recipe);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return recipe;
    }
}
