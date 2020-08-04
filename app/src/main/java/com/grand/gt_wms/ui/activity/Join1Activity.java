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
import com.grand.gt_wms.bean.RnBox;
import com.grand.gt_wms.service.wmsService;
import com.grand.gt_wms.tool.ActionList;
import com.grand.gt_wms.tool.HttpPath;
import com.grand.gt_wms.tool.HttpReq;
import com.grand.gt_wms.ui.adapter.Join1Adapter;
import com.grand.gt_wms.ui.groupedadapter.adapter.GroupedRecyclerViewAdapter;
import com.grand.gt_wms.ui.groupedadapter.holder.BaseViewHolder;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Join1Activity extends BaseActivity {
    private String rva07;    //送货单
    private RecyclerView mRecyclerView;
    private Join1Adapter join1Adapter;
    private Button ok_button;
    private JSONObject jsonObject;
    private List<BasicNameValuePair> list = null;
    public  String rvb01;  //收货单

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join1);
        init();

    }

    private void init() {
        setTitle(itemArray[12]);     //对应主界面的数据12
        back();
        setSearchEdit("送货单号");
        ok_button = findViewById(R.id.ok_button);
        ok_button.setBackgroundColor(Color.GRAY);
        ok_button.setEnabled(false);
        onSearchButtonListen();
        mRecyclerView = findViewById(R.id.gridview_join1);   //关联RV
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        join1Adapter = new Join1Adapter(this);
        join1Adapter.setOnHeaderClickListener(new GroupedRecyclerViewAdapter.OnHeaderClickListener() {
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
        mRecyclerView.setAdapter(join1Adapter);
        ok_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmJoin1();   //发送交接记录请求
                closeKeyboard();
            }
        });
    }

    //重写数据显示方法
    @Override
    protected void showList5(boolean ifAuto, String title) {
        super.showList5(ifAuto, title);
        ok_button.setBackgroundColor(Color.GRAY);
        ok_button.setEnabled(false);
        this.rva07 = title;
        int i = 0;
        closeKeyboard();
        et_search.setText(title);
        join1Adapter.notifyDataChanged();    //数据更新
        if (ifAuto) {
            ok_button.setBackgroundResource(R.color.app_orange);
            ok_button.setEnabled(true);
        }
        if (wmsService.RnBoxList != null) {
            for (RnBox rnBox : wmsService.RnBoxList) {
                 rvb01=rnBox.getRvb01();   //收货单
                if (rnBox.isIfScanedBox()) {
                    i++;
                }
            }
            if (i == wmsService.RnBoxList.size()) {
                ok_button.setBackgroundResource(R.color.app_orange);
                ok_button.setEnabled(true);
            }
        }
    }
    //重写过账请求方法
    @Override
    protected void confirmJoin1() {
        super.confirmJoin1();
        list = new ArrayList<BasicNameValuePair>();
        list.add(new BasicNameValuePair("number", rva07.substring(2)));
        list.add(new BasicNameValuePair("user", wmsService.USER.getEmployee()));
        list.add(new BasicNameValuePair("plant",rvb01.substring(4,7)));
        HttpReq httpReq = new HttpReq(list, HttpPath.getConfirmReceivingJoin1Path(),
                mhandler, ActionList.GET_CONFIRMJOIN1_HANDLER_TYPE, Join1Activity.context);
        httpReq.start();
        ok_button.setBackgroundColor(Color.GRAY);
        ok_button.setEnabled(false);
    }
}