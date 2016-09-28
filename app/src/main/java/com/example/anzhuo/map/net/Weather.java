package com.example.anzhuo.map.net;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by anzhuo on 2016/9/12.
 */
public class Weather {
    StringBuffer buffer;
    Context mcontext;
    String url;
    private static final int MSG = 1;
    public Weather(Context context){
        this.mcontext=context;

    }
    public void requestNetWork(final Handler handler, final int what, final String adrass) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    url = "http://op.juhe.cn/onebox/weather/query?cityname=" + URLEncoder.encode(adrass, "utf-8") + "&dtype=json&key=a36808314ca92f163ec77ef96438e006";
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
                    JSONObject jso = new JSONObject(buffer.toString());
                    JSONObject jsonObject1 = jso.getJSONObject("result");
                    JSONObject jsonObject2 = jsonObject1.getJSONObject("data");
                    JSONObject jsonObject3 = jsonObject2.getJSONObject("realtime");
                    JSONObject jsonObject4 = jsonObject3.getJSONObject("weather");
                    String weather = jsonObject4.getString("info");
                    handler.sendMessage(Message.obtain(handler,what,weather));



                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }
    public void temperature(final Handler handler, final int what, final String adrass) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    url = "http://op.juhe.cn/onebox/weather/query?cityname=" + URLEncoder.encode(adrass, "utf-8") + "&dtype=json&key=a36808314ca92f163ec77ef96438e006";
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
                    JSONObject jso = new JSONObject(buffer.toString());
                    JSONObject jsonObject1 = jso.getJSONObject("result");
                    JSONObject jsonObject2 = jsonObject1.getJSONObject("data");
                    JSONArray jsa=jsonObject2.optJSONArray("weather");
                    JSONObject jso1=jsa.getJSONObject(0);
                    JSONObject jso2=jso1.getJSONObject("info");
                    JSONArray jsa1=jso2.optJSONArray("day");
                    JSONArray jsa2=jso2.getJSONArray("night");
                    String a=jsa1.getString(2);
                    String c=jsa2.getString(2);
                    String temperature=c+"℃"+"-"+a+"℃";
                    handler.sendMessage(Message.obtain(handler,what,temperature));
                    Log.i("LT",temperature.toString());

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }

}
