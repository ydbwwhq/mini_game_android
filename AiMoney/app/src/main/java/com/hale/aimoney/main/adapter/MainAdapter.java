package com.hale.aimoney.main.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hale.aimoney.R;

/**
 * Created by shinezone on 2018/6/11.
 */

public class MainAdapter extends BaseAdapter {

    Context mContext;
    public  MainAdapter(Context context)
    {
        super();
        this.mContext = context;
    }
    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null)
        {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.main_adapter_layout,null);
        }
        TextView title1TV = (TextView) convertView.findViewById(R.id.title1);
        TextView categoryTV = (TextView) convertView.findViewById(R.id.categoryTV);
        TextView scoreTV = (TextView) convertView.findViewById(R.id.scoreTV);
        switch (position)
        {
            case 0:
            {
                title1TV.setText("比手速");
                categoryTV.setText("娱乐");
                scoreTV.setText("0.2-1积分");
            }
            break;
            case  1:
            {
                title1TV.setText("拼人品");
                categoryTV.setText("休闲");
                scoreTV.setText("0.1-0.5积分");
            }
            break;
        }
        return convertView;
    }
}
