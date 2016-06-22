package com.merxury.xmuassistant;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by deng on 2016/5/16.
 * Modified by lihenggui on 2016/5/17
 * 获取网页新闻，存放在一个HashMap中，三个key分别是url,title,context
 * 获取到了数据之后可以使用HashMap中的get(KEY)来获取数据
 * 测试通过，可以获取到新闻url+标题+内容
 * 哈哈哈哈哈哈哈
 * 要使用getNewsInfo()方法来获取数据
 * 返回一个HashMap
 * .get("URL")获取新闻地址
 * .get("Title")获取标题
 * .get("content")获取新闻内容
 */
public class NewsQuery {
    /**
     * 获取网页上的文章标题
     * url为传入的网页地址
     * 返回文章标题字符串
     */

    public static String url = "http://jwc.xmu.edu.cn/"; //连接需要抓取网站的URL
    private static HashMap<String, String> newsInfo = new HashMap<>();
    private static int i = 1;

    public static HashMap<String, String> getNewsInfo() {
        //如果HashMap中无数据的话，获取新闻，然后返回一个HashMap
        if (newsInfo.isEmpty()) {
            getNews();
        }
        return newsInfo;
    }

    //使用一个HashMap来存储数据
    public static HashMap<String, String> getTitle() {

        try {
            //连接到选定的网站，获取整个网页数据
            Document doc = Jsoup.connect(url).timeout(60000).get();
            //根据指定的Xselector语句抓取网页内元素，这里抓取的是除了置顶以外的第一条新闻
            Elements ListDiv = doc.select("#wp_news_w13 > table > tbody > tr:nth-child(" + (i + 2) + ") > td:nth-child(2) > table > tbody > tr > td:nth-child(1) > a");
            //抓取子页面的URL，然后打开，要删除最后一个斜杠，否则会出错
            String newsURL = url.substring(0, url.length() - 1) + ListDiv.attr("href").trim();
            //获得页面的标题
            String newsTitle = ListDiv.text();
            newsInfo.put("URL" + i, newsURL);
            newsInfo.put("Title" + i, newsTitle);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return newsInfo;
    }

    /**
     * 获取指定网页文章的内容
     * 存入HashMap中
     * 返回文章内容
     */
    public static HashMap<String, String> Information() {
        Document doc;
        String text = "";
        try {
            //从HashMap中调取需要读取的页面URL
            doc = Jsoup.connect(newsInfo.get("URL" + i)).get();
            //选择整个页面的新闻部分，抓取所有内容
            Elements ListDiv = doc.select("#newsinfo > div > div > div > div");
            String content = ListDiv.text();
            newsInfo.put("content" + i, content);
            i++;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return newsInfo;
    }

    public static void getNews() {
        //获取n个新闻标题+内容的方法
        for (int a = 0; a < 5; a++) {
            getTitle();
            Information();
        }
    }
}
