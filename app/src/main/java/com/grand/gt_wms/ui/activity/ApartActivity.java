package com.grand.gt_wms.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.grand.gt_wms.R;
import com.grand.gt_wms.bean.CKbox;
import com.grand.gt_wms.service.wmsService;
import com.grand.gt_wms.tool.ActionList;
import com.grand.gt_wms.tool.HttpPath;
import com.grand.gt_wms.tool.HttpReq;
import com.grand.gt_wms.tool.PopUtil;
import com.grand.gt_wms.ui.adapter.AllotAdapter;
import com.grand.gt_wms.ui.adapter.ApartAdapter;
import com.grand.gt_wms.ui.groupedadapter.adapter.GroupedRecyclerViewAdapter;
import com.grand.gt_wms.ui.groupedadapter.holder.BaseViewHolder;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ApartActivity extends BaseActivity {
    private String tse01;    //调拨单号
    private TextView tv_tse03,tv_tse12,tv_tse05;
    private String ck_num;
    private RecyclerView mRecyclerView;
    private ApartAdapter apartAdapter;
    private Button ok_button;
    private JSONObject jsonObject;
    private List<BasicNameValuePair> list = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apart);
        init();
    }

    private void init() {
        setTitle(itemArray[10]);     //对应主界面的数据11
        back();
        setSearchEdit("拆解单号");
        ok_button = findViewById(R.id.ok_button);
        tv_tse03=findViewById(R.id.tv_tse03);
        tv_tse12=findViewById(R.id.tv_tse12);
        tv_tse05=findViewById(R.id.tv_tse05);
        ok_button.setBackgroundColor(Color.GRAY);
        ok_button.setEnabled(false);
        onSearchButtonListen();
        mRecyclerView = findViewById(R.id.gridview_apart);   //关联RV
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        apartAdapter = new ApartAdapter(this);
        apartAdapter.setOnHeaderClickListener(new GroupedRecyclerViewAdapter.OnHeaderClickListener() {
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
        mRecyclerView.setAdapter(apartAdapter);
        ok_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmApart();   //发送拆解过账请求
                closeKeyboard();
            }
        });
    }
    protected void showItems1(String tse01) {
        super.showItems(tse01);
        tse01 = tse01;
        et_search.setText(tse01);
        if (wmsService.CKBoxList != null && wmsService.CKBoxList.size()>0) {
            closeKeyboard();
//            tv_tlf902.setText("仓库："+wmsService.imptAndEmptItemsList.get(0).getTlf902());
//            tlf902.setText("仓库："+wmsService.imptAndEmptItemsList.get(0).getTlf902());
//            tlf902.setText(wmsService.imptAndEmptItemsList.get(0).getTlf902());
            tv_tse03.setText("拆解料号："+wmsService.CKBoxList.get(0).getTse03());
            tv_tse12.setText("拆解仓库："+wmsService.CKBoxList.get(0).getTse12());
            tv_tse05.setText("拆解数量："+wmsService.CKBoxList.get(0).getTse05());
            apartAdapter.notifyDataChanged();
        }else{
            closeKeyboard();
            apartAdapter.notifyDataChanged();
            PopUtil.showPopUtil("没找到此物料数据");
        }
    }
//重写数据显示方法
    @Override
    protected void showList3(boolean ifAuto, String title) {
        super.showList2(ifAuto, title);
        ok_button.setBackgroundColor(Color.GRAY);
        ok_button.setEnabled(false);
        this.tse01 = title;
        int i = 0;
        closeKeyboard();
        et_search.setText(title);
        apartAdapter.notifyDataChanged();    //数据更新
        if (ifAuto) {
            ok_button.setBackgroundResource(R.color.app_orange);
            ok_button.setEnabled(true);
        }
        if (wmsService.CKBoxList != null) {
            for (CKbox cKbox : wmsService.CKBoxList) {
                if (cKbox.isIfScanedBox()) {
                    i++;
                }
            }
            if (i == wmsService.CKBoxList.size()) {
                ok_button.setBackgroundResource(R.color.app_orange);
                ok_button.setEnabled(true);
            }
        }
    }
//重写过账请求方法
    @Override
    protected void confirmApart() {
        super.confirmApart();
        list = new ArrayList<BasicNameValuePair>();
        list.add(new BasicNameValuePair("number", tse01));
        list.add(new BasicNameValuePair("user", wmsService.USER.getEmployee()));
        list.add(new BasicNameValuePair("plant",tse01.substring(4,7)));
        HttpReq httpReq = new HttpReq(list, HttpPath.getConfirmApartPath(),
                mhandler, ActionList.GET_CONFIRMAPART_HANDLER_TYPE, ApartActivity.context);
        httpReq.start();
        ok_button.setBackgroundColor(Color.GRAY);
        ok_button.setEnabled(false);
    }
}