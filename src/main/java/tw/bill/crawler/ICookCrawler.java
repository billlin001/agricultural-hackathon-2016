package tw.bill.crawler;

import org.jsoup.nodes.Element;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/**
 * Created by bill33 on 2016/8/3.
 */
public class ICookCrawler {
    private static final int THREAD_SIZE = 10;

    public void findByCategoryId(Long[] categoriesIds) {
        try {
            List<CategoryDto> categories = getCategory(categoriesIds);
            for(CategoryDto item : categories) {
                getRecipes(item);
                saveFile(item);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private List<CategoryDto> getCategory(Long[] categoryIds) throws IOException, ExecutionException, InterruptedException {
        System.out.println("getCategory start...");
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_SIZE);
        List<FutureTask<CategoryDto>> tasks = new ArrayList<>();
        FutureTask<CategoryDto> task;
        List<CategoryDto> results = new ArrayList<>();

        for (Long item : categoryIds) {
            task = new FutureTask<CategoryDto>(new CategoryTask(item));
            tasks.add(task);
            executor.execute(task);
        }

        while(true) {
            boolean finish = true;
            for (FutureTask<CategoryDto> item : tasks) {
                if(!item.isDone()) {
                    finish = false;
                    Thread.sleep(1000);
                    break;
                }
            }

            if(finish) {
                for (FutureTask<CategoryDto> item : tasks) {
                    results.add(item.get());
                }
                break;
            }
        }

        System.out.println("getCategory end");
        return results;
    }

    private void getRecipes(CategoryDto category) throws IOException, InterruptedException, ExecutionException {
        System.out.println("getRecipes start");
        List<String> links = category.getLinks();

        ExecutorService executor = Executors.newFixedThreadPool(THREAD_SIZE);
        List<FutureTask<RecipeDto>> tasks = new ArrayList<>();
        FutureTask<RecipeDto> task;
        List<RecipeDto> results = new ArrayList<>();

        for (String item : links) {
            task = new FutureTask<RecipeDto>(new RecipeTask(item, category));
            tasks.add(task);
            executor.execute(task);
        }

        while (true) {
            boolean finish = true;
            for (FutureTask<RecipeDto> item : tasks) {
                if (!item.isDone()) {
                    finish = false;
                    Thread.sleep(1000);
                    break;
                }
            }

            if (finish) {
                for (FutureTask<RecipeDto> item : tasks) {
                    results.add(item.get());
                }
                break;
            }
        }

        category.getRecipes().addAll(results);
        System.out.println("getRecipes end");
    }

    private void saveFile(CategoryDto category) throws IOException {
        System.out.println("saveFile Start...");

        BufferedWriter bw = new BufferedWriter(new FileWriter(new File("icook.csv"), true));

        List<RecipeDto> recipes = category.getRecipes();
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
        long startTime = System.currentTimeMillis();
        Long[] categoryUrls = getCategoryLinks();
        new ICookCrawler().findByCategoryId(categoryUrls);
        System.out.println("spend time(s): " + (System.currentTimeMillis() - startTime) / 1000);
    }

    private static Long[] getCategoryLinks() throws IOException {
        String url = "https://icook.tw/categories?ref=homepage";
        String host = "https://icook.tw";
        String elementSelector = "ul.main-menu.clearfix";
        String linksSelector = "ul.list-group.hidden-xs li.list-group-item a";

        JSoupHelper helper = new JSoupHelper(url);
        Element element = helper.getElement(elementSelector, 0);
        List<String> categoryUrls = helper.findLinkByElement(element, linksSelector, host);
        System.out.println(categoryUrls);
        System.out.println(categoryUrls.size());

        Long[] results = new Long[categoryUrls.size()];
        for(int i=0; i<results.length; i++) {
            String categoryUrl = categoryUrls.get(i);
            Long categoryId = Long.valueOf(categoryUrl.substring(categoryUrl.lastIndexOf("/") + 1));
            results[i] = categoryId;
        }

        return results;
    }
}
