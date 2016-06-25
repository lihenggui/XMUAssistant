package com.merxury.xmuassistant;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity {

    /**
     * 滚动显示和隐藏menu时，手指滑动需要达到的速度。
     */

    public static final int UPDATE_TEXT = 1;
    public static OkHttpClient client;
    public static String studentName;
    public static String money;
    List<NewsQuery.News> news;
    String elecString;

    private TextView urlTextView;
    private TextView titleTextView;
    private TextView contentTextView;
    private CardView newsCardView;
    private CardView newsCardView2;
    private CardView newsCardView3;
    private CardView newsCardView4;
    private CardView newsCardView5;
    private TextView elecTextView;


    private LinearLayout score;
    private LinearLayout course;
    private LinearLayout channel;
    private LinearLayout settings;
    private LinearLayout exit;
    private SearchView searchView;
    private SharedPreferences.Editor editor;
    private SharedPreferences pref;
    /**
     * 将获取到的电费信息及新闻显示在CardView里，修改界面只能在主线程中进行，
     */
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_TEXT: {
                    //显示第一条新闻
                    urlTextView = (TextView) findViewById(R.id.news_url);
                    titleTextView = (TextView) findViewById(R.id.news_title);
                    contentTextView = (TextView) findViewById(R.id.news_content);
                    newsCardView = (CardView) findViewById(R.id.newsCard);
                    NewsQuery.News temp = news.get(0);
                    urlTextView.setText(temp.getUrl());
                    titleTextView.setText(temp.getTitle());
                    if (temp.getContent().length() > 200) {
                        contentTextView.setText(temp.getContent().substring(0, 200));
                    } else {
                        contentTextView.setText(temp.getContent());
                    }
                    final String url = temp.getUrl();
                    newsCardView.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(url));
                            startActivity(intent);

                        }
                    });
                    //显示第二条新闻
                    urlTextView = (TextView) findViewById(R.id.news_url2);
                    titleTextView = (TextView) findViewById(R.id.news_title2);
                    contentTextView = (TextView) findViewById(R.id.news_content2);
                    newsCardView2 = (CardView) findViewById(R.id.newsCard2);
                    temp = news.get(1);
                    urlTextView.setText(temp.getUrl());
                    titleTextView.setText(temp.getTitle());
                    if (temp.getContent().length() > 200) {
                        contentTextView.setText(temp.getContent().substring(0, 200));
                    } else {
                        contentTextView.setText(temp.getContent());
                    }
                    final String url1 = temp.getUrl();
                    newsCardView.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(url1));
                            startActivity(intent);

                        }
                    });
                    //显示第三条新闻
                    urlTextView = (TextView) findViewById(R.id.news_url3);
                    titleTextView = (TextView) findViewById(R.id.news_title3);
                    contentTextView = (TextView) findViewById(R.id.news_content3);
                    newsCardView3 = (CardView) findViewById(R.id.newsCard3);
                    temp = news.get(2);
                    urlTextView.setText(temp.getUrl());
                    titleTextView.setText(temp.getTitle());
                    if (temp.getContent().length() > 200) {
                        contentTextView.setText(temp.getContent().substring(0, 200));
                    } else {
                        contentTextView.setText(temp.getContent());
                    }
                    final String url2 = temp.getUrl();
                    newsCardView.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(url2));
                            startActivity(intent);

                        }
                    });
                    //显示第四条新闻
                    urlTextView = (TextView) findViewById(R.id.news_url4);
                    titleTextView = (TextView) findViewById(R.id.news_title4);
                    contentTextView = (TextView) findViewById(R.id.news_content4);
                    newsCardView4 = (CardView) findViewById(R.id.newsCard4);
                    temp = news.get(3);
                    urlTextView.setText(temp.getUrl());
                    titleTextView.setText(temp.getTitle());
                    if (temp.getContent().length() > 200) {
                        contentTextView.setText(temp.getContent().substring(0, 200));
                    } else {
                        contentTextView.setText(temp.getContent());
                    }
                    final String url3 = temp.getUrl();
                    newsCardView.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(url3));
                            startActivity(intent);

                        }
                    });
                    //显示第五条新闻
                    urlTextView = (TextView) findViewById(R.id.news_url5);
                    titleTextView = (TextView) findViewById(R.id.news_title5);
                    contentTextView = (TextView) findViewById(R.id.news_content5);
                    newsCardView5 = (CardView) findViewById(R.id.newsCard5);
                    temp = news.get(4);
                    urlTextView.setText(temp.getUrl());
                    titleTextView.setText(temp.getTitle());
                    if (temp.getContent().length() > 200) {
                        contentTextView.setText(temp.getContent().substring(0, 200));
                    } else {
                        contentTextView.setText(temp.getContent());
                    }
                    final String url4 = temp.getUrl();
                    newsCardView.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(url4));
                            startActivity(intent);

                        }
                    });
                    //显示获取到的电费
                    elecTextView = (TextView) findViewById(R.id.elecQuery);
                    elecTextView.setText("当前电费余额:" + elecString);
                }
                break;
                default:
                    break;
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_main);//显示界面
        pref = getSharedPreferences("data", MODE_PRIVATE);
        studentName = pref.getString("studentName", "");
        money = pref.getString("CardMoney", "");
        displayNews(new NewsQuery(this, "news", null, 1));
        DisplayMoneyAndName();
        LinearLayout library = (LinearLayout) findViewById(R.id.nav_library);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //跳转到图书查询界面
