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

import com.hale.aimoney.R;
import com.hale.aimoney.common.CommonConfig;
import com.hale.aimoney.main.adapter.MainAdapter;
import com.hale.aimoney.main.receiver.MoneyChangedReceiver;
import com.hale.bishousu.activity.BishousuActivity;

public class MainActivity extends Activity {

    private ListView mLV;
    private TextView mTV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTV = (TextView) findViewById(R.id.titleTV);
        mLV = (ListView) findViewById(R.id.lv);
        MainAdapter adapter = new MainAdapter(this);
        mLV.setAdapter(adapter);
        SharedPreferences sharedPreferences = getSharedPreferences("setting",0);
        if(sharedPreferences != null)
        {
            float money = sharedPreferences.getFloat(CommonConfig.kMoneyKey,0);
            mTV.setText("你已获得" + money + "积分");
        }else
        {
            mTV.setText("你已获得0.00积分");
        }
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

                    }
                        break;
                }
            }
        });
        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message message){
                float obj = (float)message.obj;
                mTV.setText("你已获得" + obj + "积分");

            }
        };

        IntentFilter filter = new IntentFilter();
        filter.addAction("MoneyChanged");
        MoneyChangedReceiver receiver = new MoneyChangedReceiver(handler);
        registerReceiver(receiver,filter);

    }
    public void getMoney(View view){

        SharedPreferences sharedPreferences = getSharedPreferences("setting",0);
        if(sharedPreferences != null)
        {
            float money = sharedPreferences.getFloat(CommonConfig.kMoneyKey,0);
            if(money < 20)
            {
                Toast.makeText(MainActivity.this,"继续加油，马上可以提现喽",Toast.LENGTH_LONG).show();
            }
        }else
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
    protected void onDestroy() {
        super.onDestroy();
        Log.i("hh","onDestory");
    }
}
