package com.merxury.xmuassistant;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity
        implements ObservableScrollView.ScrollViewListener, OnTouchListener {

    /**
     * 滚动显示和隐藏menu时，手指滑动需要达到的速度。
     */
    public static final int SNAP_VELOCITY = 200;
    public static final int UPDATE_TEXT = 1;
    public static OkHttpClient client;
    public static String studentName;
    public static String money;
    boolean isLogin = false;
    HashMap<String, String> news;
    String elecString;
    private ObservableScrollView scrollView1 = null;
    private BroadcastReceiver loginBroadcastReceiver;
    private LocalBroadcastManager localBroadcastManager;
    private TextView urlTextView;
    private TextView titleTextView;
    private TextView contentTextView;
    private CardView newsCardView;
    private CardView newsCardView2;
    private CardView newsCardView3;
    private CardView newsCardView4;
    private CardView newsCardView5;

    /**
     * 屏幕宽度值。
     */
    private int screenWidth;
    /**
     * menu最多可以滑动到的左边缘。值由menu布局的宽度来定，marginLeft到达此值之后，不能再减少。
     */
    private int leftEdge;
    /**
     * menu最多可以滑动到的右边缘。值恒为0，即marginLeft到达0之后，不能增加。
     */
    private int rightEdge = 0;
    /**
     * menu完全显示时，留给content的宽度值。
     */
    private int menuPadding = 540;
    /**
     * 主内容的布局。
     */
    private View content;
    /**
     * menu的布局。
     */
    private View menu;
    /**
     * menu布局的参数，通过此参数来更改leftMargin的值。
     */
    private LinearLayout.LayoutParams menuParams;
    /**
     * 记录手指按下时的横坐标。
     */
    private float xDown;
    /**
     * 记录手指移动时的横坐标。
     */
    private float xMove;
    /**
     * 记录手机抬起时的横坐标。
     */
    private float xUp;
    /**
     * menu当前是显示还是隐藏。只有完全显示或隐藏menu时才会更改此值，滑动过程中此值无效。
     */
    private boolean isMenuVisible;
    /**
     * 用于计算手指滑动的速度。
     */
    private VelocityTracker mVelocityTracker;
    private LinearLayout library;
    private LinearLayout score;
    private LinearLayout course;
    private LinearLayout channel;
    private LinearLayout settings;
    private LinearLayout exit;
    private SearchView searchView;
    private SharedPreferences.Editor editor;
    private SharedPreferences pref;
    /**
     * 隐藏输入的密码,放在了LoginActivity，还未测试
     * <p/>
     * private void init() {
     * mPasswordEditText = (EditText) findViewById(R.id.password_enter);
     * mPasswordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
     * }
     */
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_TEXT: {
                    //1
                    urlTextView = (TextView) findViewById(R.id.news_url);
                    titleTextView = (TextView) findViewById(R.id.news_title);
                    contentTextView = (TextView) findViewById(R.id.news_content);
                    newsCardView = (CardView) findViewById(R.id.newsCard);
                    urlTextView.setText(news.get("URL1"));
                    titleTextView.setText(news.get("Title1"));
                    contentTextView.setText(news.get("content1").substring(0, 200));
                    newsCardView.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            String url = news.get("URL1");
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(url));
                            startActivity(intent);

                        }
                    });
                    //2
                    urlTextView = (TextView) findViewById(R.id.news_url2);
                    titleTextView = (TextView) findViewById(R.id.news_title2);
                    contentTextView = (TextView) findViewById(R.id.news_content2);
                    newsCardView2 = (CardView) findViewById(R.id.newsCard2);
                    urlTextView.setText(news.get("URL2"));
                    titleTextView.setText(news.get("Title2"));
                    contentTextView.setText(news.get("content2").substring(0, 200));
                    newsCardView2.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            String url = news.get("URL2");
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(url));
                            startActivity(intent);

                        }
                    });
                    //3
                    urlTextView = (TextView) findViewById(R.id.news_url3);
                    titleTextView = (TextView) findViewById(R.id.news_title3);
                    contentTextView = (TextView) findViewById(R.id.news_content3);
                    newsCardView3 = (CardView) findViewById(R.id.newsCard3);
                    urlTextView.setText(news.get("URL3"));
                    titleTextView.setText(news.get("Title3"));
                    contentTextView.setText(news.get("content3").substring(0, 200));
                    newsCardView3.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            String url = news.get("URL3");
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(url));
                            startActivity(intent);

                        }
                    });
                    //4
                    urlTextView = (TextView) findViewById(R.id.news_url4);
                    titleTextView = (TextView) findViewById(R.id.news_title4);
                    contentTextView = (TextView) findViewById(R.id.news_content4);
                    newsCardView4 = (CardView) findViewById(R.id.newsCard4);
                    urlTextView.setText(news.get("URL4"));
                    titleTextView.setText(news.get("Title4"));
                    contentTextView.setText(news.get("content4").substring(0, 200));
                    newsCardView4.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            String url = news.get("URL4");
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(url));
                            startActivity(intent);

                        }
                    });
                    //5
                    urlTextView = (TextView) findViewById(R.id.news_url5);
                    titleTextView = (TextView) findViewById(R.id.news_title5);
                    contentTextView = (TextView) findViewById(R.id.news_content5);
                    newsCardView5 = (CardView) findViewById(R.id.newsCard4);
                    urlTextView.setText(news.get("URL5"));
                    titleTextView.setText(news.get("Title5"));
                    contentTextView.setText(news.get("content5").substring(0, 200));
                    newsCardView5.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            String url = news.get("URL5");
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(url));
                            startActivity(intent);
                            TextView elecTextView = (TextView) findViewById(R.id.elecQuery);
                            elecTextView.setText(elecString);

                        }
                    });
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
        initValues();
        displayNews();
        DisplayMoneyAndName();
        content.setOnTouchListener(this);
        LinearLayout library = (LinearLayout) findViewById(R.id.nav_library);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        scrollView1 = (ObservableScrollView) findViewById(R.id.scrollView);
        scrollView1.setScrollViewListener(this);//dan
        //跳转到图书查询界面
        library = (LinearLayout) findViewById(R.id.nav_library);
        library.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, LibraryQuery.class);
                startActivity(intent);
            }
        });
        //跳转到QueryResults
        score = (LinearLayout) findViewById(R.id.nav_score);
        score.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, QueryResults.class);
                startActivity(intent);
            }
        });
        //跳转到课程表界面
        course = (LinearLayout) findViewById(R.id.nav_course);
        course.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://ssfw.xmu.edu.cn/cmstar/index.portal?.pn=p1201_p3530_p3531"));
                startActivity(intent);
            }
        });
        //跳转到QuickRoadActivity
        channel = (LinearLayout) findViewById(R.id.nav_channel);
        channel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, QuickRoadActivity.class);
                startActivity(intent);
            }
        });//跳转到SettingActivity
        settings = (LinearLayout) findViewById(R.id.nav_settings);
        settings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setContentView(R.layout.settings_activity);
            }
        });
        //退出结束应用程序
        exit = (LinearLayout) findViewById(R.id.nav_exit);
        exit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
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
        //exit.setOnClickListener(onclicklistener);


    }

    /**
     * 初始化一些关键性数据。包括获取屏幕的宽度，给content布局重新设置宽度，给menu布局重新设置宽度和偏移距离等。
     */
    private void initValues() {
        WindowManager window = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        screenWidth = window.getDefaultDisplay().getWidth();
        content = findViewById(R.id.content);
        menu = findViewById(R.id.activity_main_drawer);
        menuParams = (LinearLayout.LayoutParams) menu.getLayoutParams();
        // 将menu的宽度设置为屏幕宽度减去menuPadding
        menuParams.width = screenWidth - menuPadding;
        // 左边缘的值赋值为menu宽度的负数
        leftEdge = -menuParams.width;
        // menu的leftMargin设置为左边缘的值，这样初始化时menu就变为不可见
        menuParams.leftMargin = leftEdge;
        // 将content的宽度设置为屏幕宽度
        content.getLayoutParams().width = screenWidth;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        createVelocityTracker(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 手指按下时，记录按下时的横坐标
                xDown = event.getRawX();
                break;
            case MotionEvent.ACTION_MOVE:
                // 手指移动时，对比按下时的横坐标，计算出移动的距离，来调整menu的leftMargin值，从而显示和隐藏menu
                xMove = event.getRawX();
                int distanceX = (int) (xMove - xDown);
                if (isMenuVisible) {
                    menuParams.leftMargin = distanceX;
                } else {
                    menuParams.leftMargin = leftEdge + distanceX;
                }
                if (menuParams.leftMargin < leftEdge) {
                    menuParams.leftMargin = leftEdge;
                } else if (menuParams.leftMargin > rightEdge) {
                    menuParams.leftMargin = rightEdge;
                }
                menu.setLayoutParams(menuParams);
                break;
            case MotionEvent.ACTION_UP:
                // 手指抬起时，进行判断当前手势的意图，从而决定是滚动到menu界面，还是滚动到content界面
                xUp = event.getRawX();
                if (wantToShowMenu()) {
                    if (shouldScrollToMenu()) {
                        scrollToMenu();
                    } else {
                        scrollToContent();
                    }
                } else if (wantToShowContent()) {
                    if (shouldScrollToContent()) {
                        scrollToContent();
                    } else {
                        scrollToMenu();
                    }
                }
                recycleVelocityTracker();
                break;
        }
        return true;
    }

    /**
     * 判断当前手势的意图是不是想显示content。如果手指移动的距离是负数，且当前menu是可见的，则认为当前手势是想要显示content。
     *
     * @return 当前手势想显示content返回true，否则返回false。
     */
    private boolean wantToShowContent() {
        return xUp - xDown < 0 && isMenuVisible;
    }

    /**
     * 判断当前手势的意图是不是想显示menu。如果手指移动的距离是正数，且当前menu是不可见的，则认为当前手势是想要显示menu。
     *
     * @return 当前手势想显示menu返回true，否则返回false。
     */
    private boolean wantToShowMenu() {
        return xUp - xDown > 0 && !isMenuVisible;
    }

    /**
     * 判断是否应该滚动将menu展示出来。如果手指移动距离大于屏幕的1/2，或者手指移动速度大于SNAP_VELOCITY，
     * 就认为应该滚动将menu展示出来。
     *
     * @return 如果应该滚动将menu展示出来返回true，否则返回false。
     */
    private boolean shouldScrollToMenu() {
        return xUp - xDown > screenWidth / 2 || getScrollVelocity() > SNAP_VELOCITY;
    }

    /**
     * 判断是否应该滚动将content展示出来。如果手指移动距离加上menuPadding大于屏幕的1/2，
     * 或者手指移动速度大于SNAP_VELOCITY， 就认为应该滚动将content展示出来。
     *
     * @return 如果应该滚动将content展示出来返回true，否则返回false。
     */
    private boolean shouldScrollToContent() {
        return xDown - xUp + menuPadding > screenWidth / 2 || getScrollVelocity() > SNAP_VELOCITY;
    }

    /**
     * 将屏幕滚动到menu界面，滚动速度设定为30.
     */
    private void scrollToMenu() {
        new ScrollTask().execute(30);
    }

    /**
     * 将屏幕滚动到content界面，滚动速度设定为-30.
     */
    private void scrollToContent() {
        new ScrollTask().execute(-30);
    }

    /**
     * 创建VelocityTracker对象，并将触摸content界面的滑动事件加入到VelocityTracker当中。
     *
     * @param event content界面的滑动事件
     */
    private void createVelocityTracker(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }

    /**
     * 获取手指在content界面滑动的速度。
     *
     * @return 滑动速度，以每秒钟移动了多少像素值为单位。
     */
    private int getScrollVelocity() {
        mVelocityTracker.computeCurrentVelocity(1000);
        int velocity = (int) mVelocityTracker.getXVelocity();
        return Math.abs(velocity);
    }

    /**
     * 回收VelocityTracker对象。
     */
    private void recycleVelocityTracker() {
        mVelocityTracker.recycle();
        mVelocityTracker = null;
    }

    /**
     * 使当前线程睡眠指定的毫秒数。
     *
     * @param millis 指定当前线程睡眠多久，以毫秒为单位
     */
    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onScrollChanged(ObservableScrollView scrollView, int x, int y,
                                int oldx, int oldy) {
        TextView textView1 = (TextView) findViewById(R.id.textContent);
        TextView textView2 = (TextView) findViewById(R.id.singleLine);
        System.out.println(scrollView.getScrollY());
        if (0 <= scrollView.getScrollY() && scrollView.getScrollY() <= 1884) {
            textView2.setBackgroundColor(0xff2196f3);
            textView1.setText("Course");//第一个小提示
        } else if (1884 <= scrollView.getScrollY() && scrollView.getScrollY() <= 3791) {
            textView2.setBackgroundColor(0xffFF8f00);
            textView1.setText("Balance");//第二个小提示
        } else if (scrollView.getScrollY() >= 3791) {
            textView2.setBackgroundColor(0xff009688);
            textView1.setText("News to read");//第三个小提示
        }

    }

    public void displayNews() {
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
                news = NewsQuery.getNewsInfo();
                Message message = new Message();
                message.what = UPDATE_TEXT;
                handler.sendMessage(message); // 将Message对象发送出去
            }
        }).start();
    }



    public void DisplayMoneyAndName() {
        TextView elecTextView = (TextView) findViewById(R.id.studentName);
        elecTextView.setText(studentName);
        TextView xykTextview = (TextView) findViewById(R.id.xykQuery);
        xykTextview.setText("当前校园卡余额:" + money);
    }

    class ScrollTask extends AsyncTask<Integer, Integer, Integer> {

        @Override
        protected Integer doInBackground(Integer... speed) {
            int leftMargin = menuParams.leftMargin;
            // 根据传入的速度来滚动界面，当滚动到达左边界或右边界时，跳出循环。
            while (true) {
                leftMargin = leftMargin + speed[0];
                if (leftMargin > rightEdge) {
                    leftMargin = rightEdge;
                    break;
                }
                if (leftMargin < leftEdge) {
                    leftMargin = leftEdge;
                    break;
                }
                publishProgress(leftMargin);
                // 为了要有滚动效果产生，每次循环使线程睡眠5毫秒，这样肉眼才能够看到滚动动画。
                //20ms太少了我改成了5ms，应该会流畅点
                sleep(5);
            }
            isMenuVisible = speed[0] > 0;
            return leftMargin;
        }

        @Override
        protected void onProgressUpdate(Integer... leftMargin) {
            menuParams.leftMargin = leftMargin[0];
            menu.setLayoutParams(menuParams);
        }


        @Override
        protected void onPostExecute(Integer leftMargin) {
            menuParams.leftMargin = leftMargin;
            menu.setLayoutParams(menuParams);
        }
    }
 }


