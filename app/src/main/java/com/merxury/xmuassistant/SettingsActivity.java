package com.merxury.xmuassistant;

        import android.content.SharedPreferences;
        import android.os.Bundle;
        import android.preference.PreferenceManager;
        import android.support.v7.app.AppCompatActivity;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
        import android.widget.EditText;
        import android.widget.Spinner;

import com.merxury.xmuassistant.R;

/**
 * 从配置文件中读取宿舍号信息
 *SharedPreferences  pref = getSharedPreferences("data",MODE_PRIVATE);
 *String xiaoqu = pref.getString("xiaoqu",""); //获取小区ID
 *String lou = pref.getString("lou",""); //获取楼号
 *String roomID = pref.getString("roomID","");//获取房间号
 */

/**
 * Created by deng on 2016/5/18.
 */
public class SettingsActivity extends AppCompatActivity {

    private Spinner Xiaoqu = (Spinner) findViewById(R.id.xiaoqu);  //关联宿舍楼群的Spinner
    private Spinner Lou = (Spinner) findViewById(R.id.lou);   //关联楼号的Spinner
    private SharedPreferences.Editor editor;
    private SharedPreferences pref;
    private EditText room = (EditText) findViewById(R.id.room);
    //  private EditText louID = (EditText) findViewById(R.id.louid);
    private String ID = "";  //存放小区ID
    private String louid = "";   //获取楼号

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        Xiaoqu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                // TODO Auto-generated method stub

