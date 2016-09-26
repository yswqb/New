package com.example.anzhuo.map.activity.LT;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anzhuo.map.net.Bomb;
import com.example.anzhuo.map.utils.MainApplication;
import com.example.anzhuo.map.R;
import com.example.anzhuo.map.utils.Util;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQAuth;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

/**
 * Created by anzhuo on 2016/9/14.
 */
public class LoginActivity extends Activity implements View.OnClickListener {
    ImageView iv_back_login;
    ImageView iv_user_login;
    EditText et_user_login;
    ImageView iv_key_login;
    EditText et_pass_login;
    TextView tv_forget_login;
    Button bt_login_login;
    TextView tv_qq_login;
    TextView tv_weixin_login;
    TextView tv_register_login;
    private static final String TAG = LoginActivity.class.getName();
    public static String mAppid;
    public static QQAuth mQQAuth;
    private Tencent mTencent;
    private final String APP_ID = "1105627007";// 测试时使用，真正发布的时候要换成自己的APP_ID
    String name;
    String pass;
    String mobile;
    private int START;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "-->onCreate");


        // 固定竖屏
        setContentView(R.layout.login_layout);
        iv_back_login = (ImageView) findViewById(R.id.iv_back_login);
        iv_user_login = (ImageView) findViewById(R.id.iv_user_login);
        et_user_login = (EditText) findViewById(R.id.et_user_login);
        iv_key_login = (ImageView) findViewById(R.id.iv_key_login);
        et_pass_login = (EditText) findViewById(R.id.et_pass_login);
        tv_forget_login = (TextView) findViewById(R.id.tv_forget_login);
        bt_login_login = (Button) findViewById(R.id.bt_login_login);
        tv_qq_login = (TextView) findViewById(R.id.tv_qq_login);
        tv_weixin_login = (TextView) findViewById(R.id.tv_weixin_login);
        tv_register_login = (TextView) findViewById(R.id.tv_register_login);
        iv_back_login.setOnClickListener(this);
        iv_user_login.setOnClickListener(this);
        et_user_login.setOnClickListener(this);
        iv_key_login.setOnClickListener(this);
        et_pass_login.setOnClickListener(this);
        tv_forget_login.setOnClickListener(this);
        bt_login_login.setOnClickListener(this);
        tv_qq_login.setOnClickListener(new NewClickListener());
        tv_weixin_login.setOnClickListener(this);
        tv_register_login.setOnClickListener(this);
        initViews();
        /*
        改变字体颜色
         */
        tv_register_login.setText(Html.fromHtml("没有账号？<font color='#ff0000'><middle><middle>快来注册一个吧！</big></big></font>"));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back_login:
                finish();
                break;
            case R.id.bt_login_login:
                Toast.makeText(LoginActivity.this,"正在登录中....",Toast.LENGTH_SHORT).show();
                   /*
        读取后端云表格信息
         */
                Bmob.initialize(this, "7f0755d5c291508536e6400043667e1e");
                BmobQuery n = new BmobQuery("News");
                n.findObjectsByTable(new QueryListener<JSONArray>() {
                    @Override
                    public void done(JSONArray jsonArray, BmobException e) {
                        if (e == null) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                try {
                                    JSONObject json = (JSONObject) jsonArray.get(i);
                                    name = json.getString("name");
                                    pass = json.getString("password");
                                    mobile = json.getString("mobile");
                                    Log.i("LT",name);
                                } catch (JSONException e1) {
                                    e1.printStackTrace();
                                }

                                if (et_user_login.getText().toString().equals(name)||et_user_login.getText().toString().equals(mobile)&&et_pass_login.getText().toString().equals(pass)){

                                    Intent intent=new Intent(LoginActivity.this,SideslipActivity.class);
                                   intent.putExtra("name",name);
                                    startActivity(intent);
                                    Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
                                    START=2;
                                }
                            }
                        }
                        if (START!=2){
                            Toast.makeText(LoginActivity.this,"账号或密码错误",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            case R.id.tv_register_login:
                Intent intent=new Intent(LoginActivity.this,RegiseterActivity.class);
                startActivity(intent);
                break;
        }
    }

    protected void onStart() {
        Log.d(TAG, "-->onStart");
        final Context context = LoginActivity.this;
        final Context ctxContext = context.getApplicationContext();
        mAppid = APP_ID;
        mQQAuth = QQAuth.createInstance(mAppid, ctxContext);
        mTencent = Tencent.createInstance(mAppid, LoginActivity.this);
        super.onStart();
    }

    private void initViews() {
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.main_container);
        View.OnClickListener listener = new NewClickListener();
        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            View view = linearLayout.getChildAt(i);
            if (view instanceof Button) {
                view.setOnClickListener(listener);
            }

        }
    }

    private void updateUserInfo() {
        if (mQQAuth != null && mQQAuth.isSessionValid()) {
            MainApplication.qqToken = mQQAuth.getQQToken();
            Intent intent = new Intent(LoginActivity.this, SideslipActivity.class);
            startActivity(intent);
        }
    }

    private void onClickLogin() {
        if (!mQQAuth.isSessionValid()) {
            IUiListener listener = new BaseUiListener();
            mQQAuth.login(this, "all", listener);
            // mTencent.loginWithOEM(this, "all",
            // listener,"10000144","10000144","xxxx");
            mTencent.login(this, "all", listener);
        } else {
            mQQAuth.logout(this);
            updateUserInfo();
        }
    }

    public static boolean ready(Context context) {
        if (mQQAuth == null) {
            return false;
        }
        boolean ready = mQQAuth.isSessionValid()
                && mQQAuth.getQQToken().getOpenId() != null;
        if (!ready)
            Toast.makeText(context, "login and get openId first, please!",
                    Toast.LENGTH_SHORT).show();
        return ready;
    }

    private class BaseUiListener implements IUiListener {
        @Override
        public void onComplete(Object response) {
          /*  Util.showResultDialog(LoginActivity.this, response.toString(),
                    "登录成功");*/
            updateUserInfo();
        }

        protected void doComplete(JSONObject values) {
        }

        @Override
        public void onError(UiError e) {
            Util.toastMessage(LoginActivity.this, "onError: " + e.errorDetail);
            Util.dismissDialog();
        }

        @Override
        public void onCancel() {
            Util.toastMessage(LoginActivity.this, "onCancel: ");
            Util.dismissDialog();
        }
    }

    class NewClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Context context = v.getContext();
            Class<?> cls = null;
            switch (v.getId()) {
                case R.id.tv_qq_login:
                    Toast.makeText(LoginActivity.this, "123456", Toast.LENGTH_SHORT).show();
                    onClickLogin();
                    return;
            }
            if (cls != null) {
                Intent intent = new Intent(context, cls);
                context.startActivity(intent);
            }
        }

    }
}
