package com.turkisiwhatsapp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Windows10 on 7.10.2017.
 */

public class listAdapter extends BaseAdapter {
    private Context context;
    private List<adapter> list;


    public listAdapter(Context context, List<adapter> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = View.inflate(context,R.layout.custom_list_friends,null);
        TextView txtFriendsMail = (TextView)view.findViewById(R.id.customFriendsName);
        ImageView txtFriendsImage = (ImageView)view.findViewById(R.id.customFriendsImage);
        txtFriendsMail.setText(list.get(position).getS_FriendsName());

        view.setTag(list.get(position).getS_FriendsName());

        Picasso.with(context)
                .load(list.get(position).getS_FriendsImage())
                .resize(100,80)
                .into(txtFriendsImage);

        return view;
    }
}
