package com.grand.gt_wms.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.alibaba.fastjson.JSON;

import com.grand.gt_wms.R;
import com.grand.gt_wms.bean.PKbox;
import com.grand.gt_wms.service.wmsService;
import com.grand.gt_wms.tool.ActionList;
import com.grand.gt_wms.tool.HttpPath;
import com.grand.gt_wms.tool.HttpReq;
import com.grand.gt_wms.ui.adapter.PackingAdapter;
import com.grand.gt_wms.ui.groupedadapter.adapter.GroupedRecyclerViewAdapter;
import com.grand.gt_wms.ui.groupedadapter.holder.BaseViewHolder;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/11/7.
 * 发料页面
 */

public class PackingActivity extends BaseActivity {
    private String pk_num;
    private RecyclerView mRecyclerView;
    private PackingAdapter packingAdapter;
    private Button ok_button;
    private JSONObject jsonObject;
    private List<BasicNameValuePair> list = null;
    private String sfp01;//发料单号
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_packing);
        init();
    }

    private void init(){
        setTitle(itemArray[3]);
        back();
        setSearchEdit("发料单号");
        ok_button = findViewById(R.id.ok_button);
        ok_button.setBackgroundColor(Color.GRAY);
        ok_button.setEnabled(false);
        onSearchButtonListen();
        mRecyclerView = findViewById(R.id.gridview_packing);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        packingAdapter = new PackingAdapter(this);
        packingAdapter.setOnHeaderClickListener(new GroupedRecyclerViewAdapter.OnHeaderClickListener() {
            @Override
            public void onHeaderClick(GroupedRecyclerViewAdapter adapter, BaseViewHolder holder,
                                      int groupPosition) {
/*                ReceivingAdapter expandableAdapter = (ReceivingAdapter) adapter;
                if (expandableAdapter.isExpand(groupPosition)) {
                    expandableAdapter.collapseGroup(groupPosition);
                } else {
                    expandableAdapter.expandGroup(groupPosition);
                }*/
            }
        });
        mRecyclerView.setAdapter(packingAdapter);
        ok_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmPacking();
                closeKeyboard();
            }
        });
    }

    @Override
    protected void showList(boolean ifAuto, String title) {
        super.showList(ifAuto, title);
        ok_button.setBackgroundColor(Color.GRAY);
        ok_button.setEnabled(false);
        this.sfp01 = title;
        int i=0;
        closeKeyboard();
        et_search.setText(title);
        packingAdapter.notifyDataChanged();
        if(ifAuto){
            ok_button.setBackgroundResource(R.color.app_orange);
            ok_button.setEnabled(true);
        }
        if(wmsService.PKBoxList != null){
            for(PKbox pKbox : wmsService.PKBoxList){
                if(pKbox.isIfScanedBox()){
                    i++;
                }
            }
            if(i == wmsService.PKBoxList.size()){
                ok_button.setBackgroundResource(R.color.app_orange);
                ok_button.setEnabled(true);
            }
        }

    }

    @Override
    protected void confirmPacking() {
        super.confirmPacking();
        list = new ArrayList<BasicNameValuePair>();
        list.add(new BasicNameValuePair("number", sfp01));
        list.add(new BasicNameValuePair("user", wmsService.USER.getEmployee()));
        list.add(new BasicNameValuePair("plant",sfp01.substring(4,7)));
        HttpReq httpReq = new HttpReq(list, HttpPath.getConfirmPackingPath(),
                mhandler, ActionList.GET_CONFIRMPACKING_HANDLER_TYPE, PackingActivity.context);
        httpReq.start();
        ok_button.setBackgroundColor(Color.GRAY);
        ok_button.setEnabled(false);
    }


}
