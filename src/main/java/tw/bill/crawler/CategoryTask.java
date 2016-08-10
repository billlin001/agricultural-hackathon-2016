package tw.bill.crawler;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by bill33 on 2016/8/7.
 */
public class CategoryTask implements Callable<CategoryDto> {
        private static final String HOST = "https://icook.tw";
        private static final String CATEGORY_URL = "https://icook.tw/categories";
        private static final String CATEGORY_NAME_SELECTOR = "div.name";
        private static final String LINK_SELECTOR = "div.media-body.card-info a";

    private Long categoryId;

    public CategoryTask(Long id) {
        this.categoryId = id;
    }

    @Override
    public CategoryDto call() throws Exception {
        CategoryDto result = new CategoryDto();
        result.setId(categoryId);

        int index = 2;
        JSoupHelper helper = new JSoupHelper(CATEGORY_URL + "/" + categoryId);
        while (helper.hasContent(LINK_SELECTOR)) {
            String name = helper.getValue(CATEGORY_NAME_SELECTOR);
            List<String> links = helper.getLinks(LINK_SELECTOR, HOST);

            result.setId(categoryId);
            result.setName(name);
            result.getLinks().addAll(links);

            helper = new JSoupHelper(CATEGORY_URL + "/" + categoryId + "?page=" + index++);
//            if(index > 3) break; // TODO Bill, for test
        }

        return result;
    }
}
