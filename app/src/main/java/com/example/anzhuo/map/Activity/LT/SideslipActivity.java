package com.example.anzhuo.map.activity.LT;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anzhuo.map.utils.MainApplication;
import com.example.anzhuo.map.R;
import com.example.anzhuo.map.net.Guide;
import com.example.anzhuo.map.net.Weather;
import com.example.anzhuo.map.utils.MyApplication;
import com.example.anzhuo.map.utils.Util;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by anzhuo on 2016/9/9.
 */
public class SideslipActivity extends Activity implements View.OnClickListener {
    boolean choose = true;
    TextView tv_address_slip;
    TextView tv_weather_slip;
    TextView tv_name;
    ImageView iv_head;
    ImageView iv_off_slip;
    ImageView iv_weather_slip;
    ImageView iv_setting_slip;
    UserInfo mInfo;
    TextView tv_temperature_slip;
    private boolean isNight = false;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    /*
                    获取定位
                     */
                    tv_address_slip.setText(msg.obj.toString());
                    break;
                case 1:
                    /*
                    获取天气
                     */
                    tv_weather_slip.setText(msg.obj.toString());
                    break;
                case 2:
                    /*
                    获取用户名
                     */
                    JSONObject response = (JSONObject) msg.obj;
                    if (response.has("nickname")) {
                        try {
                            tv_name.setText(response.getString("nickname"));
                        }catch (Exception e){
                        e.printStackTrace();
                        }
                    }
                    break;
                case 3:
               /*
               获取头像
                */
                    Bitmap bitmap = (Bitmap) msg.obj;
                    iv_head.setImageBitmap(bitmap);
                    iv_head.setVisibility(android.view.View.VISIBLE);
                    break;
                case 4:
                    /*
                    获取温度
                     */
                    tv_temperature_slip.setText(msg.obj.toString());
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (MyApplication.appConfig.isNighTheme()) {
            this.setTheme(R.style.NightTheme);
            isNight = true;
        } else {
            this.setTheme(R.style.DayTheme);
            isNight = false;
        }

        setContentView(R.layout.sideslip_layout);
        tv_address_slip = (TextView) findViewById(R.id.tv_address_slip);
        tv_weather_slip = (TextView) findViewById(R.id.tv_weather_slip);
        iv_off_slip = (ImageView) findViewById(R.id.iv_off_slip);
        tv_name = (TextView) findViewById(R.id.tv_name_slip);
        iv_head = (ImageView) findViewById(R.id.iv_avatar_slip);
        iv_weather_slip= (ImageView) findViewById(R.id.iv_weather_slip );
        iv_setting_slip= (ImageView) findViewById(R.id.iv_setting_slip);
        tv_temperature_slip= (TextView) findViewById(R.id.tv_temperature_slip);
        Guide guide = new Guide(SideslipActivity.this);
        guide.guide(handler);
        Weather weather = new Weather(SideslipActivity.this);
        weather.requestNetWork(handler, 1,tv_address_slip.getText().toString());
        weather.temperature(handler,4,tv_address_slip.getText().toString());
        iv_off_slip.setOnClickListener(this);
        iv_setting_slip.setOnClickListener(this);
        iv_head.setOnClickListener(this);
        /*
        QQ第三方登录
         */
         IUiListener listener = new IUiListener() {
                @Override
                public void onError(UiError e) {
                    // TODO Auto-generated method stub
                }
                @Override
                public void onComplete(final Object response) {
                    Message msg = new Message();
                    msg.obj = response;
                    msg.what = 2;
                    handler.sendMessage(msg);
                    new Thread() {
                        @Override
                        public void run() {
                            JSONObject json = (JSONObject) response;
                            if (json.has("figureurl")) {
                                Bitmap bitmap = null;
                                try {
                                    bitmap = Util.getbitmap(json
                                            .getString("figureurl_qq_2"));
                                } catch (JSONException e) {

                                }
                                Message msg = new Message();
                                msg.obj = bitmap;
                                msg.what = 3;
                                handler.sendMessage(msg);
                            }
                        }

                    }.start();
                }

                @Override
                public void onCancel() {
                }
            };
        if (MainApplication.qqToken!=null){
            mInfo = new UserInfo(this, MainApplication.qqToken);
            mInfo.getUserInfo(listener);
        }
           /*
                对传过来的值进行判断
                 */
            Intent intent=getIntent();
            String name = intent.getStringExtra("name");
            if (name!=null){
            tv_name.setText(name);
        }

        Intent intent1=getIntent();
        String mobile=intent1.getStringExtra("mobile");
        //隐藏手机号中间4位

        if (mobile!=null){
            String result = mobile.substring(0,3)+"****"+mobile.substring(7,mobile.length());
            tv_name.setText("手机用户"+result);
        }




    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_off_slip:
                if (choose){
                    changTheme();
                    getApplication().setTheme(R.style.NightTheme);
                    choose=false;
                }
                else {
                    changTheme();
                    getApplication().setTheme(R.style.DayTheme);
                    choose=true;

                }
                break;
            case R.id.iv_setting_slip:
                Intent intent=new Intent(this,SettingActivity.class);
                startActivity(intent);
                break;
        }

    }
    /*
    夜间模式设置
     */
    public  void changTheme(){
        if (isNight){
            MyApplication.appConfig.setNightTheme(false);
        }
        else {
            MyApplication.appConfig.setNightTheme(true);
        }
       Intent intent=getIntent();
        overridePendingTransition(0,R.anim.out_anim);
        finish();
        overridePendingTransition(R.anim.in_anim,0);
        startActivity(intent);
    }
}
