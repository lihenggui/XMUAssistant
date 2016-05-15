package com.merxury.xmuassistant;

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
 * Created by lihen on 2016/5/15.
 * 查询电费的方法
 */
public class ElecQuery {
    public static OkHttpClient client = MainActivity.client;
    private String url = "http://elec.xmu.edu.cn/PdmlWebSetup/Pages/SMSMain.aspx";//请求url的地址
    private String xiaoqu;//请求小区的地址
    private String lou;//楼号
    private String roomID;//房间号

    public ElecQuery(String xiaoqu, String lou, String roomID) {
        this.xiaoqu = xiaoqu;
        this.lou = lou;
        this.roomID = roomID;
    }

    //getters and setters 写入&读取各种信息
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRoomID() {
        return roomID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public String getlou() {
        return lou;
    }

    public void setlou(String lou) {
        this.lou = lou;
    }

    public String getXiaoqu() {
        return xiaoqu;
    }

    public void setXiaoqu(String xiaoqu) {
        this.xiaoqu = xiaoqu;
    }

    //请求整个电费页面
    public String getElec() throws IOException {
        //电费查询有隐藏参数，放在页面源代码中，所以要申请读取一次页面抓取这些隐藏的信息
        Request request1 = new Request.Builder()
                .url(url)
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2700.0 Safari/537.36")
                .build();
        Response responseInfo = client.newCall(request1).execute();
        Document elecParams = Jsoup.parse(responseInfo.body().string());
        String ViewState = elecParams.select("#__VIEWSTATE").attr("value");
        String EventValidation = elecParams.select("#__EVENTVALIDATION").attr("value");
        String subCallBackState = elecParams.select("#dxgvSubInfo_CallbackState").attr("value");
        String elecCallBackState = elecParams.select("#dxgvElec_CallbackState").attr("value");

        //得到网页余额然后返回

        //设置时间戳，发送到服务器的时间为1970年到此刻的毫秒数
        //传入查询的页面参数
        HashMap<String, String> params = new HashMap<>();
        params.put("drxiaoqu", xiaoqu);
        params.put("drlou", lou);
        params.put("txtRoomid", roomID);
        params.put("__VIEWSTATE", ViewState);
        params.put("__EVENTVALIDATION", EventValidation);
        params.put("dxdateStart_Raw", String.valueOf(System.currentTimeMillis() - 86400000));//查询的开始时间戳，这里就设置为和结束时间相差一天，方便抓取
        params.put("dxdateEnd_Raw", String.valueOf(System.currentTimeMillis()));//查询的结束时间戳
        params.put("dxdateStart_DDDWS", "0:0:-1:-10000:-10000:0:-10000:-10000:1");
        params.put("dxdateStart_DDD_C_FNPWS", "0:0:-1:-10000:-10000:0:0px:-10000:1");
        params.put("dxdateEnd_DDDWS", "0:0:-1:-10000:-10000:0:-10000:-10000:1");
        params.put("dxdateEnd_DDD_C_FNPWS", "0:0:-1:-10000:-10000:0:0px:-10000:1");
        params.put("dxgvSubInfo$CallbackState", subCallBackState);
        params.put("dxgvElec$CallbackState", elecCallBackState);
        params.put("DXScript", "1_42,1_74,2_22,2_29,1_46,1_54,2_21,1_67,1_64,2_16,2_15,1_52,1_65,3_7");
        FormBody.Builder builder = new FormBody.Builder();
        //将参数添加至Builder中
        for (Map.Entry<String, String> entry : params.entrySet()) {
            builder.add(entry.getKey(), entry.getValue());
        }
        RequestBody formBody = builder.build();
        //发出请求
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2700.0 Safari/537.36")
                .build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            //如果请求成功返回，那么分析网页内容
            Document elecDoc = Jsoup.parse(response.body().string());
            //得到网页余额然后返回
            Elements elecElem = elecDoc.select("#lableft");
            System.out.println(elecElem);
            return elecElem.text();
        } else {
            throw new IOException("Unexpected code:" + response);
        }


    }
}
