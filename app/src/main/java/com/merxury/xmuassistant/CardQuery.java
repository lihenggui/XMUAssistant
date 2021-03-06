package com.merxury.xmuassistant;

import android.content.Context;
import android.content.SharedPreferences;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by lihen on 2016/5/20.
 * 查询学生余额信息的类定义
 */
public class CardQuery {
    public static OkHttpClient client;
    private String money;
    private String studentName;
    private String username;
    private String password;
    private SharedPreferences pref;
    private Context mContext;

    CardQuery(String username, String password, Context mContext) {
        this.username = username;
        this.password = password;
        this.mContext = mContext;
    }

    public double getMoney() {
        ClearableCookieJar cookieJar =
                new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(mContext));
        client = new OkHttpClient.Builder()
                .cookieJar(cookieJar)
                .build();
        try {
            String ixmures = sendPost("http://idstar.xmu.edu.cn/amserver/UI/Login", username, password);
            Document ixmudoc = Jsoup.parse(ixmures);
            //获得一系列的子标签,这里的pf1041特指学生卡信息的部分
            Elements elementsDiv = ixmudoc.getElementsByAttributeValue("id", "pf1041");
            //余额使用font属性来高亮，所以选择font属性就可以抓取到余额信息
            //余额信息在最后一个元素里，使用.last输出
            Elements moneyLeft = elementsDiv.select("font");
            //保存到money变量中
            money = moneyLeft.last().text().substring(0, moneyLeft.last().text().length() - 1);
            //选择姓名所在的区域
            Elements nameElems = ixmudoc.select("#pf1037 > div > div.portletContent > table > tbody > tr > td:nth-child(2) > div > ul > li:nth-child(1)");
            String tempName = nameElems.text();
            //删除末尾，留下有用的信息
            studentName = tempName.substring(0, tempName.length() - 5);
            SaveNameAndMoney(studentName, Double.valueOf(money));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Double.valueOf(money.substring(0, money.length() - 1));
    }

    private String sendPost(String url, String username, String password) throws IOException {
        //将登录的参数添加到HashMap中
        HashMap<String, String> params = new HashMap<>();
        params.put("IDToken0", "");
        params.put("IDToken1", username);
        params.put("IDToken2", password);
        params.put("IDButton", "登录");
        params.put("goto", "http://i.xmu.edu.cn");
        params.put("inputCode", "");
        params.put("gx_charset", "UTF-8");
        //初始化Builder
        FormBody.Builder builder = new FormBody.Builder();
        //将参数添加至Builder中
        for (Map.Entry<String, String> entry : params.entrySet()) {
            builder.add(entry.getKey(), entry.getValue());
        }
        //创建一个请求对象
        RequestBody formBody = builder.build();
        //发出请求
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2700.0 Safari/537.36")
                .build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            throw new IOException("Unexpected code:" + response);
        }
    }

    //将获取到的学生名字和金额保存到配置文件中
    private void SaveNameAndMoney(String name, double money) {
        pref = mContext.getSharedPreferences("data", Context.MODE_PRIVATE);
        // 创建SharedPreferences.Editor对象，用于存储数据修改
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("studentName", name);
        editor.putString("CardMoney", String.valueOf(money));
        editor.apply();
    }


}

