package com.rawcoders.hackaday.Blog.BlogEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lud on 12/15/2014.
 */
public class BlogEntry {

    /**
     * An array of sample (dummy) items.
     */
    public static List<BlogItem> ITEMS = new ArrayList<BlogItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static Map<String, BlogItem> ITEM_MAP = new HashMap<String, BlogItem>();

    static {
        // Add 3 sample items.
        addItem(new BlogItem("1", "Item 1"));
        addItem(new BlogItem("2", "Item 2"));
        addItem(new BlogItem("3", "Item 3"));
    }

    private static void addItem(BlogItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class BlogItem {
        public String id;
        public String url;
        public String description;
        public String imgur;

        public BlogItem(String id, String url) {
            this.id = id;
            this.url = url;
        }

        @Override
        public String toString() {
            return description;
        }
    }
}
