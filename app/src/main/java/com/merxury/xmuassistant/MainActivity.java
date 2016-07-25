package com.merxury.xmuassistant;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener
        ,NavigationView.OnNavigationItemSelectedListener  {

    /**
     * 滚动显示和隐藏menu时，手指滑动需要达到的速度。
     */

    public static final int UPDATE_TEXT = 1;
    public static OkHttpClient client;
    public static String studentName;
    public static String money;
    List<NewsQuery.News> news;
    String elecString;
    private SwipeRefreshLayout swipeLayout;


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
                    //显示新闻
                    showNewsFromDatabase();
                    //显示获取到的电费
                    elecTextView = (TextView) findViewById(R.id.elecQuery);
                    elecTextView.setText("当前电费余额:" + elecString);
                    swipeLayout.setRefreshing(false);
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
        setContentView(R.layout.activity_main);//显示界面
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);//SwipeLayout
        swipeLayout.setOnRefreshListener(this);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        pref = getSharedPreferences("data", MODE_PRIVATE);
        studentName = pref.getString("studentName", "");
        money = pref.getString("CardMoney", "");
//        displayNews(new NewsQuery(this, "news", null, 1));
//        DisplayMoneyAndName();

//        });
        searchView = (SearchView) findViewById(R.id.searchBox);
        //设置一个提交按钮
        searchView.setSubmitButtonEnabled(true);
        //显示新闻
        showNewsFromDatabase();
        //启动服务
        Intent startServerIntent = new Intent(this, BackgroundService.class);
        startService(startServerIntent);

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * 因涉及到网络操作，所以在子线程中获取电费以及新闻
     */
    public void displayNews(final NewsQuery newsQuery, final CardQuery cardQuery) {
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
                    Double money = cardQuery.getMoney();


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
            elecTextView.setText(studentName);
            TextView xykTextview = (TextView) findViewById(R.id.xykQuery);
            xykTextview.setText("当前校园卡余额:" + money);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }

    public void onRefresh() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    displayNews(new NewsQuery(getApplicationContext(), "news", null, 1), new CardQuery(pref.getString("account", ""), pref.getString("password", ""), getApplicationContext()));
                    DisplayMoneyAndName();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_library) {

        } else if (id == R.id.nav_score) {

        } else if (id == R.id.nav_course) {

        } else if (id == R.id.nav_channel) {
               Intent intent = new Intent();
               intent.setClass(MainActivity.this, QuickRoadActivity.class);
               startActivity(intent);

        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_exit) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void showNewsFromDatabase() {
        NewsQuery newsQuery = new NewsQuery(getApplicationContext(), "news", null, 1);
        news = newsQuery.getAllNews();
        titleTextView = (TextView) findViewById(R.id.news_title);
        contentTextView = (TextView) findViewById(R.id.news_content);
        newsCardView = (CardView) findViewById(R.id.newsCard);
        //对新闻体进行判断，如果是空新闻的话，提示用户刷新
        if (news == null) {
            titleTextView.setText("请下拉刷新以获取新闻！");
            contentTextView.setText("请下拉刷新以获取新闻！");
            return;
        }
        //初始化新闻内容，从数据库中读取
        NewsQuery.News temp = news.get(0);
        titleTextView.setText(temp.getTitle());
        if (temp.getContent().length() > 140) {
            String content = temp.getContent().substring(0, 140) + "...";
            contentTextView.setText(content);
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
        titleTextView = (TextView) findViewById(R.id.news_title2);
        contentTextView = (TextView) findViewById(R.id.news_content2);
        newsCardView2 = (CardView) findViewById(R.id.newsCard2);
        temp = news.get(1);
        titleTextView.setText(temp.getTitle());
        if (temp.getContent().length() > 140) {
            String content = temp.getContent().substring(0, 140) + "...";
            contentTextView.setText(content);
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
        titleTextView = (TextView) findViewById(R.id.news_title3);
        contentTextView = (TextView) findViewById(R.id.news_content3);
        newsCardView3 = (CardView) findViewById(R.id.newsCard3);
        temp = news.get(2);
        titleTextView.setText(temp.getTitle());
        if (temp.getContent().length() > 140) {
            String content = temp.getContent().substring(0, 140) + "...";
            contentTextView.setText(content);
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
        titleTextView = (TextView) findViewById(R.id.news_title4);
        contentTextView = (TextView) findViewById(R.id.news_content4);
        newsCardView4 = (CardView) findViewById(R.id.newsCard4);
        temp = news.get(3);
        titleTextView.setText(temp.getTitle());
        if (temp.getContent().length() > 140) {
            String content = temp.getContent().substring(0, 140) + "...";
            contentTextView.setText(content);
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
        titleTextView = (TextView) findViewById(R.id.news_title5);
        contentTextView = (TextView) findViewById(R.id.news_content5);
        newsCardView5 = (CardView) findViewById(R.id.newsCard5);
        temp = news.get(4);
        titleTextView.setText(temp.getTitle());
        if (temp.getContent().length() > 140) {
            String content = temp.getContent().substring(0, 140) + "...";
            contentTextView.setText(content);
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
    }

    //        跳转到图书查询界面

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




}



