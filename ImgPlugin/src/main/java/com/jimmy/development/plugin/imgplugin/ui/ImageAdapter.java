package com.jimmy.development.plugin.imgplugin.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jimmy.development.plugin.imgplugin.R;
import com.jimmy.development.plugin.imgplugin.request.data.ImageListBean;

import java.util.List;

/**
 * Created by jinguochong on 2017/9/4.
 */
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private Context context;
    private List<ImageListBean> imgList;


    public ImageAdapter(Context context, List<ImageListBean> imgList) {
        this.context = context;
        this.imgList = imgList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_img, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTextView.setText(imgList.get(position).desc);
        Glide.with(context).load(imgList.get(position).url).fitCenter().into(holder.imgView);
    }

    @Override
    public int getItemCount() {
        return imgList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mTextView;
        public ImageView imgView;

        public ViewHolder(View view) {
            super(view);
            mTextView = (TextView) view.findViewById(R.id.tv);
            imgView = (ImageView) view.findViewById(R.id.iv);
        }
    }

}
