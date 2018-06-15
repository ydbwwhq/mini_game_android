package com.hale.bishousu.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;


/**
 * Created by shinezone on 2018/6/12.
 */

public class TimerReceiver extends BroadcastReceiver {
    private Handler mHandler;

    public TimerReceiver(Handler handler) {
        super();
        this.mHandler = handler;
    }

    public TimerReceiver()
    {
        super();
    }
    @Override
    public void onReceive(Context context, Intent intent) {
//        mHandler.sendEmptyMessage(1);
        Log.i("hh","123");
    }
}
