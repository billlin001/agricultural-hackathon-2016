package tw.bill.crawler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bill33 on 2016/8/3.
 */
public class ICookCrawler {
    private static final String HOST = "https://icook.tw";
    private static final String CATEGORY_URL = "https://icook.tw/categories";
    private static final String LINK_SELECTOR = "div.media-body.card-info a";
    private static final String RECIPE_SELECTOR = "div.ingredients .group.group-0 .ingredient-name";
    private static final String RECIPE_NAME_SELECTOR = "article.recipe-detail h1";

    public void findByCategoryId(long categoriesId) {
        try {
            CategoryDto category = getCategory(categoriesId);
            getRecipes(category);
            saveFile(category.getRecipes());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private CategoryDto getCategory(long categoryId) throws IOException {
        CategoryDto result = new CategoryDto();
        result.setId(categoryId);

        int index = 2;
        JSoupHelper helper = new JSoupHelper(CATEGORY_URL + "/" + categoryId);
        while (helper.hasContent(LINK_SELECTOR)) {
            List<String> links = helper.getLinks(LINK_SELECTOR, HOST);
            result.getLinks().addAll(links);
            helper = new JSoupHelper(CATEGORY_URL + "/" + categoryId + "?page=" + index++);
            if(index > 2) break; // TODO Bill, for test
        }

        return result;
    }

    private void getRecipes(CategoryDto category) throws IOException {
        category.getLinks().parallelStream().forEach( item -> {
            JSoupHelper helper = null;
            try {
                helper = new JSoupHelper(item);
                Long id = Long.valueOf(item.substring(item.lastIndexOf("/") + 1));
                String name = helper.getValue(RECIPE_NAME_SELECTOR);
                List<String> ingredients = helper.getValues(RECIPE_SELECTOR);

                RecipeDto recipe = new RecipeDto();
                recipe.setId(id);
                recipe.setName(name);
                recipe.setIngredients(ingredients);

                category.getRecipes().add(recipe);
                System.out.println(recipe);

            } catch (IOException e) {
                e.printStackTrace();
            }
        });

//        for(String item : category.getLinks()) {
//            JSoupHelper helper = new JSoupHelper(item);
//
//            Long id = Long.valueOf(item.substring(item.lastIndexOf("/") + 1));
//            String name = helper.getValue(RECIPE_NAME_SELECTOR);
//            List<String> ingredients = helper.getValues(RECIPE_SELECTOR);
//
//            RecipeDto recipe = new RecipeDto();
//            recipe.setId(id);
//            recipe.setName(name);
//            recipe.setIngredients(ingredients);
//
//            category.getRecipes().add(recipe);
//            System.out.println(recipe);
//        }
    }

    private void saveFile(List<RecipeDto> recipes) throws IOException {
        System.out.println("saveFile Start...");

        BufferedWriter bw = new BufferedWriter(new FileWriter(new File("icook.csv")));
        recipes.forEach(item -> {
            try {
                System.out.println(item.toString() + "\n");
                bw.write(item.toString() + "\n");

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        bw.close();

        System.out.println("saveFile Finish...");
    }

    public static void main(String[] args) throws IOException {
        new ICookCrawler().findByCategoryId(352L);
    }
}