//        library = (LinearLayout) findViewById(R.id.nav_library);
//
//        跳转到QueryResults
//        score = (LinearLayout) findViewById(R.id.nav_score);
//        score.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {/       /*
//             此处留空，跳转到查询结果
//             */
//            }
//        });
        // 跳转到课程表界面
//        course = (LinearLayout) findViewById(R.id.nav_course);
//        course.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                intent.setData(Uri.parse("http://ssfw.xmu.edu.cn/cmstar/index.portal?.pn=p1201_p3530_p3531"));
//                startActivity(intent);
//            }
//        });
//        //跳转到QuickRoadActivity
//        channel = (LinearLayout) findViewById(R.id.nav_channel);
//        channel.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setClass(MainActivity.this, QuickRoadActivity.class);
//                startActivity(intent);
//            }
//        });//跳转到SettingActivity
//        settings = (LinearLayout) findViewById(R.id.nav_settings);
//        settings.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                setContentView(R.layout.settings_activity);
//            }
//        });
//        //退出结束应用程序
//        exit = (LinearLayout) findViewById(R.id.nav_exit);
//        exit.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setClass(MainActivity.this, LoginActivity.class);
//                startActivity(intent);
//            }
//        });
        searchView = (SearchView) findViewById(R.id.searchBox);
        //设置一个提交按钮
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            private String TAG = getClass().getSimpleName();

            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d(TAG, "onQueryTextSubmit = " + query);
                if (searchView != null) {
                    // 得到输入管理对象
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        // 这将让键盘在所有的情况下都被隐藏，但是一般我们在点击搜索按钮后，输入法都会乖乖的自动隐藏的。
                        imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0); // 输入法如果是显示状态，那么就隐藏输入法
                    }
                    searchView.clearFocus(); // 不获取焦点
                }
                //获取到用户输入的文本内容然后调用tabs打开
                String url = "http://210.34.4.28/opac/openlink.php?q0=" + query + "&sType0=01&pageSize=20&sort=score&desc=on&strText=" + query + "&doctype=01&strSearchType=title&displaypg=20";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }

    /**
     * 因涉及到网络操作，所以在子线程中获取电费以及新闻
     */
    public void displayNews(final NewsQuery newsQuery) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
                    String xiaoqu = pref.getString("xiaoqu", ""); //获取小区ID
                    String lou = pref.getString("lou", ""); //获取楼号
                    String roomID = pref.getString("roomID", "");//获取房间号
                    ElecQuery elec = new ElecQuery(xiaoqu, lou, roomID);
                    elecString = elec.getElec();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                newsQuery.getAllNewsInfoFromServer();
                news = newsQuery.getAllNews();
                Message message = new Message();
                message.what = UPDATE_TEXT;
                handler.sendMessage(message); // 将Message对象发送出去
            }
        }).start();
    }

    public void DisplayMoneyAndName() {
        try {
            TextView elecTextView = (TextView) findViewById(R.id.studentName);
            //elecTextView.setText(studentName);
            TextView xykTextview = (TextView) findViewById(R.id.xykQuery);
            // xykTextview.setText("当前校园卡余额:" + money);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }


}



