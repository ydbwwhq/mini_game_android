package com.hale.bishousu.activity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.hale.bishousu.R;
import com.hale.bishousu.receiver.TimerReceiver;
import com.hale.config.BishousuConfig;

import java.util.Date;
import java.util.Random;

public class BishousuActivity extends Activity {

    private TextView mTimeTV;
    private Button   mBlueBtn;
    private TextView mNum1TV;
    private TextView mNum2TV;
    private Button   mStartBtn;

    private int  mANum = 0;
    private int  mBNum = 0;
    private int  mSecond = 0;
    private int  mMaxSec = 0;

    private boolean mIsPlaying = false;
    private Handler mTimerHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bishousu);

        mTimeTV  = (TextView) findViewById(R.id.timeTV);
        mBlueBtn = (Button) findViewById(R.id.blueBtn);
        mNum1TV  = (TextView)findViewById(R.id.num1TV);
        mNum2TV  = (TextView)findViewById(R.id.num2TV);
        mStartBtn= (Button) findViewById(R.id.startBtn);

        IntentFilter filter = new IntentFilter();
        filter.addAction("ABC");
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message message)
            {
                super.handleMessage(message);
                setmBNum(++mBNum);
            }
        };
//        registerReceiver(new TimerReceiver(handler),filter);



        mTimerHandler = new Handler(){
          @Override
          public void handleMessage(Message message)
          {
              super.handleMessage(message);
              switch (message.what)
              {
                  case BishousuConfig.kWhatFindOpponent:
                  {
                      mTimeTV.setText("匹配成功");
                      mTimerHandler.sendEmptyMessageDelayed(BishousuConfig.kWhatReadyToPlay,1000);
                  }
                  break;
                  case BishousuConfig.kWhatReadyToPlay:
                  {
                      Log.i("hh","收到消息");
                      Random random = new Random();
                      int randSec = random.nextInt(45 - 10 + 1) + 10;
                      mSecond = randSec;
                      mMaxSec = randSec;
                      mTimeTV.setText("" + randSec);

                      mTimerHandler.sendEmptyMessageDelayed(BishousuConfig.kWhatCountDown,1000);
                  }
                  break;
                  case BishousuConfig.kWhatCountDown:
                  {
                      if(mSecond > 0) {
                          mSecond--;
                          mTimeTV.setText(mSecond + "");
                          mTimerHandler.sendEmptyMessageDelayed(BishousuConfig.kWhatCountDown,1000);
                          if(mSecond == mMaxSec - 1)
                          {
                              Random random = new Random();
                              int randSec = random.nextInt(200 - 100 + 1) + 100;
                              mTimerHandler.sendEmptyMessageDelayed(BishousuConfig.kWhatMachineClick,randSec);
                              mIsPlaying = true;
                              mStartBtn.setText("进行中...");
                          }
                      }else
                      {
                          mIsPlaying = false;
                          mStartBtn.setText("开始");
                          if(mANum < mBNum)
                          {
                              Toast.makeText(BishousuActivity.this,"😂😂😂你输了😂😂😂",Toast.LENGTH_LONG).show();
                          }else if(mANum == mBNum)
                          {
                              Toast.makeText(BishousuActivity.this,"😊😊😊平局😊😊😊",Toast.LENGTH_LONG).show();
                          }else
                          {
                              Toast.makeText(BishousuActivity.this,"🎉🎉🎉你赢了🎉🎉🎉",Toast.LENGTH_LONG).show();
                          }
                          mANum = 0 ;
                          mBNum = 0;

                      }
                  }
                  break;
                  case BishousuConfig.kWhatMachineClick:
                  {
                      if(mIsPlaying)
                      {
                          mBNum ++ ;
                          mNum2TV.setText(mBNum + "");
                          Random random = new Random();
                          int randSec = random.nextInt(200 - 100 + 1) + 100;
                          mTimerHandler.sendEmptyMessageDelayed(BishousuConfig.kWhatMachineClick,randSec);
                      }

                  }
                  break;
              }
          }
        };

    }
    public void touchBlueBtn(View view)
    {
        if(mIsPlaying)
        {
            mANum ++ ;
            mNum1TV.setText("" + mANum);
        }
    }
    public void touchBack(View view)
    {
        this.finish();
    }
    public void touchStartBtn(View view)
    {
        mTimeTV.setText("正在寻找对手...");
        Random rand = new Random();
        int sec = rand.nextInt(3)+1;
        Log.i("hh",sec  + "");
        mTimerHandler.sendEmptyMessageDelayed(BishousuConfig.kWhatFindOpponent,sec * 1000);
    }
    public  void play()
    {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent("ABC");
        PendingIntent sender = PendingIntent.getBroadcast(this,0,intent,0);
        alarmManager.setRepeating(AlarmManager.RTC,System.currentTimeMillis(),1000,sender);
        sendBroadcast(intent);
    }
    public void setmANum(int num)
    {
        this.mANum = num;
    }
    public void setmBNum(int num)
    {
        this.mBNum = num;
        mNum2TV.setText(num + "");
    }
}
