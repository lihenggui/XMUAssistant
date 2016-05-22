package com.merxury.xmuassistant;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class CourseTable extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_table);
        webView = (WebView) findViewById(R.id.webview);
        /*
        将前一步获取到的cookie传入webView,实现免登录即可获取到页面
         */
        String strUrl = "http://ssfw.xmu.edu.cn/cmstar/index.portal?.p=Znxjb20ud2lzY29tLnBvcnRhbC5zaXRlLmltcGwuRnJhZ21lbnRXaW5kb3d8ZjI2MDl8dmlld3xub3JtYWx8YWN0aW9uPXF1ZXJ5#anchorf2609\n";

        /*CookieJar cookieJar = LoginActivity.client.cookieJar();
        HttpUrl url = HttpUrl.parse(strUrl);
        List<Cookie> cookies = cookieJar.loadForRequest(url);
        StringBuilder sb = new StringBuilder();
        for (Cookie cookie : cookies) {
            sb.append(cookie.toString());
            sb.append(";");
        }
        String strCookies = sb.toString();*/
        CookieManager cookieManager = CookieManager.getInstance();
        String CookieStr = cookieManager.getCookie(strUrl);
        webView.loadUrl(strUrl);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Toast.makeText(CourseTable.this, "Load Finished!", Toast.LENGTH_SHORT).show();
            }
        });

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
    }
}