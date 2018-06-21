package com.hale.aimoney.main.activity;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.hale.aimoney.R;
import com.hale.aimoney.common.CommonConfig;
import com.hale.aimoney.main.adapter.MainAdapter;
import com.hale.aimoney.main.receiver.MoneyChangedReceiver;
import com.hale.bishousu.activity.BishousuActivity;
import com.hale.common.FileUtil;
import com.hale.common.Util;
import com.hale.pinrenpin.PinRenPinActivity;

import java.io.File;
import java.math.BigDecimal;
import java.util.Date;

import static com.hale.aimoney.R.id.adView;
import static com.hale.aimoney.R.id.logo;

public class MainActivity extends Activity {

    private ListView mLV;
    private TextView mTV;
    private AdView   mAdView;
    private InterstitialAd mADV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        MobileAds.initialize(this,"ca-app-pub-5096850339378215~3460463593");


        mTV = (TextView) findViewById(R.id.titleTV);
        mLV = (ListView) findViewById(R.id.lv);
        mAdView = (AdView) findViewById(adView);
        AdRequest request =  new AdRequest.Builder().build();
        mAdView.loadAd(request);


        MainAdapter adapter = new MainAdapter(this);
        mLV.setAdapter(adapter);


        String totalMoney = FileUtil.read(CommonConfig.kMoneyKey,this);
        float money = 0.0f;
        if(totalMoney.length() > 0)
        {
            money = Float.valueOf(totalMoney);
            BigDecimal b  =   new  BigDecimal(money);
            money   =  b.setScale(2,  BigDecimal.ROUND_HALF_UP).floatValue();
        }
        mTV.setText("你已获得" + money + "积分");

        mADV = new InterstitialAd(MainActivity.this);
        mADV.setAdUnitId("ca-app-pub-5096850339378215/5811294762");
        mADV.loadAd(new AdRequest.Builder().build());
        mADV.setAdListener(new AdListener(){
            @Override
            public void onAdLoaded()
            {
                Log.i("hh","加载成功");
            }
            @Override
            public void onAdFailedToLoad(int errorCode)
            {
                Log.i("hh","错误码"+errorCode);
            }
        });

        mLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch (position)
                {
                    case 0:
                    {
                        Intent intent = new Intent(MainActivity.this, BishousuActivity.class);
                        startActivity(intent);
                    }
                        break;
                    case 1:
                    {
                        Intent intent = new Intent(MainActivity.this, PinRenPinActivity.class);
                        startActivity(intent);
                    }
                        break;
                }
            }
        });
        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message message){
                float obj = (float)message.obj;
                BigDecimal b  =   new  BigDecimal(obj);
                obj   =  b.setScale(2,  BigDecimal.ROUND_HALF_UP).floatValue();
                mTV.setText("你已获得" + obj + "积分");

            }
        };

        IntentFilter filter = new IntentFilter();
        filter.addAction("MoneyChanged");
        MoneyChangedReceiver receiver = new MoneyChangedReceiver(handler);
        registerReceiver(receiver,filter);

        String lastTimeStr = FileUtil.read(CommonConfig.kLastTimestampKey,this);
        long time = 0;
        if(lastTimeStr.length() > 0)
        {
            time = Long.valueOf(time);
        }
        if(time == 0)
        {
            FileUtil.save(CommonConfig.kLastTimestampKey,System.currentTimeMillis() + "",this);
        }else
        {
            Date lastDate = new Date(time);
            if(!Util.isSameDay(lastDate,new Date()))
            {
                FileUtil.save("kBiShouSuNumKey","0",this);
                FileUtil.save("kPinRenPinNumKey","0",this);
            }
        }




    }
    public void getMoney(View view){
        float money = Float.valueOf(FileUtil.read(CommonConfig.kMoneyKey,this));
        if(money < 20)
        {
            Toast.makeText(MainActivity.this,"继续加油，马上可以提现喽",Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        // 过滤按键动作
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);

        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mADV.isLoaded())
        {
            mADV.show();
        }else
        {
            Log.i("hh","广告加载失败");
        }

    }
    @Override
    protected void onStart()
    {
        super.onStart();
        mADV.loadAd(new AdRequest.Builder().build());
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("hh","onDestory");
    }
}
