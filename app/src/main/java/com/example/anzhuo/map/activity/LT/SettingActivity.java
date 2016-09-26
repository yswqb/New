package com.example.anzhuo.map.activity.LT;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.print.PrintJob;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anzhuo.map.R;
import com.example.anzhuo.map.utils.MyApplication;

/**
 * Created by anzhuo on 2016/9/12.
 */
public class SettingActivity extends Activity implements View.OnClickListener{
    ImageView iv_back_setting;
    TextView tv_clear_setting;
    TextView tv_opinion_setting;
    TextView tv_new_setting;
    TextView tv_change_setting;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (MyApplication.appConfig.isNighTheme()) {
            this.setTheme(R.style.NightTheme);
        } else {
            this.setTheme(R.style.DayTheme);
        }
        setContentView(R.layout.setting_layout);
        iv_back_setting= (ImageView) findViewById(R.id.iv_back_setting);
        tv_clear_setting= (TextView) findViewById(R.id.tv_clear_setting);
        tv_change_setting= (TextView) findViewById(R.id.tv_change_setting);
        tv_opinion_setting= (TextView) findViewById(R.id.tv_opinion_setting);
        tv_new_setting= (TextView) findViewById(R.id.tv_new_setting);
        iv_back_setting.setOnClickListener(this);
         tv_clear_setting.setOnClickListener(this);
        tv_change_setting.setOnClickListener(this);
        tv_opinion_setting.setOnClickListener(this);
        tv_new_setting.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_opinion_setting:
                Intent intent=new Intent(this,OpinionActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_back_setting:
                finish();
                break;
            case R.id.tv_clear_setting:
              dialog();
                break;
            case R.id.tv_change_setting:
                Intent intent1=new Intent(this,LoginActivity.class);
                startActivity(intent1);
                break;
            case R.id.tv_new_setting:
                Toast.makeText(this,"还没有新版本！",Toast.LENGTH_SHORT).show();
                break;
        }



    }
    private void dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("极速通讯");
        builder.setMessage("点击确认后，你的收藏信息·历史记录将清除，确定要清除缓存吗？");
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(SettingActivity.this, "清除成功！", Toast.LENGTH_SHORT).show();

            }
        });

        builder.setNeutralButton("退出", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });


        AlertDialog dialog = builder.create();
        dialog.show();



    }
}
