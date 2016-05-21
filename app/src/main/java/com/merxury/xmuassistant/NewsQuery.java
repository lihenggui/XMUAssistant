package com.merxury.xmuassistant;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by deng on 2016/5/16.
 * Modified by lihenggui on 2016/5/17
 * 获取网页新闻，存放在一个HashMap中，三个key分别是url,title,context
 * 获取到了数据之后可以使用HashMap中的get(KEY)来获取数据
 * ！！！！方法未测试！！！！需要检验！！！
 * 要使用getNewsInfo()方法来获取数据
 */
public class NewsQuery {
    /**
     * 获取网页上的文章标题
     * url为传入的网页地址
     * 返回文章标题字符串
     */

    public static String url = "http://jwc.xmu.edu.cn/"; //连接需要抓取网站的URL
    private static HashMap<String, String> newsInfo = new HashMap<>();

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
            //根据指定的cssQuery语句抓取网页内元素，这里抓取的是除了置顶以外的第一条新闻
            Elements ListDiv = doc.select("#wp_news_w13 > table > tbody > tr:nth-child(3) > td:nth-child(2) > table > tbody > tr > td:nth-child(1) > a");
            //抓取子页面的URL，然后打开，要删除最后一个斜杠，否则会出错
            String newsURL = url.substring(0, url.length() - 1) + ListDiv.attr("herf");
            //获得页面的标题
            String newsTitle = ListDiv.text();
            newsInfo.put("URL", newsURL);
            newsInfo.put("Title", newsTitle);

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
            doc = Jsoup.connect(newsInfo.get("url")).get();
            //选择整个页面的新闻部分，抓取所有内容
            Elements ListDiv = doc.select("#newsinfo > div > div > div > div");
            for (Element element : ListDiv) {
                //新闻中的文字都有标题
                Elements textInfos = element.getElementsByAttribute("span");
                //获取每一行的内容
                for (Element textInfo : textInfos) {
                    text += textInfo.text().trim();
                }
            }
            newsInfo.put("context", text);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return newsInfo;
    }

    public static void getNews() {
        //获取新闻标题+内容的方法
        getTitle();
        Information();
    }
}
