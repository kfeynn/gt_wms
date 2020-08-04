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
import com.grand.gt_wms.bean.WhrBox;
import com.grand.gt_wms.service.wmsService;
import com.grand.gt_wms.tool.ActionList;
import com.grand.gt_wms.tool.HttpPath;
import com.grand.gt_wms.tool.HttpReq;
import com.grand.gt_wms.ui.adapter.WHReturnAdapter;
import com.grand.gt_wms.ui.groupedadapter.adapter.GroupedRecyclerViewAdapter;
import com.grand.gt_wms.ui.groupedadapter.holder.BaseViewHolder;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2019/1/8.
 * 仓退页面
 */

public class WHReturnActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private WHReturnAdapter whReturnAdapter;
    private Button ok_button;
    private List<BasicNameValuePair> list = null;
    private String rvv01;//退料单号
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whreturn);
        init();
    }

    private void init(){
        setTitle(itemArray[5]);
        back();
        setSearchEdit("退料单号");
        ok_button = findViewById(R.id.ok_button);
        ok_button.setBackgroundColor(Color.GRAY);
        ok_button.setEnabled(false);
        onSearchButtonListen();
        mRecyclerView = findViewById(R.id.gridview_packing);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        whReturnAdapter = new WHReturnAdapter(this);
        whReturnAdapter.setOnHeaderClickListener(new GroupedRecyclerViewAdapter.OnHeaderClickListener() {
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
        mRecyclerView.setAdapter(whReturnAdapter);
        ok_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmWhReturn();
                closeKeyboard();
            }
        });
    }

    @Override
    protected void showList(boolean ifAuto, String title) {
        super.showList(ifAuto, title);
        this.rvv01 = title;
        int i=0;
        et_search.setText(title);
        closeKeyboard();
        whReturnAdapter.notifyDataChanged();
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
    protected void confirmWhReturn() {
        super.confirmWhReturn();
        list = new ArrayList<BasicNameValuePair>();
        list.add(new BasicNameValuePair("WHR", buildWHReturnJson()));
        list.add(new BasicNameValuePair("plant",rvv01.substring(4,7)));
        HttpReq httpReq = new HttpReq(list, HttpPath.getConfirmWhReturnPath(),
                mhandler, ActionList.GET_CONFIRMWHRETURN_HANDLER_TYPE, WHReturnActivity.context);
        httpReq.start();
        ok_button.setBackgroundColor(Color.GRAY);
        ok_button.setEnabled(false);
    }

    private String buildWHReturnJson(){
        Map returnMap = new HashMap<>();
        Map<String, String> headMap = new HashMap<>();
        headMap.put("rvu01", rvv01);
        headMap.put("user",wmsService.USER.getEmployee());
        returnMap.put("head", headMap);
        System.out.println(JSON.toJSONString(returnMap));
        return JSON.toJSONString(returnMap);
    }
}
