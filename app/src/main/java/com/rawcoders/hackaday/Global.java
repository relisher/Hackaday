package com.rawcoders.hackaday;

import android.app.Activity;
import android.content.Context;
import com.rawcoders.hackaday.Blog.BlogEntry.BlogEntry;

/**
 * Created by lud on 1/4/2015.
 */
public class Global {

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    public static BlogEntry mAdapter = null;

    static {

    }

    public static void initGlobal(Activity context)   {
        mAdapter = new BlogEntry(context,R.layout.blog_list_item);
    }

    public static void clearGlobal() {

    }
}
