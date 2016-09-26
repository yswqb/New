package com.example.anzhuo.map.activity.LT;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.anzhuo.map.R;

/**
 * Created by anzhuo on 2016/9/22.
 */
public class OpinionActivity extends Activity implements View.OnClickListener {
    ImageView iv_back_opinion;
    Button bt_send_opinion;
    EditText et_content_opinion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.opinion_layout);
        iv_back_opinion= (ImageView) findViewById(R.id.iv_back_opinion);
        bt_send_opinion= (Button) findViewById(R.id.bt_send_opinion);
        et_content_opinion= (EditText) findViewById(R.id.et_content_opinion);
        iv_back_opinion.setOnClickListener(this);
        bt_send_opinion.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back_opinion:
                finish();
                break;
            case R.id.bt_send_opinion:
                if (et_content_opinion.getText().toString().equals("")){
                    Toast.makeText(this,"内容不能为空......",Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(this,"发送成功！",Toast.LENGTH_SHORT).show();
                   finish();
                }

                break;
        }
    }
}
