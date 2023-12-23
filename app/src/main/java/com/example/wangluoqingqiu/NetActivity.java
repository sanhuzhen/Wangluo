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
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class NetActivity extends AppCompatActivity {
    private TextView mTvData;
    private Button mBtnSend;
    private String mPostUrl = "https://www.wanandroid.com/banner/json";
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
// sendGetRequest(mGetUrl);
                HashMap<String,String> params = new HashMap<>();
                params.put("username","薛暗浊");
                params.put("password","123456");
                params.put("repassword","123456");
                sendPostRequest(mPostUrl,params);
            }
        });
    }
    private void sendPostRequest(String theUrl,HashMap<String,String> params){
        new Thread(
                () -> {
                    try {
                        URL url = new URL(theUrl);
                        HttpURLConnection connection = (HttpURLConnection)
                                url.openConnection();
                        connection.setRequestMethod("POST");//设置请求⽅式为POST
                        connection.setConnectTimeout(8000);//设置最⼤连接时间，单位为ms
                        connection.setReadTimeout(8000);//设置最⼤的读取时间，单位为ms
                        connection.setDoOutput(true);//允许输⼊流
                        connection.setDoInput(true);//允许输出流
                        StringBuilder dataToWrite = new StringBuilder();//构建参数值
                        for (String key : params.keySet()) {
                            dataToWrite.append(key).append("=").append(params.get(key)).append("&");
                        }
                        connection.connect();//正式连接
                        OutputStream outputStream = connection.getOutputStream();//开
                        outputStream.write(dataToWrite.substring(0,
                                dataToWrite.length() - 1).getBytes());//去除最后⼀个&
                        InputStream in = connection.getInputStream();//从接⼝处获取输⼊
                        String responseData = StreamToString(in);//这⾥就是服务器返回的
                        Log.d("lx", "sendPostNetRequest: "+responseData);
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
