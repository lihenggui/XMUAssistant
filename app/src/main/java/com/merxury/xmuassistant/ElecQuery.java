package com.merxury.xmuassistant;

import android.content.Context;
import android.content.SharedPreferences;

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
 * Created by lihenggui on 2016/5/15.
 * 查询电费的方法，需要二次抓取页面
 * 构造函数需要传入三个参数，第一个是小区的代号，第二个是楼号（中文），第三个是宿舍的房间号（四位数）
 * getMoney/getElec方法都是获取电费信息，区别就是，getElec是实时获取电费信息，因为要和服务器连接通讯所以速度较慢
 * getmoney方法是直接调用保存好的电费余额，如果余额检测为空会自动调用getElec方法获取余额
 */
public class ElecQuery {
    public static OkHttpClient client = new OkHttpClient.Builder().build();
    private String url = "http://elec.xmu.edu.cn/PdmlWebSetup/Pages/SMSMain.aspx";//请求url的地址
    private String xiaoqu;//请求小区的地址
    private String lou;//楼号
    private String roomID;//房间号
    private String moneyLeft; //余额信息
    private Context context; //传入Context
    private SharedPreferences preferences;

    /**
     * 电费查询方法的构造甘薯
     *
     * @param xiaoqu  校区名称
     * @param lou     楼号
     * @param roomID  房间号
     * @param context 传入context，用于保存配置
     */
    public ElecQuery(String xiaoqu, String lou, String roomID, Context context) {
        this.xiaoqu = xiaoqu;
        this.lou = lou;
        this.roomID = roomID;
        this.context = context;
    }


    //getters and setters 写入&读取各种信息
    public double getMoney() {
        if (moneyLeft.isEmpty()) {
            try {
                moneyLeft = getElec();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return Double.valueOf(moneyLeft);
    }

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
        //支持三位宿舍号的输入
        //网页要求输入的宿舍号是四位数，对于三位数的宿舍号前方要加一个0才能查询
        //ROOMID一定要为四位数
        if (roomID.length() == 3) {
            roomID = "0" + roomID;
        }
        this.roomID = roomID;
    }

    public String getlou() {
        return lou;
    }

    //楼号，传入数字即可，会自动添加字符串
    public void setlou(String lou) {
        lou = lou + "号楼";
        this.lou = lou;
    }

    public String getXiaoqu() {
        return xiaoqu;
    }

    public void setXiaoqu(String xiaoqu) {
        this.xiaoqu = xiaoqu;
    }

    //请求整个电费页面
    //网页开启了两步验证，首先要将楼号，viewstate,eventvalidation参数传给服务器，服务器返回页面后新生成的viewstate/eventvalidation才能采集+提交
    //不然会报http 500错误

    /**
     * 得到电费的方法
     * @return 电费的金额，String类型
     * @throws IOException
     */
    public String getElec() throws IOException {
        //电费查询有隐藏参数，放在页面源代码中，所以要申请读取一次页面抓取这些隐藏的信息
        //第一步抓取页面，首先先向服务器发送数据，获取网页内容
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
        //ok，得到了这四个参数之后，要跟着小区号一起传送给服务器，获取到新的页面才能查询到电费信息，MDZZ
        HashMap<String, String> tempPara = new HashMap<>();
        tempPara.put("drxiaoqu", xiaoqu);
        tempPara.put("__VIEWSTATE", ViewState);
        tempPara.put("__EVENTVALIDATION", EventValidation);
        tempPara.put("dxgvSubInfo_CallbackState", subCallBackState);
        tempPara.put("dxgvElec_CallbackState", elecCallBackState);
        //得到网页余额然后返回
        FormBody.Builder tempBuilder = new FormBody.Builder();
        //将参数添加至Builder中
        for (Map.Entry<String, String> entry : tempPara.entrySet()) {
            tempBuilder.add(entry.getKey(), entry.getValue());
        }
        RequestBody firstFormBody = tempBuilder.build();
        //发出请求
        Request firstRequest = new Request.Builder()
                .url(url)
                .post(firstFormBody)
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2700.0 Safari/537.36")
                .build();
        Response firstResponse = client.newCall(firstRequest).execute();
        if (firstResponse.isSuccessful()) {
            //获取到了新页面后，重新提取页面上的验证信息，用于第二次向服务器发送数据
            Document realParams = Jsoup.parse(firstResponse.body().string());
            ViewState = realParams.select("#__VIEWSTATE").attr("value");
            EventValidation = realParams.select("#__EVENTVALIDATION").attr("value");
            subCallBackState = realParams.select("#dxgvSubInfo_CallbackState").attr("value");
            elecCallBackState = realParams.select("#dxgvElec_CallbackState").attr("value");
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
            //发出第二次请求
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
                Elements elecElem = elecDoc.select("#dxgvElec_DXDataRow0 > td:nth-child(7)");
                moneyLeft = elecElem.text();
                SaveElecMoney(moneyLeft);
                return moneyLeft;
            } else {
                throw new IOException("Unexpected code:" + response);
            }
        } else {
            throw new IOException("Unexpected code:" + firstResponse);
        }
    }

    //保存电费到配置文件中的方法
    private void SaveElecMoney(String money) {
        preferences = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("ElecMoney", money);
        editor.apply();
    }

}
