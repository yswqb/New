package com.example.anzhuo.map.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.anzhuo.map.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by anzhuo on 2016/9/8.
 */
public class WeatherActivity extends Activity {
    StringBuffer buffer;
    EditText tv;
    TextView weather;
    String url;
    Button bt;
    private static final int MSG = 1;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG:
                    try {

                        JSONObject jso = new JSONObject(buffer.toString());
                        JSONObject jsonObject1 = jso.getJSONObject("result");
                        JSONObject jsonObject2 = jsonObject1.getJSONObject("data");
                        JSONObject jsonObject3 = jsonObject2.getJSONObject("realtime");
                        JSONObject jsonObject4=jsonObject3.getJSONObject("weather");
                        String aaa = jsonObject4.getString("info");
                        weather.setText(aaa);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_layout);
        tv= (EditText) findViewById(R.id.tv);
        bt= (Button) findViewById(R.id.bt);
        weather = (TextView) findViewById(R.id.weather);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        try {
                            url = "http://op.juhe.cn/onebox/weather/query?cityname=" + URLEncoder.encode(tv.getText().toString(), "utf-8") + "&dtype=json&key=a36808314ca92f163ec77ef96438e006";
                            requestNetWork(url);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        });

    }

    private void requestNetWork(String url) {
        try {
            URL urlstr = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) urlstr.openConnection();
            InputStream in = new BufferedInputStream(connection.getInputStream());
            buffer = new StringBuffer();
            byte[] b = new byte[10 * 1024];
            int len;
            Log.i("LT", buffer.toString());
            while ((len = in.read(b)) != -1) {
                buffer.append(new String(b, 0, len));
            }
            in.close();
            handler.sendEmptyMessage(MSG);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
