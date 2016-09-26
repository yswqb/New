package com.example.anzhuo.map.net;

import android.content.Context;
import android.util.Log;

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
 * Created by anzhuo on 2016/9/19.
 */
public class Bomb {
    Context mcontext;
    String name;
    String pass;
    String mobile;

    public Bomb(Context context) {
        this.mcontext = context;

    }

    public void register(String name, String mobile, String password) {
        Bmob.initialize(mcontext, "7f0755d5c291508536e6400043667e1e");
        News news = new News();
        news.setName(name);
        news.setMobile(mobile);
        news.setPassword(password);
        news.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    Log.i("LT", "添加数据成功，返回objectId为：" + s);
                } else {
                    Log.i("LT", "创建数据失败：" + e.getMessage());
                }
            }
        });
    }

    public String login() {
        Bmob.initialize(mcontext, "7f0755d5c291508536e6400043667e1e");
        BmobQuery n = new BmobQuery("News");
        n.findObjectsByTable(new QueryListener<JSONArray>() {
            @Override
            public void done(JSONArray jsonArray, BmobException e) {
                if (e == null) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        try {
                            JSONObject json = (JSONObject) jsonArray.get(i);
                            name = json.getString("name");
                             pass=json.getString("password");
                            mobile=json.getString("mobile");
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }

                    }
                }
            }
        });
        return name;
    }

    public class News extends BmobObject {
        private String name;
        private String mobile;
        private String password;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

}
