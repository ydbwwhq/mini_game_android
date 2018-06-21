package com.hale.bishousu.activity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.hale.bishousu.R;

import com.hale.common.FileUtil;
import com.hale.config.BishousuConfig;

import java.math.BigDecimal;
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

    private float mTotalScore = 0;
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
                      Random random = new Random();
                      int minSec = 0;
                      int maxSec = 10;
                      if (mTotalScore < 5) {
                          minSec = 10;
                          maxSec = 15;
                      } else if (mTotalScore < 10) {
                          minSec = 10;
                          maxSec = 20;
                      } else if (mTotalScore < 15) {
                          minSec = 20;
                          maxSec = 30;
                      } else if (mTotalScore < 18){
                          minSec = 30;
                          maxSec = 40;
                      }else
                      {
                          minSec = 40;
                          maxSec = 60;
                      }
                      int randSec = random.nextInt(maxSec - minSec + 1) + minSec;
                      mSecond = randSec;
                      mMaxSec = randSec;
                      mTimeTV.setText("" + randSec);

                      mTimerHandler.sendEmptyMessageDelayed(BishousuConfig.kWhatCountDown,0);
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
                          mStartBtn.setClickable(true);
                          if(mANum < mBNum)
                          {
                              Toast.makeText(BishousuActivity.this,"😂😂😂你输了😂😂😂",Toast.LENGTH_SHORT).show();
                          }else if(mANum == mBNum)
                          {
                              Toast.makeText(BishousuActivity.this,"😊😊😊平局😊😊😊",Toast.LENGTH_SHORT).show();
                          }else {
                              float reward;
                              Random random = new Random();
                              int minReward = 0;
                              int maxReward = 10;
                              if (mTotalScore < 5) {
                                 minReward = 5;
                                 maxReward = 10;
                              } else if (mTotalScore < 10) {
                                  minReward = 5;
                                  maxReward = 8;
                              } else if (mTotalScore < 15) {
                                  minReward = 2;
                                  maxReward = 5;
                              } else if (mTotalScore < 18){
                                  minReward = 2;
                                  maxReward = 4;
                              }
                              reward = (float) ((random.nextInt(maxReward - minReward + 1) + minReward)/ 10.0);
                              BigDecimal b  =   new  BigDecimal(reward);
                              reward   =  b.setScale(2,  BigDecimal.ROUND_HALF_UP).floatValue();
                              mTimeTV.setText("你已获得+" + reward + "积分");

                              FileUtil.save(BishousuConfig.kMoneyKey,(reward + mTotalScore)+"",BishousuActivity.this);

                              Toast.makeText(BishousuActivity.this,"🎉🎉🎉你赢了🎉🎉🎉",Toast.LENGTH_SHORT).show();

                              Intent intent = new Intent();
                              intent.setAction("MoneyChanged");
                              intent.putExtra(BishousuConfig.kMoneyKey,reward + mTotalScore);
                              sendBroadcast(intent);
                          }
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


                          int minMill = 0;
                          int maxMill = 10;
                          if (mTotalScore < 5) {
                              minMill = 200;
                              maxMill = 250;
                          } else if (mTotalScore < 10) {
                              minMill = 180;
                              maxMill = 200;
                          } else if (mTotalScore < 15) {
                              minMill = 150;
                              maxMill = 180;
                          } else if (mTotalScore < 18){
                              minMill = 100;
                              maxMill = 250;
                          }
                          int randSec = random.nextInt(maxMill - minMill + 1) + minMill;
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
    public void touchStartBtn(final View view)
    {
        view.setClickable(false);
        String num = FileUtil.read(BishousuConfig.kBiShouSuNumKey,this);
        if(num.length() > 0) {
            int n = Integer.valueOf(num);
            Log.i("hh", "n=" + n);
            if (n > 4) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setTitle("是否愿意花费0.2个积分获得一次比赛机会？");
                builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String toString = FileUtil.read("kMoneyKey", BishousuActivity.this);
                        if (toString.length() > 0) {
                            mTotalScore = Float.valueOf(toString);
                        }
                        if (mTotalScore > 0.2) {
                            mTotalScore = mTotalScore - 0.2f;
                            FileUtil.save(BishousuConfig.kMoneyKey, mTotalScore + "", BishousuActivity.this);
                            Intent intent = new Intent();
                            intent.setAction("MoneyChanged");
                            intent.putExtra(BishousuConfig.kMoneyKey, mTotalScore);
                            sendBroadcast(intent);
                            realStart();
                        } else {
                            view.setClickable(true);
                            Toast.makeText(BishousuActivity.this, "积分不足，请明天再来挑战!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        view.setClickable(true);
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            } else
            {
                realStart();
            }

            n++;
            FileUtil.save(BishousuConfig.kBiShouSuNumKey,""+n,this);
        }else
        {
            FileUtil.save(BishousuConfig.kBiShouSuNumKey,"1",this);
            realStart();
        }

    }
    public void realStart()
    {
        setmANum(0);
        setmBNum(0);
        String scoreStr = FileUtil.read(BishousuConfig.kMoneyKey,BishousuActivity.this);
        if(scoreStr.length() > 0)
        {
            mTotalScore = Float.valueOf(scoreStr);
        }else
        {
            mTotalScore = 0;
        }

        mTimeTV.setText("正在寻找对手...");
        Random rand = new Random();
        int sec = rand.nextInt(3)+1;
        mTimerHandler.sendEmptyMessageDelayed(BishousuConfig.kWhatFindOpponent,sec * 1000);
    }
    public void setmANum(int num)
    {
        this.mANum = num;
        mNum1TV.setText(mANum + "");
    }
    public void setmBNum(int num)
    {
        this.mBNum = num;
        mNum2TV.setText(num + "");
    }
}
