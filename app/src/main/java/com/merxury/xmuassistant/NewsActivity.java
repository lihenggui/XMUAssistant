package com.merxury.xmuassistant;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by deng on 2016/5/16.
 * 获取网页新闻
 */
public class NewsActivity {
    /**
     * 获取网页上的文章标题
     * url为传入的网页地址
     * 返回文章标题字符串
     */
    public static String Title(String url) {
        String linkTitle = "";
        try {
            Document doc = Jsoup.connect(url).timeout(60000).get();
            Elements ListDiv = doc.getElementsByAttributeValue("class", "postTitle");
            for (Element element : ListDiv) {
                Elements links = element.getElementsByTag("a");
                for (Element link : links) {
                    linkTitle = link.toString();//获取URL标题
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return linkTitle;
    }

    /**
     * 获取指定网页文章的内容
     * url为传入的网页地址
     * 返回文章内容
     */
    public static String Information(String url) {
        Document doc;
        String content = "";
        try {
            doc = Jsoup.connect(url).get();
            Elements ListDiv = doc.getElementsByAttributeValue("class", "postBody");
            for (Element element : ListDiv) {
                content = element.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }
}
