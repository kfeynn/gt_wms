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
import com.grand.gt_wms.service.wmsService;
import com.grand.gt_wms.tool.ActionList;
import com.grand.gt_wms.tool.HttpPath;
import com.grand.gt_wms.tool.HttpReq;
import com.grand.gt_wms.ui.adapter.AllotAdapter;
import com.grand.gt_wms.ui.groupedadapter.adapter.GroupedRecyclerViewAdapter;
import com.grand.gt_wms.ui.groupedadapter.holder.BaseViewHolder;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AllotActivity extends BaseActivity  {
    private  String imm01;    //调拨单号
    private String ak_num;
    private RecyclerView mRecyclerView;
    private AllotAdapter allotAdapter;
    private Button ok_button;
    private JSONObject jsonObject;
    private List<BasicNameValuePair> list = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allot);
        init();
    }
    private void init(){
        setTitle(itemArray[8]);     //对应主界面的数据10
        back();
        setSearchEdit("调拨单号");
        ok_button = findViewById(R.id.ok_button);
        ok_button.setBackgroundColor(Color.GRAY);
        ok_button.setEnabled(false);
        onSearchButtonListen();
        mRecyclerView = findViewById(R.id.gridview_allot);   //关联RV
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        allotAdapter = new AllotAdapter(this);
        allotAdapter.setOnHeaderClickListener(new GroupedRecyclerViewAdapter.OnHeaderClickListener() {
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
        mRecyclerView.setAdapter(allotAdapter);
        ok_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmAllot();   //发送调拨过账请求
                closeKeyboard();
            }
        });
    }

    @Override//重写父类的显示方法
    protected void showList1(boolean ifAuto, String title) {
        super.showList1(ifAuto, title);
        ok_button.setBackgroundColor(Color.GRAY);
        ok_button.setEnabled(false);
        this.imm01=title;
        int i=0;
        closeKeyboard();
        et_search.setText(title);
        allotAdapter.notifyDataChanged();    //数据更新
        if(ifAuto){
            ok_button.setBackgroundResource(R.color.app_orange);
            ok_button.setEnabled(true);
        }
        if(wmsService.AKBoxList != null){
            for(AKbox aKbox : wmsService.AKBoxList){
                if(aKbox.isIfScanedBox()){
                    i++;
                }
            }
            if(i == wmsService.AKBoxList.size()){
                ok_button.setBackgroundResource(R.color.app_orange);
                ok_button.setEnabled(true);
            }
        }

    }

    @Override//重写过账请求方法
    protected void confirmAllot() {
        super.confirmAllot();
        list = new ArrayList<BasicNameValuePair>();
        list.add(new BasicNameValuePair("number", imm01));
        list.add(new BasicNameValuePair("user", wmsService.USER.getEmployee()));
        list.add(new BasicNameValuePair("plant",imm01.substring(4,7)));
        HttpReq httpReq = new HttpReq(list, HttpPath.getConfirmAllotPath(),
                mhandler, ActionList.GET_CONFIRMALLOT_HANDLER_TYPE, AllotActivity.context);
        httpReq.start();
        ok_button.setBackgroundColor(Color.GRAY);
        ok_button.setEnabled(false);
    }
}
