package com.merxury.xmuassistant;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener
        ,NavigationView.OnNavigationItemSelectedListener  {

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
    private CardView studentCardMoney;
    private CardView elecMoney;
    private TextView elecTextView;

    private SearchView searchView;
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);//显示界面
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);//SwipeLayout
        swipeLayout.setColorSchemeColors(Color.BLUE,Color.RED);//设置旋转颜色
        swipeLayout.setOnRefreshListener(this);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        pref = getSharedPreferences("data", MODE_PRIVATE);
        studentName = pref.getString("studentName", "");
        money = pref.getString("CardMoney", "");
        //显示新闻
        showNewsFromDatabase();

        //卡片可点击
        MakeMoneyCardClickable();

        //启动服务
        Intent startServerIntent = new Intent(this, BackgroundService.class);
        startService(startServerIntent);

        //设置一个提交按钮
        searchView = (SearchView) findViewById(R.id.searchBox);
        searchView.setSubmitButtonEnabled(true);
        searchView.setQueryHint("请键入您想要搜索的书籍名称");
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
        DisplayMoneyAndName();
        //一进入界面就开始刷新
        swipeLayout.setRefreshing(true);
        onRefresh();
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
     * 将获取到的电费信息及新闻显示在CardView里，修改界面只能在主线程中进行，
     */
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_TEXT: {
                    //显示新闻
                    showNewsFromDatabase();
                    //显示学生卡余额
                    DisplayMoneyAndName();
                    //显示获取到的电费
                    elecTextView = (TextView) findViewById(R.id.elecQuery);
                    elecTextView.setText("当前电费余额：" + elecString + "元");
                    //弹出刷新完毕提示
                    Toast.makeText(getApplicationContext(), "刷新完毕",
                            Toast.LENGTH_SHORT).show();
                    swipeLayout.setRefreshing(false);
                }
                break;
                default:
                    break;
            }
        }

    };

    /**
     * 检测当的网络（WLAN、3G/2G）状态
     *
     * @param context Context
     * @return true 表示网络可用
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                // 当前网络是连接的
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    // 当前所连接的网络可用
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 因涉及到网络操作，所以在子线程中获取电费以及新闻
     * 参数列表
     * @param newsQuery 新闻查询类
     * 执行完毕之后，会发送一个消息给Handler接收
     * 更新主界面
     */
    public void GetNews(final NewsQuery newsQuery) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //获取楼号，然后请求电费信息
                    SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
                    String xiaoqu = pref.getString("xiaoqu", ""); //获取小区ID
                    String lou = pref.getString("lou", ""); //获取楼号
                    String roomID = pref.getString("roomID", "");//获取房间号
                    ElecQuery elec = new ElecQuery(xiaoqu, lou, roomID, getApplicationContext());
                    //下面的两个方法都会更新配置文件储存的金额和学生姓名
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

    /**
     * 查询学生卡余额的方法
     * @param cardQuery 查询学生卡的对象
     * 使用getMoney方法来获得余额信息
     */
    public void GetStudentCardMoney(final CardQuery cardQuery){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                 cardQuery.getMoney();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void DisplayMoneyAndName() {
        try {
            TextView elecTextView = (TextView) findViewById(R.id.studentName);
            studentName = pref.getString("studentName", "");
            if (studentName.trim().isEmpty()) {
                elecTextView.setText("获取信息不正确，请重新下拉以刷新");
            } else {
                elecTextView.setText(studentName);
            }
            TextView xykTextview = (TextView) findViewById(R.id.xykQuery);
            money = pref.getString("CardMoney", "0.00");
            xykTextview.setText("当前校园卡余额：" + money + "元");
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }
    //在下拉刷新的时候执行的操作
    public void onRefresh() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    GetNews(new NewsQuery(getApplicationContext(), "news", null, 1));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                GetStudentCardMoney( new CardQuery(pref.getString("account", ""), pref.getString("password", ""), getApplicationContext()));
            }
        }).start();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer == null){
            return;
        }
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

        // Navigation Drawer的点击事件处理
        int id = item.getItemId();

        if (id == R.id.nav_library) {
            //未完成
            Toast.makeText(getApplicationContext(), "我们正在努力完善功能中，尽请期待",
                    Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_score) {
            //未完成
            Toast.makeText(getApplicationContext(), "我们正在努力完善功能中，尽请期待",
                    Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_course) {
            //未完成
            Toast.makeText(getApplicationContext(), "我们正在努力完善功能中，尽请期待",
                    Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_channel) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, QuickRoadActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_settings) {
            Intent intent =  new Intent();
            intent.setClass(MainActivity.this,SettingsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_exit) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this,LoginActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if(drawer != null) {
            drawer.closeDrawer(GravityCompat.START);
        }
        return true;
    }
    public void MakeMoneyCardClickable(){
        studentCardMoney = (CardView)findViewById(R.id.student_money_card);
        studentCardMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://i.xmu.edu.cn"));
                startActivity(intent);
            }
        });
        elecMoney = (CardView)findViewById(R.id.elec_money_card);
        elecMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://elec.xmu.edu.cn"));
                startActivity(intent);
            }
        });
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
        //对新闻标题进行判断，如果新闻标题是null的话，提示用户刷新
        //初始化新闻内容，从数据库中读取
        NewsQuery.News temp = news.get(0);
        if (temp.getTitle().equals("null")) {
            titleTextView.setText("教务处给我们的数据不对啊，请下拉刷新以获取新闻！");
            contentTextView.setText("请下拉刷新以获取新闻！");
            return;
        }
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
        newsCardView2.setOnClickListener(new View.OnClickListener() {
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
        newsCardView3.setOnClickListener(new View.OnClickListener() {
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
        newsCardView4.setOnClickListener(new View.OnClickListener() {
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
        newsCardView5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url4));
                startActivity(intent);

            }
        });
    }



}



