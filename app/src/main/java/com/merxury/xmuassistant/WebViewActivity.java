package com.merxury.xmuassistant;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import org.apache.http.cookie.CookieRestrictionViolationException;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

public class WebViewActivity extends AppCompatActivity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        webView = (WebView) findViewById(R.id.webView1);
        /*
        同步Cookies
         */
        String strUrl = "http://i.xmu.edu.cn";
        /*
        CookieJar cookieJar = LoginActivity.client.cookieJar();
        HttpUrl url = HttpUrl.parse(strUrl);
        List<Cookie> cookies = cookieJar.loadForRequest(url);
        StringBuilder sb = new StringBuilder();
        for (Cookie cookie : cookies) {
            sb.append(cookie.toString());
            sb.append(";");
        }
        String strCookies = sb.toString();
        android.webkit.CookieManager cookieManager = android.webkit.CookieManager.getInstance();
        cookieManager.setCookie(strUrl, strCookies);
        */
        webView.loadUrl(strUrl);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Toast.makeText(WebViewActivity.this, "Load Finished!", Toast.LENGTH_SHORT).show();
            }
        });

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
    }
}
