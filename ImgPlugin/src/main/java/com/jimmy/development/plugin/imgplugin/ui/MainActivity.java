package com.jimmy.development.plugin.imgplugin.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.jimmy.development.plugin.imgplugin.R;
import com.jimmy.development.plugin.imgplugin.request.IHttpListener;
import com.jimmy.development.plugin.imgplugin.request.ImageRequest;
import com.jimmy.development.plugin.imgplugin.request.data.ImageListBean;
import com.jimmy.development.plugin.imgplugin.request.data.ReturnDataList;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<ImageListBean> imgList;
    private ImageRequest request;
    private int page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toast.makeText(this, "this is img plugin", Toast.LENGTH_SHORT).show();
        imgList = new ArrayList<>();
        request = new ImageRequest();
        recyclerView = (RecyclerView) findViewById(R.id.rv_img);

        //创建默认的线性LayoutManager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        recyclerView.setHasFixedSize(true);
        //创建并设置Adapter
        adapter = new ImageAdapter(this, imgList);
        recyclerView.setAdapter(adapter);

        page = 1;
        request.getImageListByRetrofit(page, new IHttpListener<ReturnDataList<ImageListBean>>() {
            @Override
            public void ok(ReturnDataList<ImageListBean> data) {
                Log.e("jgc", data.toString());
                imgList.addAll(data.results);
                //recyclerView.notify();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void fail(int code, String msg) {
                Toast.makeText(MainActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
            }
        });


    }


    private void refreshList() {

    }
}
