package com.byacht.retrofitdemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.byacht.retrofitdemo.R;
import com.byacht.retrofitdemo.entity.MeiZhi;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dn on 2017/3/29.
 */

public class MeiZhiAdapter extends RecyclerView.Adapter<MeiZhiAdapter.MyViewHolder>{

    private Context mContext;
    private ArrayList<MeiZhi> meiZhiData;
    private ArrayList<Integer> mPhotoHeight;

    public MeiZhiAdapter(Context context, ArrayList<MeiZhi> meiZhiData, ArrayList<Integer> photoHeight) {
        mContext = context;
        this.meiZhiData = meiZhiData;
        this.mPhotoHeight = photoHeight;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d("out", "create:" + getItemCount());
        View view = LayoutInflater.from(mContext).inflate(R.layout.meizhi_layout_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) holder.photoView.getLayoutParams();
        lp.height = mPhotoHeight.get(position);
        holder.photoView.setLayoutParams(lp);
        Glide.with(mContext)
                .load(meiZhiData.get(position).getUrl())
                .placeholder(R.mipmap.ic_launcher)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(holder.photoView);
    }

    @Override
    public int getItemCount() {
        return meiZhiData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.img_meizhi)
        ImageView photoView;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
