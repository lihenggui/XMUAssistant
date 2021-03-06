package com.merxury.xmuassistant;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by deng on 2016/5/15.
 */
public class QuickRoadActivity extends Activity {
    private LinearLayout xuegong;
    private LinearLayout jiaowu;
    private LinearLayout yanjiusheng;
    private LinearLayout lecture;
    private LinearLayout affair;
    private LinearLayout library;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quickroad);

        xuegong = (LinearLayout) findViewById(R.id.xuegong);
        xuegong.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://xg.xmu.edu.cn"));
                startActivity(intent);
            }
        });
        jiaowu = (LinearLayout) findViewById(R.id.jiaowu);
        jiaowu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://ssfw.xmu.edu.cn"));
                startActivity(intent);
            }
        });
        yanjiusheng = (LinearLayout) findViewById(R.id.yanjiusheng);
        yanjiusheng.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://yjsy.xmu.edu.cn"));
                startActivity(intent);
            }
        });
        lecture = (LinearLayout) findViewById(R.id.lecture);
        lecture.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://lecture.xmu.edu.cn"));
                startActivity(intent);
            }
        });
        library = (LinearLayout) findViewById(R.id.library);
        library.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://library.xmu.edu.cn"));
                startActivity(intent);
            }
        });
        affair = (LinearLayout) findViewById(R.id.transtraction);
        affair.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://infoplus.xmu.edu.cn"));
                startActivity(intent);
            }
        });


    }


}
