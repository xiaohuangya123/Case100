package com.xhj.case01_pic_cache.activity;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.xhj.R;
import com.xhj.common.Constant;

/**
 * Author: Created by XHJ on 2019/1/13.
 *  * 种一棵树最好的时间是十年前，其次是现在。
 *
 *  超大图加载实现
 */
public class BigImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.case01_activity_big_image);

        //初始化可折叠toolbar
        initToolBar();

    }

    /**
     * 初始化可折叠toolbar
     */
    private void initToolBar() {
        Intent intent = getIntent();
        String picName = intent.getStringExtra(Constant.CASE01_PIC_NAME);
        int picImageId = intent.getIntExtra(Constant.CASE01_PIC_IMAGE_ID, -1);
        Toolbar toolbar = findViewById(R.id.toolbar);
        CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        ImageView picImage = findViewById(R.id.pic_image);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        collapsingToolbar.setTitle(picName);
        Glide.with(this).load(picImageId).into(picImage);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
