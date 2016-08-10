package tw.bill.crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by bill33 on 2016/8/6.
 */
public class JSoupHelper {
    private Document document;

    public JSoupHelper(String url) throws IOException {
        System.out.println(String.format("url: %s", url));
        document = Jsoup.connect(url).get();
    }

    public String getValue(String selector) {
        Elements elements = document.select(selector);
        Element element = elements.first();
        return element.html();
    }

    public List<String> getValues(String selector) {
        List<String> results = new ArrayList<>();
        Elements elements = document.select(selector);
        elements.iterator().forEachRemaining(item -> results.add(item.html()));
        return results;
    }

    public List<String> getLinks(String selector, String host) {
        List<String> results = new ArrayList<>();
        Elements elements = document.select(selector);
        elements.iterator().forEachRemaining(item -> results.add(host + item.attributes().get("href")));
        return results;
    }

    public Element getElement(String selector, int position) {
        Elements elements = document.select(selector);
        return elements.get(position);
    }

    public List<String> findLinkByElement(Element element, String selector, String host) {
        List<String> results = new ArrayList<>();
        Elements elements = element.select(selector);
        elements.iterator().forEachRemaining(item -> results.add(host + item.attributes().get("href")));
        return results;
    }

    public boolean hasContent(String selector) {
        List<String> results = new ArrayList<>();
        Elements elements = document.select(selector);
        return elements.size() > 0 ? true : false;
    }
}
