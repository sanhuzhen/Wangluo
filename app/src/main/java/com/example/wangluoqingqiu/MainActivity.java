package com.example.wangluoqingqiu;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private TextView mTvData;
    private Button mBtnSend;
    private String mUrl = "https://www.wanandroid.com/banner/json";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net);
        initView();
        initClick();
    }
    private void initView() {
        mTvData = findViewById(R.id.main_activity_net_tv_data);
        mBtnSend = findViewById(R.id.main_activity_net_btn_send);
    }
    private void initClick() {
        mBtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
// ⽹络请求
                sendGetRequest(mUrl);
            }
        });
    }
    private void sendGetRequest(String mUrl){
//lambda表达式，相当于其中new Runnable并且重写⽅法
        new Thread(
                () -> {
                    try {
                        URL url = new URL(mUrl);
                        HttpURLConnection connection = (HttpURLConnection)
                                url.openConnection();
                        connection.setRequestMethod("GET");//设置请求⽅式为GET
                        connection.setConnectTimeout(8000);//设置最⼤连接时间，单位
                        connection.setReadTimeout(8000);//设置最⼤的读取时间，单位为
                        connection.setRequestProperty("Accept-Language",
                                "zh-CN,zh;q=0.9");
                        connection.setRequestProperty("Accept-Encoding",
                                "gzip,deflate");
                        connection.connect();//正式连接
                        InputStream in = connection.getInputStream();//从接⼝处获取
                        String responseData = StreamToString(in);//这⾥就是服务器返
                        Log.d("lx", "sendGetNetRequest: "+responseData);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        ).start();
    }
    private String StreamToString(InputStream in) {
        StringBuilder sb = new StringBuilder();//新建⼀个StringBuilder，⽤于⼀点⼀点
        String oneLine;//流转换为字符串的⼀⾏
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        try {
            while ((oneLine = reader.readLine()) != null) {//readLine⽅法将读取⼀⾏
                sb.append(oneLine).append('\n');//拼接字符串并且增加换⾏，提⾼可读性
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();//关闭InputStream
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();//将拼接好的字符串返回出去
    }
}