package com.example.anzhuo.map.activity.LT;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.example.anzhuo.map.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by anzhuo on 2016/9/9.
 */
public class BombActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bomb_layout);
        Bmob.initialize(this,"7f0755d5c291508536e6400043667e1e");
        Person p=new Person();
        p.setName("刘涛");
        p.setAddress("黄石");
        p.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
if (e==null){
    Log.i("LT","添加数据成功，返回objectId为："+s);
}
                else {
    Log.i("LT","创建数据失败：" + e.getMessage());
}
            }
        });

        BmobQuery person=new BmobQuery("Person");
        person.findObjectsByTable(new QueryListener<JSONArray>() {
            @Override
            public void done(JSONArray jsonArray, BmobException e) {


                  if(e==null){
                      for(int i=0;i<jsonArray.length();i++){
                          try {
                              JSONObject json= (JSONObject) jsonArray.get(i);

                              String  name=json.getString("name");
                              Log.i("LT",name);
                          } catch (JSONException e1) {
                              e1.printStackTrace();
                          }
                      }
                      Log.i("LT",jsonArray.toString());

                  }
            }
        });
    }
    public class Person extends BmobObject {
        private String name;
        private String address;

        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public String getAddress() {
            return address;
        }
        public void setAddress(String address) {
            this.address = address;
        }
    }



}
