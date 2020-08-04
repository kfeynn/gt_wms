package com.grand.gt_wms.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.grand.gt_wms.R;
import com.grand.gt_wms.bean.WhrBox;
import com.grand.gt_wms.service.wmsService;
import com.grand.gt_wms.tool.ActionList;
import com.grand.gt_wms.tool.HttpPath;
import com.grand.gt_wms.tool.HttpReq;
import com.grand.gt_wms.ui.adapter.JoinAdapter;
import com.grand.gt_wms.ui.groupedadapter.adapter.GroupedRecyclerViewAdapter;
import com.grand.gt_wms.ui.groupedadapter.holder.BaseViewHolder;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JoinActivity extends BaseActivity {
    private String rvv01;    //仓退单号
    private RecyclerView mRecyclerView;
    private JoinAdapter joinAdapter;
    private Button ok_button;
    private JSONObject jsonObject;
    private List<BasicNameValuePair> list = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        init();

    }

    private void init() {
        setTitle(itemArray[11]);     //对应主界面的数据11
        back();
        setSearchEdit("仓退单号");
        ok_button = findViewById(R.id.ok_button);
        ok_button.setBackgroundColor(Color.GRAY);
        ok_button.setEnabled(false);
        onSearchButtonListen();
        mRecyclerView = findViewById(R.id.gridview_join);   //关联RV
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        joinAdapter = new JoinAdapter(this);
        joinAdapter.setOnHeaderClickListener(new GroupedRecyclerViewAdapter.OnHeaderClickListener() {
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
        mRecyclerView.setAdapter(joinAdapter);
        ok_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmJoin();   //发送交接记录请求
                closeKeyboard();
            }
        });
    }

    //重写数据显示方法
    @Override
    protected void showList4(boolean ifAuto, String title) {
        super.showList4(ifAuto, title);
        ok_button.setBackgroundColor(Color.GRAY);
        ok_button.setEnabled(false);
        this.rvv01 = title;
        int i = 0;
        closeKeyboard();
        et_search.setText(title);
        joinAdapter.notifyDataChanged();    //数据更新
        if (ifAuto) {
            ok_button.setBackgroundResource(R.color.app_orange);
            ok_button.setEnabled(true);
        }
        if (wmsService.whrBoxList != null) {
            for (WhrBox whrBox : wmsService.whrBoxList) {
                if (whrBox.isIfScanedBox()) {
                    i++;
                }
            }
            if (i == wmsService.whrBoxList.size()) {
                ok_button.setBackgroundResource(R.color.app_orange);
                ok_button.setEnabled(true);
            }
        }
    }
    //重写过账请求方法
    @Override
    protected void confirmJoin() {
        super.confirmJoin();
        list = new ArrayList<BasicNameValuePair>();
        list.add(new BasicNameValuePair("number", rvv01));
        list.add(new BasicNameValuePair("user", wmsService.USER.getEmployee()));
//        list.add(new BasicNameValuePair("plant",rvv01.substring(4,7)));
        HttpReq httpReq = new HttpReq(list, HttpPath.getConfirmWhReturnJoinPath(),
                mhandler, ActionList.GET_CONFIRMJOIN_HANDLER_TYPE, ApartActivity.context);
        httpReq.start();
        ok_button.setBackgroundColor(Color.GRAY);
        ok_button.setEnabled(false);
    }
}