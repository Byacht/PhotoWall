package com.byacht.retrofitdemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.byacht.retrofitdemo.R;
import com.byacht.retrofitdemo.entity.ZhihuDaily;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dn on 2017/4/8.
 */

public class ZhihuAdapter extends RecyclerView.Adapter<ZhihuAdapter.MyViewHolder>{
    private Context mContext;
    private ArrayList<ZhihuDaily> zhihuDailies;

    public ZhihuAdapter(Context context, ArrayList<ZhihuDaily> zhihuDailies){
        this.mContext = context;
        this.zhihuDailies = zhihuDailies;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.zhihu_layout_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Log.d("out", "url:" + zhihuDailies.get(position).getId());
        Glide.with(mContext)
                .load(zhihuDailies.get(position).getImages()[0])
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(holder.imageView);
        holder.textView.setText(zhihuDailies.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return zhihuDailies.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img_zhihu)
        ImageView imageView;
        @BindView(R.id.tv_zhihu)
        TextView textView;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
