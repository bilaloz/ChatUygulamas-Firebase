package com.turkisiwhatsapp;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Windows10 on 11.10.2017.
 */

public class customListAdapter extends BaseAdapter {
    private Context context;
    private List<customChatAdapter> customListAdapters2;

    public customListAdapter(Context context, List<customChatAdapter> customListAdapters2) {
        this.context = context;
        this.customListAdapters2 = customListAdapters2;
    }

    @Override
    public int getCount() {
        return customListAdapters2.size();
    }

    @Override
    public Object getItem(int position) {
        return customListAdapters2.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = View.inflate(context,R.layout.custom_list_mesaj,null);
        TextView txtMesajs = (TextView)v.findViewById(R.id.txtMesaj);
        txtMesajs.setText(customListAdapters2.get(position).getS_mesaj());

        if (customListAdapters2.get(position).getS_kullanici() =="sen"){
            txtMesajs.setTextColor(Color.BLACK);
            txtMesajs.setTextAlignment(v.TEXT_ALIGNMENT_TEXT_END);
        }
        return v;
    }
}
