package com.example.anzhuo.map.activity.LT;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.anzhuo.map.R;
import com.example.anzhuo.map.net.Bomb;
import com.example.anzhuo.map.utils.MyApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

/**
 * Created by anzhuo on 2016/9/19.
 */
public class RegiseterActivity extends Activity implements View.OnClickListener {
    ImageView iv_back_register;
    EditText et_name_register;
    EditText et_password_register;
    EditText et_passagain_register;
    Button bt_register;
    String name;
    String mobile;
    private int START = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (MyApplication.appConfig.isNighTheme()) {
            this.setTheme(R.style.NightTheme);
        } else {
            this.setTheme(R.style.DayTheme);
        }
        setContentView(R.layout.register_layout);
        iv_back_register = (ImageView) findViewById(R.id.iv_back_register);
        et_name_register = (EditText) findViewById(R.id.et_name_register);
        et_password_register = (EditText) findViewById(R.id.et_password_register);
        et_passagain_register = (EditText) findViewById(R.id.et_passagain_register);
        bt_register = (Button) findViewById(R.id.bt_register);
        iv_back_register.setOnClickListener(this);
        bt_register.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back_register:
                finish();
                break;
            case R.id.bt_register:
                /*
        读取后端云表格数据
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
                                    mobile = json.getString("mobile");
                                } catch (JSONException e1) {
                                    e1.printStackTrace();
                                }
                                if (et_name_register.getText().toString().equals(name)) {
                                    Toast.makeText(RegiseterActivity.this, "用户名已存在", Toast.LENGTH_SHORT).show();
                                    START = 1;
                                }

                                else {
                                    START=2;
                                }

                              /*  if (!et_passagain_register.getText().toString().equals(et_password_register.getText().toString())) {
                                    Toast.makeText(RegiseterActivity.this, "密码不一致，请重新输入", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    if (et_name_register.getText().equals(name)) {
                                        Toast.makeText(RegiseterActivity.this, "用户名已存在", Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        if (et_mobile_register.getText().toString().equals(mobile)) {
                                            Toast.makeText(RegiseterActivity.this, "该手机号已注册！", Toast.LENGTH_SHORT).show();
                                        }
                                        else {
                                                Bomb bomb = new Bomb(RegiseterActivity.this);
                                                bomb.register(et_name_register.getText().toString(), et_mobile_register.getText().toString(), et_password_register.getText().toString());
                                                Toast.makeText(RegiseterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                                                finish();

                                        }
                                    }
                                }

                              */


                            }
                        }
                        if (START != 1) {
                            if (!et_passagain_register.getText().toString().equals(et_password_register.getText().toString())) {
                                Toast.makeText(RegiseterActivity.this, "密码不一致，请重新输入", Toast.LENGTH_SHORT).show();
                            }
                            else if (et_name_register.getText().toString().equals("")||et_password_register.getText().toString().equals("")){
                                Toast.makeText(RegiseterActivity.this, "请填写完整信息！", Toast.LENGTH_SHORT).show();
                            }
                            //只能输入汉字和英文"[^a-zA-Z0-9\u4E00-\u9FA5]";
                         /*   else if (!et_password_register.getText().toString().matches("[^a-zA-Z0-9\u4E00-\u9FA5]")){
                                Log.i("LT",et_name_register.getText().toString());
                                Toast.makeText(RegiseterActivity.this, "用户名格式不对!", Toast.LENGTH_SHORT).show();
                            }*/
                           else{
                                Bomb bomb = new Bomb(RegiseterActivity.this);
                                bomb.register(et_name_register.getText().toString(), et_password_register.getText().toString());
                                Toast.makeText(RegiseterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    }
                });

                break;


        }

    }
}

