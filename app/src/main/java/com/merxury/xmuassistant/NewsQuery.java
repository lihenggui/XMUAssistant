package com.merxury.xmuassistant;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Rewrited by lihenggui on 2016/6/24
 * 重写了查询新闻的方法
 * 使用SQLite来存储数据
 * 优化了获取网页数据的逻辑，等待时间减少了40%
 * <p/>
 * 方法介绍
 * getAllNewsFromServer：从服务器端获取新闻信息并写入到数据库中，返回的是一个布尔值
 * getAllNews从数据库中读取获取新闻的方法，返回的是一个List<news>集合类
 */
public class NewsQuery extends SQLiteOpenHelper {
    public static final String CREATE_TABLE = "create table news("
            + "id integer primary key not null, "
            + "title text, "
            + "url text, "
            + "content text);";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "data.db";
    /**
     * 获取网页上的文章标题
     * url为传入的网页地址
     * 返回文章标题字符串
     */
    public static String url = "http://jwc.xmu.edu.cn/"; //连接需要抓取网站的URL
    private static HashMap<String, String> newsInfo = new HashMap<>();
    private Context mContext;
    private SQLiteDatabase db;

    //构造函数
    NewsQuery(Context context, String name, SQLiteDatabase.CursorFactory cursorFactory, int version) {
        super(context, name, cursorFactory, version);
        mContext = context;
    }

    //使用一个HashMap来存储数据
    private static boolean getTitle() {

        try {
            //连接到选定的网站，获取整个网页数据
            Document doc = Jsoup.connect(url).timeout(60000).get();
            //根据指定的Xselector语句抓取网页内元素，这里抓取的是除了置顶以外的五条新闻
            for (int i = 1; i <= 5; i++) {
                Elements ListDiv = doc.select("#wp_news_w13 > table > tbody > tr:nth-child(" + (i + 2) + ") > td:nth-child(2) > table > tbody > tr > td:nth-child(1) > a");
                //抓取子页面的URL，然后打开，要删除最后一个斜杠，否则会出错
                String newsURL = url.substring(0, url.length() - 1) + ListDiv.attr("href").trim();
                //获得页面的标题+url
                String newsTitle = ListDiv.text();
                newsInfo.put("URL" + i, newsURL);
                newsInfo.put("Title" + i, newsTitle);
            }

            for (int i = 1; i <= 5; i++) {
                //获取新闻失败，返回失败值
                if (!Information(i)) {
                    return false;
                }
            }
            return true;


        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }


    private static boolean Information(int order) {
        Document doc;
        try {
            //从HashMap中调取需要读取的页面URL
            doc = Jsoup.connect(newsInfo.get("URL" + order)).get();
            //选择整个页面的新闻部分，抓取所有内容
            Elements ListDiv = doc.select("#newsinfo > div > div > div > div");
            String content = ListDiv.text();
            newsInfo.put("content" + order, content);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean getAllNewsInfoFromServer() {
        Cursor cursor;
        if (getTitle()) {
            //获取五个新闻内容
            db = getWritableDatabase();
            cursor = db.query("news", null, null, null, null, null, null);
            for (int i = 1; i <= 5; i++) {
                if (cursor.moveToNext()) {
                    rewriteNews(i);
                } else {
                    writeNews(i);
                }
            }
            cursor.close();
            return true;
        } else {
            return false;
        }

    }

    //从数据库中读取获取新闻的方法，返回的是一个List集合类
    public List<News> getAllNews() {
        db = getReadableDatabase();
        Cursor cursor = db.query("news", null, null, null, null, null, null);
        List<News> newsList = new ArrayList<>();
        while (cursor.moveToNext()) {
            News news = new News();
            news.setTitle(cursor.getString(cursor.getColumnIndex("title")));
            news.setUrl(cursor.getString(cursor.getColumnIndex("url")));
            news.setContent(cursor.getString(cursor.getColumnIndex("content")));
            newsList.add(news);
        }
        cursor.close();
        return newsList;

    }

    //第一次运行，往数据库写入新闻信息
    private void writeNews(int order) {
        db = getWritableDatabase();
        db.execSQL("insert into news(id,title,url,content) values("
                + order + ", '"
                + newsInfo.get("title" + order) + "', '"
                + newsInfo.get("url" + order) + "',' "
                + newsInfo.get("content" + order) + "');");
    }

    //数据库内已有新闻信息，使用sql语句的update语句来覆写新闻信息
    private void rewriteNews(int order) {
        db = getWritableDatabase();
        db.execSQL("update news set title='"
                + newsInfo.get("Title" + order) + "', url='"
                + newsInfo.get("URL" + order) + "',content=' "
                + newsInfo.get("content" + order) + "' where id="
                + order + ";");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO 创建数据库后，对数据库的操作
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO 更改数据库版本的操作
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        // TODO 每次成功打开数据库后首先被执行
    }

    //存放新闻的私有类
    public class News {
        private String title;
        private String url;
        private String content;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}

