package com.grand.gt_wms.ui.activity;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.grand.gt_wms.R;
import com.grand.gt_wms.bean.AKbox;
import com.grand.gt_wms.bean.BKbox;
import com.grand.gt_wms.service.wmsService;
import com.grand.gt_wms.tool.ActionList;
import com.grand.gt_wms.tool.HttpPath;
import com.grand.gt_wms.tool.HttpReq;
import com.grand.gt_wms.ui.adapter.AllotAdapter;
import com.grand.gt_wms.ui.adapter.DumpimgAdapter;
import com.grand.gt_wms.ui.groupedadapter.adapter.GroupedRecyclerViewAdapter;
import com.grand.gt_wms.ui.groupedadapter.holder.BaseViewHolder;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DumpingActivity extends BaseActivity  {
     private RecyclerView mRecyclerView;
     private Button ok_button;
     private DumpimgAdapter dumpimgAdapter;
     private String sfk01;   //报废单号
     private String bk_num;
    private JSONObject jsonObject;
    private List<BasicNameValuePair> list = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dumping);
        init();
    }
    //初始化
    private void init(){
        setTitle(itemArray[9]);     //对应主界面的数据10
        back();
        setSearchEdit("报废单号");
        ok_button = findViewById(R.id.ok_button);
        ok_button.setBackgroundColor(Color.GRAY);
        ok_button.setEnabled(false);
        onSearchButtonListen();
        mRecyclerView = findViewById(R.id.gridview_dumpimg);   //关联RV
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        dumpimgAdapter = new DumpimgAdapter(this);
        dumpimgAdapter.setOnHeaderClickListener(new GroupedRecyclerViewAdapter.OnHeaderClickListener() {
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
        mRecyclerView.setAdapter(dumpimgAdapter);
        ok_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDumpimg();   //发送报废过账请求
                closeKeyboard();
            }
        });
    }

    @Override //重写显示方法
    protected void showList2(boolean ifAuto, String title) {
        super.showList2(ifAuto, title);
        ok_button.setBackgroundColor(Color.GRAY);
        ok_button.setEnabled(false);
        this.sfk01=title;
        int i=0;
        closeKeyboard();
        et_search.setText(title);
        dumpimgAdapter.notifyDataChanged();    //数据更新
        if(ifAuto){
            ok_button.setBackgroundResource(R.color.app_orange);
            ok_button.setEnabled(true);
        }
        if(wmsService.BKBoxList != null){  //循环判断是否扫描完
            for(BKbox bKbox : wmsService.BKBoxList){
                if(bKbox.isIfScanedBox()){
                    i++;
                }
            }
            if(i == wmsService.BKBoxList.size()){
                ok_button.setBackgroundResource(R.color.app_orange);
                ok_button.setEnabled(true);
            }
        }
    }

    @Override //重写过账方法
    protected void confirmDumpimg() {
        super.confirmDumpimg();
        list = new ArrayList<BasicNameValuePair>();
        list.add(new BasicNameValuePair("number", sfk01));
        list.add(new BasicNameValuePair("user", wmsService.USER.getEmployee()));
        list.add(new BasicNameValuePair("plant",sfk01.substring(4,7)));
        HttpReq httpReq = new HttpReq(list, HttpPath.getConfirmDumpingPath(),
                mhandler, ActionList.GET_CONFIRMDUMPING_HANDLER_TYPE, DumpingActivity.context);
        httpReq.start();
        ok_button.setBackgroundColor(Color.GRAY);
        ok_button.setEnabled(false);
    }
}
