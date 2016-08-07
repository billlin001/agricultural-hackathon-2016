package tw.bill.crawler;

import org.hamcrest.Matchers;
import org.hamcrest.collection.IsArrayContainingInOrder;
import org.hamcrest.collection.IsCollectionWithSize;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

/**
 * Created by bill33 on 2016/8/6.
 */
public class JSoupHelperTest {
    JSoupHelper target;

    @Before
    public void setUp() throws Exception {
        target = new JSoupHelper("https://icook.tw/categories/352");
    }

    @Test
    public void testGetValue() throws Exception {
        String selector = "div.media-body.card-info a";
        String actual = target.getValue(selector);
        String expected = "[減肥食譜] 地瓜泥沙拉";
        assertEquals(expected, actual);
    }

    @Test
    public void testGetValues() throws Exception {
        target = new JSoupHelper("https://icook.tw/recipes/162250");
        String selector = "div.ingredients .group.group-0 .ingredient-name";
        List<String> actuals = target.getValues(selector);
        System.out.println(actuals);
        assertThat(actuals, hasSize(8));
        assertThat(actuals, containsInAnyOrder("馬斯卡彭起司", "A蛋白", "A白砂糖（蛋白打發用）", "B蛋黃", "B白砂糖", "消化餅", "無糖可可粉（表面用）", "動物性鮮奶油（可省略）"));
    }

    @Test
    public void testGetLinks() throws Exception {
        String selector = "div.media-body.card-info a";
        String host = "https://icook.tw";

        List<String> actual = target.getLinks(selector, host);
        String expected = "https://icook.tw/recipes/125760";

        assertThat(actual, hasSize(12));
        assertThat(actual, hasItem(expected));
    }

    @Test
    public void whenNoSelectValues_shouldEmptyList() throws Exception {
        target = new JSoupHelper("https://icook.tw/categories/352?page=1000");
        String selector = "div.ingredients .group.group-0 .ingredient-name";
        List<String> actuals = target.getValues(selector);
        System.out.println(actuals);
        assertThat(actuals, hasSize(0));
    }
}