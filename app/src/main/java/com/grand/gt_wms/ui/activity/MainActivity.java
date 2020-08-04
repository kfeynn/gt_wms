package com.grand.gt_wms.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.support.v7.widget.RecyclerView;
import android.view.WindowManager;
import android.widget.Button;

import com.grand.gt_wms.R;
import com.grand.gt_wms.ui.adapter.MainAdapter;

/**
 * Created by Administrator on 2018/7/30.此为主显示界面
 */

public class MainActivity extends BaseActivity{
    private Context context;
    private RecyclerView mRecyclerView;
    private Button btn_titlebar_right;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        context = this.getApplicationContext();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); //隐藏状态栏
        Intent intent = new Intent();
        intent.setPackage(getPackageName());
        intent.setAction("com.grand.gt_wms.action.main");
        this.startService(intent);
        initData();
    }

    private void initData() {
        mRecyclerView = findViewById(R.id.gridview);
        btn_titlebar_right = findViewById(R.id.btn_titlebar_right);
        btn_titlebar_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(context, LoginActivity.class);
                startActivity(myIntent);
                finish();
            }
        });
        GridLayoutManager layoutManager = new GridLayoutManager(this,2);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(new MainAdapter(this));
/*        mRecyclerView.setRecyclerListener(new RecyclerView.RecyclerListener() {
            @Override
            public void onViewRecycled(RecyclerView.ViewHolder holder) {

            }
        });*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent myIntent = new Intent(this, LoginActivity.class);
            startActivity(myIntent);
            this.finish();
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_HOME) {
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }


}
