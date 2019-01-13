package com.xhj;

import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.xhj.case01_pic_cache.adapter.PicAdapter;
import com.xhj.case01_pic_cache.entity.PicObj;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Author: Created by XHJ on 2019/1/11.
 * 种一棵树最好的时间是十年前，其次是现在。
 *
 * case01  ：图片优化与超大图片加载
 * case说明：1、实现图片三级缓存，内存缓存，本地SD卡缓存，网络缓存
 *           2、实现超大图加载功能
 */
public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    //用于显示的图片资源
    private PicObj[] pics = {new PicObj("Apple", R.drawable.apple), new PicObj("Banana", R.drawable.banana),
            new PicObj("Orange", R.drawable.orange), new PicObj("Watermelon", R.drawable.watermelon),
            new PicObj("Pear", R.drawable.pear), new PicObj("Grape", R.drawable.grape),
            new PicObj("Pineapple", R.drawable.pineapple), new PicObj("Strawberry", R.drawable.strawberry),
            new PicObj("Cherry", R.drawable.cherry), new PicObj("Mango", R.drawable.mango)};
    private List<PicObj> picsList = new ArrayList<>();
    private PicAdapter picAdapter;
    private SwipeRefreshLayout swipeRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolbar();
        initLeftNavigation();
        initFab();
        initPics();
        initRecyclerView();
        initSwipeRefresh();

    }

    /**
     * 初始化Toolbar
     */
    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    /**
     * 初始化左侧导航栏
     */
    private void initLeftNavigation() {
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mNavigationView = findViewById(R.id.nav_view);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }
        mNavigationView.setCheckedItem(R.id.call);
        mNavigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
    }

    /**
     * 初始化FloatingActionButton
     */
    private void initFab() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Snackbar.make(v,"Data delete?" , Snackbar.LENGTH_SHORT)
                        .setAction("Undo", new View.OnClickListener(){
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(MainActivity.this,
                                        "Data restored", Toast.LENGTH_SHORT).show();
                            }
                        }).show();
            }
        });
    }

    /**
     * 初始化用于展示的的图片资源
     */
    private void initPics() {
        picsList.clear();
        for (int i = 0; i < 50; i++) {
            Random random = new Random();
            int index = random.nextInt(pics.length);
            picsList.add(pics[index]);
        }
    }

    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        GridLayoutManager manager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(manager);
        picAdapter = new PicAdapter(picsList);
        recyclerView.setAdapter(picAdapter);
    }

    /**
     * 用SwipeResreshLayout实现图片的下拉刷新
     */
    private void initSwipeRefresh() {
        swipeRefresh = findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshPics();
            }
        });
    }

    /**
     * 刷新图片
     */
    private void refreshPics() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initPics();
                        picAdapter.notifyDataSetChanged();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        }).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                mDrawerLayout.openDrawer(Gravity.START);
                break;
            case R.id.backup:
                Toast.makeText(this, "You click backup", Toast.LENGTH_SHORT).show();
                break;
            case R.id.delete:
                Toast.makeText(this,"You click delete", Toast.LENGTH_SHORT).show();
                break;
            case R.id.setting:
                Toast.makeText(this, "You click settting", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }
}
