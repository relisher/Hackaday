package com.rawcoders.hackaday.Blog.BlogEntry;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import com.rawcoders.hackaday.Blog.BlogFeedParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
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

    public static String FEED_URL = "http://hackaday.com/blog/feed/?paged="; // + pageID
    public static InputStream FEED_STREAM;

    static {
        AsyncDownloader ad = new AsyncDownloader();
        try {
            ad.execute(new URL(FEED_URL));
        }
        catch(MalformedURLException murl) {
            Log.d("MURL",murl.toString());
        }
    }

    public static void setUp()  {
        //Setup async Loading tasks.
    }

    public static void addItem(BlogItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class BlogItem {
        public String id;
        public String title;
        public String url;
        public String description;
        public String imgurl;

        public BlogItem()   {
            this.id = "";
            this.title = "";
            this.url = "";
            this.description = "";
            this.imgurl = "";
        }

        public BlogItem(String id, String url) {
            this.id = id;
            this.url = url;
        }

        @Override
        public String toString() {
            String debugO = "";
            debugO += (id + title + url + imgurl);
            return debugO;
        }
    }

    public static class AsyncDownloader extends AsyncTask<URL, Integer, Integer>    {
        protected Integer doInBackground(URL... urls) {
            Integer totalSize = 0;
            //TODO : Download data here and fill ITEMS
            try {
                HttpURLConnection conn = (HttpURLConnection) urls[0].openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                // Starts the query
                conn.connect();
                BlogEntry.FEED_STREAM = conn.getInputStream();
                Log.w("ASYNC TASK", "Download Complete");
                BlogFeedParser.parseXML(BlogEntry.FEED_STREAM);
                
            }
            catch(IOException iox)  {
                Log.d("IOX", iox.toString());
            }
            catch(XmlPullParserException exc)   {
                Log.d("XMLPullParserException", exc.toString());
            }

            return totalSize;
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPostExecute(Long result) {
            Log.w("ASYNC TASK", "Download Complete");
            try {
                BlogFeedParser.parseXML(BlogEntry.FEED_STREAM);
            }
            catch(IOException iox)  {
                Log.d("IOX", iox.toString());
            }
            catch(XmlPullParserException exc)   {
                Log.d("XMLPullParserException", exc.toString());
            }
        }
    }
}
