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
import com.grand.gt_wms.ui.adapter.WOReturnAdapter;
import com.grand.gt_wms.ui.groupedadapter.adapter.GroupedRecyclerViewAdapter;
import com.grand.gt_wms.ui.groupedadapter.holder.BaseViewHolder;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/12/19.
 * 工单退料
 */

public class WOReturnActivity extends BaseActivity {
    private Button ok_button;
    private RecyclerView mRecyclerView;
    private WOReturnAdapter woReturnAdapter;
    private List<BasicNameValuePair> list = null;
    private String sfp01;//退料单号
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_woreturn);
        init();
    }

    private void init(){
        setTitle(itemArray[4]);
        back();
        setSearchEdit("退料单号");
        ok_button = findViewById(R.id.ok_button);
        ok_button.setBackgroundColor(Color.GRAY);
        ok_button.setEnabled(false);
        onSearchButtonListen();
        mRecyclerView = findViewById(R.id.gridview_packing);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        woReturnAdapter = new WOReturnAdapter(this);
        woReturnAdapter.setOnHeaderClickListener(new GroupedRecyclerViewAdapter.OnHeaderClickListener() {
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
        mRecyclerView.setAdapter(woReturnAdapter);
        ok_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmWoReturn();
                closeKeyboard();
            }
        });
    }

    @Override
    protected void showList(boolean ifAuto, String title) {
        super.showList(ifAuto, title);
        this.sfp01 = title;
        int i=0;
        et_search.setText(title);
        closeKeyboard();
        woReturnAdapter.notifyDataChanged();
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
    protected void confirmWoReturn() {
        super.confirmWoReturn();
        list = new ArrayList<BasicNameValuePair>();
        list.add(new BasicNameValuePair("WR", buildPackingJson()));
        list.add(new BasicNameValuePair("plant",sfp01.substring(4,7)));
        HttpReq httpReq = new HttpReq(list, HttpPath.getConfirmWoReturnPath(),
                mhandler, ActionList.GET_CONFIRMWORETURN_HANDLER_TYPE, WOReturnActivity.context);
        httpReq.start();
        ok_button.setBackgroundColor(Color.GRAY);
        ok_button.setEnabled(false);
    }

    private String buildPackingJson(){
        Map returnMap = new HashMap<>();
        Map<String, String> headMap = new HashMap<>();
        headMap.put("sfp01", sfp01);
        headMap.put("user",wmsService.USER.getEmployee());
        returnMap.put("head", headMap);
        System.out.println(JSON.toJSONString(returnMap));
        return JSON.toJSONString(returnMap);
    }

}
