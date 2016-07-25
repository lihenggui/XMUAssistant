package com.merxury.xmuassistant;

/**
 * Created by dhd on 2016/5/11.
 * Modified by lihenggui on 2016/7/25
 * 解决了第一屏的跳帧问题
 * 加入了登录状态的判断
 */

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import java.util.Timer;
import java.util.TimerTask;

public class AppStart extends Activity {
    String username;
    private SharedPreferences preferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        final View view = View.inflate(this, R.layout.start, null);
        setContentView(view);
        preferences = this.getSharedPreferences("data", MODE_PRIVATE);
        IsUsernameOrRoomAvailable();
    }

    public void IsUsernameOrRoomAvailable() {
        username = preferences.getString("account", "");
        //检测用户名是否为空，如果为空的话，跳转到登录页面
        if (username.isEmpty()) {
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    startActivity(new Intent(AppStart.this, LoginActivity.class));
                    finish();
                }
            }, 4000); //此为停留时间,1000=1s
            return;
        }
        //如果之前已经保存好了配置文件，直接跳转到MainActivity
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                startActivity(new Intent(AppStart.this, MainActivity.class));
                finish();
            }
        }, 4000); //此为停留时间,1000=1s
    }

}
