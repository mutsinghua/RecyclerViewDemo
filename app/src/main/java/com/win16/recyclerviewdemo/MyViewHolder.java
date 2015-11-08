package com.win16.recyclerviewdemo;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Rex on 11/5/2015.
 * blog.csdn.net/zoudifei
 * email:dfzou@live.com
 * powered by Win16.com
 */
public class MyViewHolder extends RecyclerView.ViewHolder{

    public TextView mTextView;

    public MyViewHolder(View itemView) {
        super(itemView);
        mTextView = (TextView) itemView.findViewById(android.R.id.text1);
    }
}
