package com.merxury.xmuassistant;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dhd on 2016/5/17.
 */
import android.app.Activity;
import android.app.Activity;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;

import com.merxury.xmuassistant.ObservableScrollView;
import com.merxury.xmuassistant.R;

public class Content extends Activity implements ObservableScrollView.ScrollViewListener {


    private ObservableScrollView scrollView1 = null;
    private ObservableScrollView scrollView2 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        scrollView1 = (ObservableScrollView) findViewById(R.id.scrollView);
        scrollView1.setScrollViewListener(this);

    }

    @Override
    public void onScrollChanged(ObservableScrollView scrollView, int x, int y,
                                int oldx, int oldy) {
        TextView textView1= (TextView) findViewById(R.id.textContent);
        TextView textView2= (TextView) findViewById(R.id.singleLine);
        System.out.println(scrollView.getScrollY());
        if(0<=scrollView.getScrollY()&&scrollView.getScrollY()<=1884){
            textView2.setBackgroundColor(0xff2196f3);
            textView1.setText("Course");
        }
        else if(1884<=scrollView.getScrollY()&&scrollView.getScrollY()<=3791){
            textView2.setBackgroundColor(0xffFF8f00);
            textView1.setText("News to read");
        }else if(scrollView.getScrollY()>=3791){
            textView2.setBackgroundColor(0xff009688);
            textView1.setText("Notice");
        }

    }

}
