package com.wei.customviews.view.activity;


import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.wei.customviews.R;
import com.wei.customviews.model.bean.IpModel;
import com.wei.customviews.service.MyService;
import com.wei.customviews.view.AppBaseActivity;
import com.wei.utillibrary.utils.LogUtil;

import org.androidannotations.annotations.EActivity;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static android.R.attr.country;

@EActivity(R.layout.activity_ok_http)
public class HttpActivity extends AppBaseActivity
{
    private MyService.MyBinder mMyBinder;

    @Override
    protected void onResume() {
        super.onResume();
//        getHttp();
//        getRetrofit();
    }

    private void getRetrofit()
    {
        String url = "http://ip.taobao.com/service/"; //getIpInfo.php?ip=61.144.145.42";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
//        这里的baseUrl加上之前@GET(“getIpInfo.php”)定义的参数形成完整的请求地址；
//       addConverterFactory用于指定返回的参数数据类型，这里我们支持String和Gson类型。
        IpService ipService = retrofit.create(IpService.class);
        retrofit2.Call<IpModel> call = ipService.getIpMsg("61.144.145.42");
        call.enqueue(new retrofit2.Callback<IpModel>() {
            @Override
            public void onResponse(retrofit2.Call<IpModel> call, retrofit2.Response<IpModel> response) {
                String country = response.body().getData().getCountry();
                LogUtil.e(TAG, "country : " + country);
                showMsg(country);
            }

            @Override
            public void onFailure(retrofit2.Call<IpModel> call, Throwable throwable) {
                LogUtil.e(TAG, throwable.getMessage());
            }
        });
    }

    private void getHttp()
    {
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url("http://liuwangshu.cn/application/network/5-okhttp2x.html")
                .build();
        Call call = okHttpClient.newCall(request);
        // 异步请求
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String s = response.body().string();
                Log.e(TAG, s);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showMsg("请求成功！");
                    }
                });
            }
        });
    }

    public void startService(View view)
    {
        Intent intent_start = new Intent(this, MyService.class);
        startService(intent_start);
    }

    public void stopService(View view)
    {
        Intent intent_start = new Intent(this, MyService.class);
        stopService(intent_start);
    }

    public void bindService(View view)
    {
        Intent intent_bind = new Intent(this, MyService.class);
        bindService(intent_bind, mServiceConnection, BIND_AUTO_CREATE);
    }

    public void unbindService(View view)
    {
        unbindService(mServiceConnection);
    }

    ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            LogUtil.e(TAG, "--- onServiceConnected ---");
            mMyBinder = (MyService.MyBinder) iBinder;
            try {
                Thread.sleep(2000);
                LogUtil.e(TAG, "currentcount is : " + mMyBinder.getCount());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            LogUtil.e(TAG, "--- onServiceDisconnected ---");
        }
    };
}
