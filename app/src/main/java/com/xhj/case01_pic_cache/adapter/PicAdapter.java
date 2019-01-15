package com.xhj.case01_pic_cache.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xhj.case01_pic_cache.activity.BigImageActivity;
import com.xhj.case01_pic_cache.activity.PicDetailActivity;
import com.xhj.R;
import com.xhj.case01_pic_cache.entity.PicObj;
import com.xhj.common.Constant;

import java.util.List;

/**
 * Author: Created by XHJ on 2019/1/11.
 * 种一棵树最好的时间是十年前，其次是现在。
 */
public class PicAdapter extends RecyclerView.Adapter<PicAdapter.ViewHolder> {

    private Context context;
    private List<PicObj> picObjList;

    public PicAdapter(Context context, List<PicObj> picObjList) {
        this.context = context;
        this.picObjList = picObjList;
    }

    public PicAdapter(List<PicObj> picObjList) {
        this.picObjList = picObjList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int i) {
        if(context == null){
            context = viewGroup.getContext();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.case01_picture_item,viewGroup,false);
        final ViewHolder holder = new ViewHolder(view);
        //点击图片名字进入可折叠toolbar页面
        holder.picName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                PicObj picObj = picObjList.get(position);
                Intent intent = new Intent(context, PicDetailActivity.class);
                intent.putExtra(Constant.CASE01_PIC_NAME, picObj.getName());
                intent.putExtra(Constant.CASE01_PIC_IMAGE_ID, picObj.getImageId());
                context.startActivity(intent);
            }
        });
        holder.picImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                PicObj picObj = picObjList.get(position);
                Intent intent = new Intent(context, BigImageActivity.class);
                intent.putExtra(Constant.CASE01_PIC_NAME, picObj.getName());
                intent.putExtra(Constant.CASE01_PIC_IMAGE_ID, picObj.getImageId());
                context.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        PicObj picObj = picObjList.get(i);
        viewHolder.picName.setText(picObj.getName());
        Glide.with(context).load(picObj.getImageId()).into(viewHolder.picImage);
    }

    @Override
    public int getItemCount() {
        return picObjList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        ImageView picImage;
        TextView picName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            picImage = itemView.findViewById(R.id.picture_image);
            picName = itemView.findViewById(R.id.picture_name);
        }
    }

}
