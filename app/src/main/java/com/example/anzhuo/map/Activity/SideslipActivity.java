package com.example.anzhuo.map.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.example.anzhuo.map.R;

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
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by anzhuo on 2016/9/9.
 */
public class SideslipActivity extends Activity implements AMapLocationListener{
    public AMapLocationClient mLocationClient=null;
    public AMapLocationClientOption mLocationOption=null;
    StringBuffer buffer;
    String url;
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
                        Log.i("LT",aaa);



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
        setContentView(R.layout.sideslip_layout);
        mLocationClient=new AMapLocationClient(getApplicationContext());
        mLocationClient.setLocationListener(this);
        mLocationOption=new AMapLocationClientOption();
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        mLocationOption.setOnceLocation(true);
        mLocationOption.setOnceLocationLatest(true);
        mLocationOption.setInterval(1000);
        mLocationOption.setNeedAddress(true);
        mLocationOption.setWifiActiveScan(false);
        mLocationOption.setMockEnable(false);
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);
        mLocationClient.setLocationOption(mLocationOption);
        mLocationClient.startLocation();
        new Thread() {
            @Override
            public void run() {

                super.run();
                try {
                    url = "http://op.juhe.cn/onebox/weather/query?cityname=" + URLEncoder.encode("武汉", "utf-8") + "&dtype=json&key=a36808314ca92f163ec77ef96438e006";
                    requestNetWork(url);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }.start();



    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                aMapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                aMapLocation.getLatitude();//获取纬度
                aMapLocation.getLongitude();//获取经度
                aMapLocation.getAccuracy();//获取精度信息
                aMapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                aMapLocation.getCountry();//国家信息
                aMapLocation.getProvince();//省信息
                aMapLocation.getCity();//城市信息
                aMapLocation.getDistrict();//城区信息
                aMapLocation.getStreet();//街道信息
                aMapLocation.getStreetNum();//街道门牌号信息
                aMapLocation.getCityCode();//城市编码
                aMapLocation.getAdCode();//地区编码
                aMapLocation.getAoiName();//获取当前定位点的AOI信息
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date( aMapLocation.getTime());
                df.format(date);
                Toast.makeText(SideslipActivity.this,  df.format(date),Toast.LENGTH_SHORT).show();

                Log.i("LT",   aMapLocation.getCity()+ aMapLocation.getDistrict()+ aMapLocation.getStreet()+aMapLocation.getStreetNum());
//可在其中解析amapLocation获取相应内容。
            }else {
                //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                Log.i("LT","location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
            }
        }


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
