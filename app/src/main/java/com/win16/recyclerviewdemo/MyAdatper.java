package com.win16.recyclerviewdemo;

import android.content.pm.PackageInfo;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Rex on 11/5/2015.
 * blog.csdn.net/zoudifei
 * email:dfzou@live.com
 * powered by Win16.com
 */
public class MyAdatper extends RecyclerView.Adapter<MyViewHolder> {

    private List<PackageInfo> packageInfos;

    public MyAdatper(List<PackageInfo> packageInfos) {
        this.packageInfos = packageInfos;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new MyViewHolder(layoutInflater.inflate(android.R.layout.simple_list_item_1,parent,false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        TextView textView = holder.mTextView;
        textView.setText(packageInfos.get(position).packageName);

    }

    @Override
    public int getItemCount() {
        return packageInfos.size();
    }
}
