package com.merxury.xmuassistant;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity {


    //client是用来请求页面的okhttp对象
    public static OkHttpClient client = new OkHttpClient.Builder()
            .build();
    //记录学生卡名字or余额信息
    public String studentName;
    public String money;
    private SharedPreferences.Editor editor;
    private SharedPreferences sp;
    private SharedPreferences pref;

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;
    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private CheckBox rem_pw;         //记住密码复选框
    private CheckBox auto_login;   //自动登录复选框
    private View mProgressView;
    private View mLoginFormView;
    private LocalBroadcastManager localBroadcastManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        /*
        我不是很清楚下面三行代码的最佳位置放在哪里比较好
        反正能用就好了
        就先凑活吧
         */
        /*
        持久化保存cookies~一次登陆即有效~
         */
        sp = this.getSharedPreferences("data", MODE_PRIVATE);
        rem_pw = (CheckBox) findViewById(R.id.CB_password);
        auto_login = (CheckBox) findViewById(R.id.CB_auto);
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        ClearableCookieJar cookieJar =
                new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(this));
        OkHttpClient client1 = new OkHttpClient.Builder()
                .cookieJar(cookieJar)
                .build();
        client = client1;
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        //判断记住密码多选框的状态
        if (sp.getBoolean("ISCHECK", false)) {
            //设置记住密码复选框默认是记录密码状态
            rem_pw.setChecked(true);
            mEmailView.setText(sp.getString("account", ""));//填入账号
            mPasswordView.setText(sp.getString("password", ""));//填入密码
            //判断自动登陆多选框状态
            if (sp.getBoolean("AUTO_ISCHECK", false)) {
                //设置自动登录复选框默认是自动登录状态
                auto_login.setChecked(true);
                //跳转界面  跳转至下一个活动
                attemptLogin();//！！！！！不确定！！！！！
            }
        }
        mPasswordView
                .setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView textView, int id,
                                                  KeyEvent keyEvent) {
                        if (id == R.id.login || id == EditorInfo.IME_NULL) {
                            attemptLogin();
                            return true;
                        }
                        return false;
                    }
                });
        //监听记住密码多选框事件
        rem_pw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (rem_pw.isChecked()) {

                    // System.out.println("记住密码已选中");
                    sp.edit().putBoolean("ISCHECK", true).apply();

                } else {

                    // System.out.println("记住密码没有选中");
                    sp.edit().putBoolean("ISCHECK", false).apply();
                }
            }
        });
        //监听自动登录多选框事件
        auto_login.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (auto_login.isChecked()) {
                    // System.out.println("自动登录已选中");
                    sp.edit().putBoolean("AUTO_ISCHECK", true).apply();

                } else {
                    //    System.out.println("自动登录没有选中");
                    sp.edit().putBoolean("AUTO_ISCHECK", false).apply();
                }
            }
        });
        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });


        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
        }
    }


    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(
                    android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime)
                    .alpha(show ? 0 : 1)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mLoginFormView.setVisibility(show ? View.GONE
                                    : View.VISIBLE);
                        }
                    });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime)
                    .alpha(show ? 1 : 0)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mProgressView.setVisibility(show ? View.VISIBLE
                                    : View.GONE);
                        }
                    });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        // Create adapter to tell the AutoCompleteTextView what to show in its
        // dropdown list.
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                LoginActivity.this,
                android.R.layout.simple_dropdown_item_1line,
                emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }

    // init cookie manager

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;
        //保存密码到配置文件中
        private SharedPreferences.Editor editor;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
            //保存用户名密码到配置文件中
            editor = getSharedPreferences("data", MODE_PRIVATE).edit();
            editor.putString("account", email);
            editor.putString("password", password);
            editor.apply();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                //发送网络请求
                String ixmures = sendPost("http://idstar.xmu.edu.cn/amserver/UI/Login", mEmail, mPassword);
                //使用jsoup解析页面，将html转换成Document实例
                //获取到网页全部代码
                Document ixmudoc = Jsoup.parse(ixmures);
                //获得一系列的子标签,这里的pf1041特指学生卡信息的部分
                if (ixmures.contains("Authentication failed")) {
                    return false;
                    //如果用户名密码错误，返回的信息即有登陆失败的字样，返回失败
                }
                Elements elementsDiv = ixmudoc.getElementsByAttributeValue("id", "pf1041");
                //余额使用font属性来高亮，所以选择font属性就可以抓取到余额信息
                //余额信息在最后一个元素里，使用.last输出
                Elements moneyLeft = elementsDiv.select("font");
                //保存到money变量中
                money = moneyLeft.last().text();

                //选择姓名所在的区域
                Elements nameElems = ixmudoc.select("#pf1037 > div > div.portletContent > table > tbody > tr > td:nth-child(2) > div > ul > li:nth-child(1)");
                String tempName = nameElems.text();
                studentName = tempName.substring(0, tempName.length() - 5);
                editor = getSharedPreferences("data", MODE_PRIVATE).edit();
                editor.putString("CardMoney", money);            //小区ID写入data文件
                editor.putString("studentName", studentName);          //楼号写入data文件
                editor.apply();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
                System.out.println("扫描失败");
            }
            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, SettingsActivity.class);
                startActivity(intent);
                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }


        public String sendPost(String url, String username, String password) throws IOException {

            //将登录的参数添加到HashMap中
            HashMap<String, String> params = new HashMap<>();
            params.put("IDToken0", "");
            params.put("IDToken1", username);
            params.put("IDToken2", password);
            params.put("IDButton", "登录");
            params.put("goto", "http://i.xmu.edu.cn");
            params.put("inputCode", "");
            params.put("gx_charset", "UTF-8");
            //初始化Builder
            FormBody.Builder builder = new FormBody.Builder();
            //将参数添加至Builder中
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.add(entry.getKey(), entry.getValue());
            }
            //创建一个请求对象
            RequestBody formBody = builder.build();
            //发出请求
            Request request = new Request.Builder()
                    .url(url)
                    .post(formBody)
                    .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2700.0 Safari/537.36")
                    .build();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().string();
            } else {
                throw new IOException("Unexpected code:" + response);
            }
        }

    }


}
