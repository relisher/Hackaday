package com.rawcoders.hackaday.Blog.BlogEntry;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.rawcoders.hackaday.Blog.BlogFeedParser;
import com.rawcoders.hackaday.Global;
import com.rawcoders.hackaday.R;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

/**
 * Created by lud on 12/15/2014.
 */
public class BlogEntry extends ArrayAdapter<BlogEntry.BlogItem>{

    /**
     * An array of sample (dummy) items.
     */
    public List<BlogItem> ITEMS = new ArrayList<BlogItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public Map<String, BlogItem> ITEM_MAP = new HashMap<String, BlogItem>();

    private Activity mContext;
    private int layoutResourceId;


    public static String FEED_URL = "http://hackaday.com/blog/feed/?paged="; // + pageID
    public static InputStream FEED_STREAM;

    public BlogEntry(Activity mContext, int layoutResourceId)    {
        super(mContext,layoutResourceId);
        this.mContext = mContext;
        AsyncDownloader ad = new AsyncDownloader();
        Log.d("INIT","Init BlogEntry");
        try {
            Object obj[] = new Object[2];
            obj[0] = this;
            obj[1] = new URL(FEED_URL);
            ad.execute(obj);
        }
        catch(MalformedURLException murl) {
            Log.d("MURL",murl.toString());
        }
        catch(NullPointerException mexc)    {
            Log.d("NULL", mexc.toString());
        }
    }

    public void loadNext(int page)   {
        AsyncDownloader ad = new AsyncDownloader();
        Log.d("INIT","Init BlogEntry");
        try {
            Object obj[] = new Object[2];
            obj[0] = this;
            obj[1] = new URL(FEED_URL + page);
            ad.execute(obj);
        }
        catch(MalformedURLException murl) {
            Log.d("MURL",murl.toString());
        }
        catch(NullPointerException mexc)    {
            Log.d("NULL", mexc.toString());
        }
        Global.mAdapter.notifyDataSetChanged();
    }

    public void setUp()  {
        //Setup async Loading tasks.
    }

    @Override
    public int getCount()   {
        return this.ITEMS.size();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent)   {
        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        View rowView = inflater.inflate(R.layout.blog_list_item, null, true);
        //TextView txtTitle = (TextView) rowView.findViewById(R.id.txt);
        TextView textView1 = (TextView) rowView.findViewById(R.id.textView1);
        TextView textView2 = (TextView) rowView.findViewById(R.id.textView2);
        ImageView imageView1 = (ImageView) rowView.findViewById(R.id.imageView1);
        //ImageView imageView = (ImageView) rowView.findViewById(R.id.img);
        //txtTitle.setText(web[position]);
        textView1.setText(ITEMS.get(position).title);
        textView2.setText(ITEMS.get(position).description);
        //imageView1.setImageBitmap();
        imageView1.setImageResource(R.drawable.ic_action_directions);
        return rowView;
    }

    public void addItem(BlogItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    @Override
    public String toString()    {
        return "STATIC STRING";
    }

    public static class BlogItem{
        public String id;
        public String title;
        public String url;
        public String description;
        public String imgurl;
        public String imageID;


        public BlogItem() {
            this.id = "";
            this.title = "";
            this.url = "";
            this.description = "";
            this.imgurl = "";
            this.imageID = "";
        }

        public BlogItem(String id, String title)    {
            this.id = id;
            this.title = title;
            this.url = "";
            this.description = "";
            this.imgurl = "";
            this.imageID = "";
        }

        @Override
        public String toString() {
            String debugO = "";
            debugO += (id + " " + android.text.Html.fromHtml(description).toString());
            return debugO;
        }
    }

    public static class AsyncDownloader extends AsyncTask<Object , Integer, Integer>    {
        @Override
        protected Integer doInBackground(Object ... obj) {
            Integer totalSize = 0;
            //TODO : Download data here and fill ITEMS
            URL url = (URL)obj[1];
            BlogEntry be = (BlogEntry)obj[0];
            try {
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                // Starts the query
                conn.connect();
                BlogEntry.FEED_STREAM = conn.getInputStream();
                Log.w("ASYNC TASK", "Download Complete");
                BlogFeedParser.parseXML(be, BlogEntry.FEED_STREAM);
                Log.w("ASYNC TASK", "Parsing Complete");
                Log.w("ASYNC TASK",be.ITEMS.get(0).toString());
            }
            catch(IOException iox)  {
                Log.d("IOX", iox.toString());
            }
            catch(XmlPullParserException exc)   {
                Log.d("XMLPullParserException", exc.toString());
            }

            return totalSize;
        }

        @Override
        protected void onPostExecute(Integer result) {
            Log.w("onPostExecute","onPostExecute Ran");

            Global.mAdapter.notifyDataSetChanged();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
        }
    }
}
