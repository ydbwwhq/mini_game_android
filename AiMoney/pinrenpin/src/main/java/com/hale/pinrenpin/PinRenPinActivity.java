package com.hale.pinrenpin;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.hale.common.FileUtil;
import com.hale.config.PrpConfig;

import java.math.BigDecimal;
import java.util.Random;

public class PinRenPinActivity extends Activity {

    private boolean mIsPlaying = false;
    private int     mGoalNum = 0;
    private int     mRealNum = -1;
    private int     mMaxNum = 10;
    private float   mTotalScore = 0;

    private TextView mGoalTV;
    private TextView mChangeTV;
    private Button   mStartBtn;
    private TextView mResultTV;

    Handler mHandler = new Handler(){
        @Override
        public  void handleMessage(Message message)
        {

            if(mIsPlaying)
            {
                int minReward = -10;
                int maxReward = -10;
                if (mTotalScore < 5) {
                    minReward = 500;
                    maxReward = 800;
                } else if (mTotalScore < 10) {
                    minReward = 400;
                    maxReward = 600;
                } else if (mTotalScore < 15) {
                    minReward = 200;
                    maxReward = 400;
                } else if (mTotalScore < 18){
                    minReward = 100;
                    maxReward = 200;
                }
                Random random = new Random();
                int num = random.nextInt(mMaxNum+1);
                setmRealNum(num);

                int sec = random.nextInt(maxReward - minReward + 1) + minReward;
                sendEmptyMessageDelayed(1,sec);
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_ren_pin);
        mGoalTV = (TextView) findViewById(R.id.goalTV);
        mChangeTV = (TextView) findViewById(R.id.changeTV);
        mStartBtn = (Button) findViewById(R.id.startBtn);
        mResultTV = (TextView) findViewById(R.id.desTV);

    }
    public void touchBack(View view)
    {
        this.finish();
    }
    public void touchChange(View view)
    {
        if(mIsPlaying)
        {
            mStartBtn.setClickable(true);
            setmIsPlaying(false);
            if(mRealNum == mGoalNum)
            {
                float reward;
                Random random = new Random();
                int minReward = 0;
                int maxReward = 10;
                if (mTotalScore < 5) {
                    minReward = 2;
                    maxReward = 5;
                } else if (mTotalScore < 10) {
                    minReward = 1;
                    maxReward = 3;
                } else if (mTotalScore < 15) {
                    minReward = 1;
                    maxReward = 3;
                } else if (mTotalScore < 18){
                    minReward = 1;
                    maxReward = 2;
                }
                reward = (float) ((random.nextInt(maxReward - minReward + 1) + minReward)/ 10.0);
                BigDecimal b  =   new  BigDecimal(reward);
                reward   =  b.setScale(2,  BigDecimal.ROUND_HALF_UP).floatValue();
                mResultTV.setText("你已获得+" + reward + "积分");
                FileUtil.save("kMoneyKey",(reward + mTotalScore) + "",PinRenPinActivity.this);
                Toast.makeText(PinRenPinActivity.this,"🎉🎉🎉你赢了🎉🎉🎉",Toast.LENGTH_SHORT).show();

                Intent intent = new Intent();
                intent.setAction("MoneyChanged");
                intent.putExtra("kMoneyKey",reward + mTotalScore);
                sendBroadcast(intent);
            }else
            {
                Toast.makeText(PinRenPinActivity.this,"😂😂😂你输了😂😂😂",Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void setmIsPlaying(Boolean isPlaying)
    {
        mIsPlaying = isPlaying;
        if(isPlaying)
        {
            mStartBtn.setText("进行中...");
        }else
        {
            mStartBtn.setText("开始");
        }
    }
    public void touchStart(final View view)
    {
        view.setClickable(false);
        String num = FileUtil.read(PrpConfig.kPinRenPinNumKey,this);
        if(num.length() > 0)
        {
            int n = Integer.valueOf(num);
            if(n > 4)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setTitle("是否愿意花费0.1个积分获得一次比赛机会？");
                builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String toString = FileUtil.read("kMoneyKey",PinRenPinActivity.this);
                        if(toString.length() > 0)
                        {
                            mTotalScore = Float.valueOf(toString);
                        }
                        if(mTotalScore > 0.1)
                        {
                            Log.i("hh","totalScore=" + mTotalScore);
                            mTotalScore = mTotalScore - 0.1f;
                            Log.i("hh","totalScore=" + mTotalScore);
                            FileUtil.save("kMoneyKey", mTotalScore+"",PinRenPinActivity.this);
                            Intent intent = new Intent();
                            intent.setAction("MoneyChanged");
                            intent.putExtra("kMoneyKey",mTotalScore);
                            sendBroadcast(intent);
                            realStart();

                        }else
                        {
                            view.setClickable(true);
                            Toast.makeText(PinRenPinActivity.this,"积分不足，请明天再来挑战!!",Toast.LENGTH_SHORT).show();
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
            }else
            {
                realStart();
            }

            n++;
            FileUtil.save(PrpConfig.kPinRenPinNumKey,""+n,this);
        }else
        {
            FileUtil.save(PrpConfig.kPinRenPinNumKey,"1",this);
            realStart();
        }
    }
    public  void realStart()
    {
        if(!mIsPlaying)
        {
            setmIsPlaying(true);
            setmGoalNum(-1);
            setmRealNum(-2);
            Random random = new Random();
            int maxNum = random.nextInt(10 - 5 +1) + 5;
            setmGoalNum(random.nextInt(maxNum + 1));
            mHandler.sendEmptyMessage(1);

            String scoreStr = FileUtil.read("kMoneyKey",PinRenPinActivity.this);
            if(scoreStr.length() > 0)
            {
                mTotalScore = Float.valueOf(scoreStr);
            }else
            {
                mTotalScore = 0;
            }
            mStartBtn.setText("进行中...");
        }

    }
    public  void setmGoalNum(int num)
    {
        mGoalNum = num;
        mGoalTV.setText("" + num);
    }
    public  void setmRealNum(int num)
    {
        mRealNum = num;
        mChangeTV.setText("" + num);
    }
}
