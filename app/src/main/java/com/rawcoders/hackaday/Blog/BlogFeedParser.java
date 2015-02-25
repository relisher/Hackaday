package com.rawcoders.hackaday.Blog;

import android.content.ClipData;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.util.Xml;
import com.rawcoders.hackaday.Blog.BlogEntry.BlogEntry;
import com.rawcoders.hackaday.Global;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

/**
 * Created by lud on 12/22/2014.
 */
public class BlogFeedParser {
    //TODO : write static methods to parse blog feeds and fill up ITEMS :/
    private static String ns = null;
    public static void parseXML(List<BlogEntry.BlogItem> be, InputStream is) throws IOException, XmlPullParserException{
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(is, null);
            parser.nextTag();
            boolean channelFound = false;
            Log.w("InputStream",is.toString());
            //parser.require(XmlPullParser.START_TAG, ns, "channel");
            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    Log.e("TEST","continue");
                    continue;
                }
                String name = parser.getName();
                Log.e("PARSER NAME",name);
                // Starts by looking for the entry tag
                if (name.equals("channel")) {
                    Log.w("FOUND CHANNEL",name);
                    channelFound = true;
                }
                else   {
                    Log.e("SKIPPING IN CHANNEL : ",name);
                    skip(parser);
                }
                if(channelFound)   {
                    while (parser.next() != XmlPullParser.END_TAG) {
                        if (parser.getEventType() != XmlPullParser.START_TAG) {
                            continue;
                        }
                        String n = parser.getName();
                        if(n.equals("item"))    {
                            Log.e("ADDING",n);
                            be.add(readEntry(be.size(), parser));
                        }
                        else {
                            Log.e("SKIPING IN TITLE : ",n);
                            skip(parser);
                        }
                    }
                }

            }
    }

    private static BlogEntry.BlogItem readEntry(int oldId, XmlPullParser parser)  throws IOException, XmlPullParserException  {
        BlogEntry.BlogItem blg = new BlogEntry.BlogItem();
        blg.id += (oldId + 1);
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("title")) {
                blg.title = readTitle(parser);
            } else if (name.equals("description")) {
                blg.description = readSummary(parser);
            } else if (name.equals("link")) {
                blg.url = readLink(parser);
            } else if(name.equals("media:thumbnail")) {
                blg.imgurl = readImageURL(parser);
                Log.w("Image URL : ", blg.imgurl);
                blg.imageID = blg.imgurl.substring(blg.imgurl.length() - 13, blg.imgurl.length() - 6);
                URL url;
                InputStream ps;
                url = new URL(blg.imgurl);
                ps = (InputStream) url.getContent();
                blg.thumbnail = Drawable.createFromStream(ps, "src");
                Log.w("Image data : ", blg.thumbnail.toString());
                Log.w("IMG ID", blg.imageID);
                //Global.mAdapter.notifyDataSetChanged();
            }
            else {
                skip(parser);
            }
        }
        Log.e("BLOG DATA", blg.toString());
        return blg;
    }

    private static void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

    // Processes title tags in the feed.
    private static String readTitle(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "title");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "title");
        return title;
    }

    // Processes link tags in the feed.
    private static String readLink(XmlPullParser parser) throws IOException, XmlPullParserException {
        String link = "";
        parser.require(XmlPullParser.START_TAG, ns, "link");
        link = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "link");
        return link;
    }

    // Processes summary tags in the feed.
    private static String readSummary(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "description");
        String summary = readText(parser);
        summary = android.text.Html.fromHtml(summary).toString();
        parser.require(XmlPullParser.END_TAG, ns, "description");
        return summary;
    }

    private static String readImageURL(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "media:thumbnail");
        String link = "";
        String tag = parser.getName();
        String relType = parser.getAttributeValue(null, "url");
        if (tag.equals("media:thumbnail")) {
            link = parser.getAttributeValue(null, "url");
            parser.nextTag();
        }
        parser.require(XmlPullParser.END_TAG, ns, "media:thumbnail");
        return link;
    }

    // For the tags title and summary, extracts their text values.
    private static String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }
}
