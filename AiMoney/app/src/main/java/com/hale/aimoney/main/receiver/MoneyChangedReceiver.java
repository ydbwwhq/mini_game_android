package com.hale.aimoney.main.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.hale.aimoney.common.CommonConfig;

public class MoneyChangedReceiver extends BroadcastReceiver {
    private Handler mHandler;
    public  MoneyChangedReceiver(Handler handler)
    {
        super();
        this.mHandler = handler;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        float money = intent.getFloatExtra(CommonConfig.kMoneyKey,0);
        Message message = new Message();
        message.obj = money;
        mHandler.sendMessage(message);

    }
}
