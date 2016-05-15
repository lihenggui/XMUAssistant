package com.merxury.xmuassistant;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by deng on 2016/5/15.
 */
public class QuickRoadActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quickroad);
    }
    public void xuegong(View v) {
        Intent intent= new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("http://xg.xmu.edu.cn"));
        startActivity(intent);
    }

    public void jiaowu(View v) {
        Intent intent= new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("http://ssfw.xmu.edu.cn"));
        startActivity(intent);
    }

    public void yanjiusheng(View v) {
        Intent intent= new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("http://yjsy.xmu.edu.cn"));
        startActivity(intent);
    }

    public void lecture(View v) {
        Intent intent= new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("http://lecture.xmu.edu.cn"));
        startActivity(intent);
    }

    public void library(View v) {
        Intent intent= new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("http://library.xmu.edu.cn"));
        startActivity(intent);
    }

    public void affair(View v) {
        Intent intent= new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("http://infoplus.xmu.edu.cn"));
        startActivity(intent);
    }

}
