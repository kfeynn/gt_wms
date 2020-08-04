package com.grand.gt_wms.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.grand.gt_wms.R;
import com.grand.gt_wms.bean.Label;
import com.grand.gt_wms.bean.RnBox;
import com.grand.gt_wms.service.wmsService;
import com.grand.gt_wms.tool.ActionList;
import com.grand.gt_wms.tool.HttpPath;
import com.grand.gt_wms.tool.HttpReq;
import com.grand.gt_wms.ui.adapter.GroundingAdapter;
import com.grand.gt_wms.ui.groupedadapter.adapter.GroupedRecyclerViewAdapter;
import com.grand.gt_wms.ui.groupedadapter.holder.BaseViewHolder;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Administrator on 2018/8/22.
 * 上架页面
 */

public class GroundingActivity extends BaseActivity implements View.OnClickListener{
    private TextView tv_pn_num,tv_pmm01,tv_pmn04,tv_pmn041,tv_boxcount,
            tv_pnsub_rvb07,tv_pnsub_rvb17a,tv_rvb01;
    private String box_num;
    private Button btn_input_grounding;
    private RecyclerView mRecyclerView;
    private GroundingAdapter groundingAdapter;
    private List<BasicNameValuePair> list = null;
    private JSONObject jsonObject;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grounding);
        init();
    }

    private void init(){
        setTitle(itemArray[2]);
        back();
        setSearchEdit("箱号");
        onSearchButtonListen();
        tv_pmn041 = findViewById(R.id.tv_pmn041);
        tv_rvb01 = findViewById(R.id.tv_rvb01);
        btn_input_grounding = findViewById(R.id.btn_input_grounding);
        btn_input_grounding.setOnClickListener(this);
        mRecyclerView = findViewById(R.id.gridview_grounding);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        groundingAdapter = new GroundingAdapter(this,box_num);
        groundingAdapter.setOnHeaderClickListener(new GroupedRecyclerViewAdapter.OnHeaderClickListener() {
            @Override
            public void onHeaderClick(GroupedRecyclerViewAdapter adapter, BaseViewHolder holder,
                                      int groupPosition) {
                GroundingAdapter expandableAdapter = (GroundingAdapter) adapter;
                System.out.println(wmsService.RnBoxList.get(groupPosition).getLabels().size());
                if (expandableAdapter.isExpand(groupPosition)) {
                    expandableAdapter.collapseGroup(groupPosition);
                } else {
                    expandableAdapter.expandGroup(groupPosition);
                }
            }
        });
        mRecyclerView.setAdapter(groundingAdapter);
    }

    @Override
    protected void showBoxList(String boxNum) {
        super.showBoxList(boxNum);
        box_num = boxNum;
        et_search.setText(boxNum);
        if (wmsService.RnBoxList != null && wmsService.RnBoxList.size()>0) {
            closeKeyboard();
            groundingAdapter.notifyDataChanged();
            tv_rvb01.setText("收货单号："+wmsService.RnBoxList.get(0).getRvb01());
        }else{
            closeKeyboard();
            groundingAdapter.notifyDataChanged();
            tv_rvb01.setText("收货单号：");
        }
    }

    @Override
    protected void showLabel(int groupPosition) {
        super.showLabel(groupPosition);
        System.out.println(groupPosition);
        groundingAdapter.notifyDataChanged();
    }

    @Override
    protected void showBoxWaddr(boolean ifStop,boolean ifInput_Grounding) {
        super.showBoxWaddr(ifStop,ifInput_Grounding);
        groundingAdapter.notifyDataChanged();
        if(ifStop){
            double count = 0;
            for (RnBox rnBox : wmsService.RnBoxList){
                if(rnBox.getLabels().size()>0){
                    for (Label label : rnBox.getLabels()){
                        count = Double.valueOf(label.getQty()) + count;
                    }
                }else {
                    if(!rnBox.getRvv32().equals("")&&!rnBox.getRvv33().equals("")){
                        count = Double.valueOf(rnBox.getIbb07()) + count;
                    }
                }
            }
            if(ifInput_Grounding){
                commonDialog("此次上架数量"+count+"，是否确定强制上架",9);
            }else {
                commonDialog("此次上架数量"+count+"，是否确定上架",3);
            }
        }
    }

    @Override
    protected void confirmGrounding() {
        super.confirmGrounding();
        list = new ArrayList<BasicNameValuePair>();
        list.add(new BasicNameValuePair("RN", buildGroundingJson(wmsService.USER.getEmployee())));
        list.add(new BasicNameValuePair("plant", wmsService.RnBoxList.get(0).getRvb01().substring(4,7)));
        HttpReq httpReq = new HttpReq(list, HttpPath.getConfirmGroundingPath(),
                mhandler, ActionList.GET_CONFIRMGROUNDING_HANDLER_TYPE, GroundingActivity.context);
        httpReq.start();
    }

    public String buildGroundingJson(String employer){
        Map returnMap = new HashMap<>();
        Map<String, String> headMap = new HashMap<>();
        headMap.put("rva01", wmsService.RnBoxList.get(0).getRvb01());
        headMap.put("rvu07",employer);
        returnMap.put("head", headMap);
        List<Map> bodyList1 = new ArrayList<>();
        for (RnBox rnBox : wmsService.RnBoxList) {
            if(rnBox.getLabels().size()>0){
                for (Label label : rnBox.getLabels()){
                    Map<String, String> bodyMap1 = new HashMap<>();
                    bodyMap1.put("rvb02", rnBox.getRvb02());//收货单项次
                    bodyMap1.put("rvv32", rnBox.getRvv32());//库位
                    bodyMap1.put("rvv33", rnBox.getRvv33());//储位
                    bodyMap1.put("barcode", label.getLabelId());//箱号
                    bodyMap1.put("rvb05", label.getRvb05() + "");//物料编号
                    bodyMap1.put("rvv17", label.getQty()+"");//实际数量
                    bodyList1.add(bodyMap1);
                }
            }else {
                if(!rnBox.getRvv32().equals("") && !rnBox.getRvv33().equals("")){
                    Map<String, String> bodyMap1 = new HashMap<>();
                    bodyMap1.put("rvb02", rnBox.getRvb02());//收货单项次
                    bodyMap1.put("rvv32", rnBox.getRvv32());//库位
                    bodyMap1.put("rvv33", rnBox.getRvv33());//储位
                    bodyMap1.put("barcode", rnBox.getBarcode());//箱号
                    bodyMap1.put("rvb05", rnBox.getRvb05() + "");//物料编号
                    bodyMap1.put("rvv17", rnBox.getIbb07()+"");//实际数量
                    bodyList1.add(bodyMap1);
                }
            }
        }
        returnMap.put("body1", bodyList1);
        System.out.println(JSON.toJSONString(returnMap));
        return JSON.toJSONString(returnMap);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_input_grounding:
                Intent intent = new Intent(ActionList.ACTION_INPUT_GROUNDING);
                sendBroadcast(intent);
                break;
        }
    }


}