                String xiaoquValue = (String) Xiaoqu.getItemAtPosition(arg2);
                //调用自定义方法updateSpinner，使楼号的Spinner根据宿舍楼群的选择显示不同内容
                updateSpinner(xiaoquValue);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
                Xiaoqu.setSelection(0);
            }
        });
        Lou.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                // TODO Auto-generated method stub

                louid = (String) Lou.getItemAtPosition(arg2);


            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
                Lou.setSelection(0);
            }
        });

    }

    //自定义方法updateSpinner
    private void updateSpinner(String xiaoqu) {
        if (xiaoqu.equalsIgnoreCase("本部芙蓉区")) {
            ID = "01";   //更新小区的ID
            String[] lou_value = getResources().getStringArray(R.array.Bfurong);
            // 将可选内容与ArrayAdapter连接起来
            ArrayAdapter<String> lou_adapter = new ArrayAdapter<String>(SettingsActivity.this, android.R.layout.simple_spinner_item, lou_value);
            // 设置下拉列表的风格
            lou_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // 将adapter 添加到spinner中
            Lou.setAdapter(lou_adapter);
        } else if (xiaoqu.equalsIgnoreCase("本部石井区")) {
            ID = "02";
            String[] lou_value = getResources().getStringArray(R.array.Bshijing);
            // 将可选内容与ArrayAdapter连接起来
            ArrayAdapter<String> lou_adapter = new ArrayAdapter<String>(SettingsActivity.this, android.R.layout.simple_spinner_item, lou_value);
            // 设置下拉列表的风格
            lou_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // 将adapter 添加到spinner中
            Lou.setAdapter(lou_adapter);
        } else if (xiaoqu.equalsIgnoreCase("本部南光区")) {
            ID = "03";
            String[] lou_value = getResources().getStringArray(R.array.Bnanguang);
            // 将可选内容与ArrayAdapter连接起来
            ArrayAdapter<String> lou_adapter = new ArrayAdapter<String>(SettingsActivity.this, android.R.layout.simple_spinner_item, lou_value);
            // 设置下拉列表的风格
            lou_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // 将adapter 添加到spinner中
            Lou.setAdapter(lou_adapter);
        } else if (xiaoqu.equalsIgnoreCase("本部凌云区")) {
            ID = "04";
            String[] lou_value = getResources().getStringArray(R.array.Blingyun);
            // 将可选内容与ArrayAdapter连接起来
            ArrayAdapter<String> lou_adapter = new ArrayAdapter<String>(SettingsActivity.this, android.R.layout.simple_spinner_item, lou_value);
            // 设置下拉列表的风格
            lou_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // 将adapter 添加到spinner中
            Lou.setAdapter(lou_adapter);
        } else if (xiaoqu.equalsIgnoreCase("本部勤业区")) {
            ID = "05";
            String[] lou_value = getResources().getStringArray(R.array.Bqinye);
            // 将可选内容与ArrayAdapter连接起来
            ArrayAdapter<String> lou_adapter = new ArrayAdapter<String>(SettingsActivity.this, android.R.layout.simple_spinner_item, lou_value);
            // 设置下拉列表的风格
            lou_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // 将adapter 添加到spinner中
            Lou.setAdapter(lou_adapter);
        } else if (xiaoqu.equalsIgnoreCase("本部海滨新区")) {
            ID = "06";
            String[] lou_value = getResources().getStringArray(R.array.Bhaibin);
            // 将可选内容与ArrayAdapter连接起来
            ArrayAdapter<String> lou_adapter = new ArrayAdapter<String>(SettingsActivity.this, android.R.layout.simple_spinner_item, lou_value);
            // 设置下拉列表的风格
            lou_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // 将adapter 添加到spinner中
            Lou.setAdapter(lou_adapter);
        } else if (xiaoqu.equalsIgnoreCase("本部丰庭区")) {
            ID = "07";
            String[] lou_value = getResources().getStringArray(R.array.Bfengting);
            // 将可选内容与ArrayAdapter连接起来
            ArrayAdapter<String> lou_adapter = new ArrayAdapter<String>(SettingsActivity.this, android.R.layout.simple_spinner_item, lou_value);
            // 设置下拉列表的风格
            lou_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // 将adapter 添加到spinner中
            Lou.setAdapter(lou_adapter);
        } else if (xiaoqu.equalsIgnoreCase("海韵学生公寓")) {
            ID = "08";
            String[] lou_value = getResources().getStringArray(R.array.haiyun);
            // 将可选内容与ArrayAdapter连接起来
            ArrayAdapter<String> lou_adapter = new ArrayAdapter<String>(SettingsActivity.this, android.R.layout.simple_spinner_item, lou_value);
            // 设置下拉列表的风格
            lou_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // 将adapter 添加到spinner中
            Lou.setAdapter(lou_adapter);
        } else if (xiaoqu.equalsIgnoreCase("曾厝垵学生公寓")) {
            ID = "09";
            String[] lou_value = getResources().getStringArray(R.array.zengcuoan);
            // 将可选内容与ArrayAdapter连接起来
            ArrayAdapter<String> lou_adapter = new ArrayAdapter<String>(SettingsActivity.this, android.R.layout.simple_spinner_item, lou_value);
            // 设置下拉列表的风格
            lou_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // 将adapter 添加到spinner中
            Lou.setAdapter(lou_adapter);
        } else if (xiaoqu.equalsIgnoreCase("翔安芙蓉区")) {
            ID = "33";
            String[] lou_value = getResources().getStringArray(R.array.Xfurong);
            // 将可选内容与ArrayAdapter连接起来
            ArrayAdapter<String> lou_adapter = new ArrayAdapter<String>(SettingsActivity.this, android.R.layout.simple_spinner_item, lou_value);
            // 设置下拉列表的风格
            lou_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // 将adapter 添加到spinner中
            Lou.setAdapter(lou_adapter);
        } else if (xiaoqu.equalsIgnoreCase("翔安南安区")) {
            ID = "34";
            String[] lou_value = getResources().getStringArray(R.array.Xnanan);
            // 将可选内容与ArrayAdapter连接起来
            ArrayAdapter<String> lou_adapter = new ArrayAdapter<String>(SettingsActivity.this, android.R.layout.simple_spinner_item, lou_value);
            // 设置下拉列表的风格
            lou_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // 将adapter 添加到spinner中
            Lou.setAdapter(lou_adapter);
        } else if (xiaoqu.equalsIgnoreCase("翔安南光区")) {
            ID = "35";
            String[] lou_value = getResources().getStringArray(R.array.Xnanguang);
            // 将可选内容与ArrayAdapter连接起来
            ArrayAdapter<String> lou_adapter = new ArrayAdapter<String>(SettingsActivity.this, android.R.layout.simple_spinner_item, lou_value);
            // 设置下拉列表的风格
            lou_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // 将adapter 添加到spinner中
            Lou.setAdapter(lou_adapter);
        } else if (xiaoqu.equalsIgnoreCase("翔安国光区")) {
            ID = "42";
            String[] lou_value = getResources().getStringArray(R.array.Xguoguang);
            // 将可选内容与ArrayAdapter连接起来
            ArrayAdapter<String> lou_adapter = new ArrayAdapter<String>(SettingsActivity.this, android.R.layout.simple_spinner_item, lou_value);
            // 设置下拉列表的风格
            lou_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // 将adapter 添加到spinner中
            Lou.setAdapter(lou_adapter);
        } else if (xiaoqu.equalsIgnoreCase("翔安丰庭区")) {
            ID = "41";
            String[] lou_value = getResources().getStringArray(R.array.Xfengting);
            // 将可选内容与ArrayAdapter连接起来
            ArrayAdapter<String> lou_adapter = new ArrayAdapter<String>(SettingsActivity.this, android.R.layout.simple_spinner_item, lou_value);
            // 设置下拉列表的风格
            lou_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // 将adapter 添加到spinner中
            Lou.setAdapter(lou_adapter);
        } else if (xiaoqu.equalsIgnoreCase("翔安笃行区")) {
            ID = "40";
            String[] lou_value = getResources().getStringArray(R.array.Xduxing);
            // 将可选内容与ArrayAdapter连接起来
            ArrayAdapter<String> lou_adapter = new ArrayAdapter<String>(SettingsActivity.this, android.R.layout.simple_spinner_item, lou_value);
            // 设置下拉列表的风格
            lou_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // 将adapter 添加到spinner中
            Lou.setAdapter(lou_adapter);
        }

        String roomid = room.getText().toString();   //获取房间号
        editor = getSharedPreferences("data", MODE_PRIVATE).edit();
        editor.putString("xiaoqu", ID);            //小区ID写入data文件
        editor.putString("lou", louid);          //楼号写入data文件
        editor.putString("roomID", roomid);    //房间号写入data文件
        editor.commit();
    }
}
