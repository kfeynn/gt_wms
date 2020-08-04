package com.grand.gt_wms.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.grand.gt_wms.R;
import com.grand.gt_wms.bean.ImgsBox;
import com.grand.gt_wms.bean.RnBox;
import com.grand.gt_wms.service.wmsService;
import com.grand.gt_wms.tool.ActionList;
import com.grand.gt_wms.tool.HttpPath;
import com.grand.gt_wms.tool.HttpReq;
import com.grand.gt_wms.ui.adapter.GoodsMoveAdapter;
import com.grand.gt_wms.ui.adapter.GroundingAdapter;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2019/1/10.
 */

public class GoodsMoveActivity extends BaseActivity{
    private String box_num;
    private RecyclerView mRecyclerView;
    private GoodsMoveAdapter goodsMoveAdapter;
    private List<BasicNameValuePair> list = null;
    private JSONObject jsonObject;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goodsmove);
        init();
    }

    private void init(){
        setTitle(itemArray[6]);
        back();
        setSearchEdit("箱号");
        onSearchButtonListen();
        mRecyclerView = findViewById(R.id.gridview_grounding);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        goodsMoveAdapter = new GoodsMoveAdapter(this,box_num);
/*        groundingAdapter.setOnHeaderClickListener(new GroupedRecyclerViewAdapter.OnHeaderClickListener() {
            @Override
            public void onHeaderClick(GroupedRecyclerViewAdapter adapter, BaseViewHolder holder,
                                      int groupPosition) {
                GroundingAdapter expandableAdapter = (GroundingAdapter) adapter;
                if (expandableAdapter.isExpand(groupPosition)) {
                    expandableAdapter.collapseGroup(groupPosition);
                } else {
                    expandableAdapter.expandGroup(groupPosition);
                }
            }
        });*/
        mRecyclerView.setAdapter(goodsMoveAdapter);
    }

    @Override
    protected void showBoxList(String boxNum) {
        super.showBoxList(boxNum);
        box_num = boxNum;
        et_search.setText(boxNum);
        if (wmsService.imgsBoxList != null) {
            closeKeyboard();
            goodsMoveAdapter.notifyDataChanged();
        }else{
            closeKeyboard();
            goodsMoveAdapter.notifyDataChanged();
        }
    }

    @Override
    protected void showBoxWaddr(boolean ifStop) {
        super.showBoxWaddr(ifStop);
        goodsMoveAdapter.notifyDataChanged();
        if(ifStop){
            commonDialog("是否确定调拨",8);
        }
    }

    @Override
    protected void confirmGoodsMove() {
        super.confirmGoodsMove();
        list = new ArrayList<BasicNameValuePair>();
        list.add(new BasicNameValuePair("RN", buildGoodsMoveJson(wmsService.USER.getEmployee())));
        list.add(new BasicNameValuePair("plant", wmsService.imgsBoxList.get(0).getImgsplant()));
        HttpReq httpReq = new HttpReq(list, HttpPath.getConfirmGoodsMovePath(),
                mhandler, ActionList.GET_CONFIRMGOODSMOVE_HANDLER_TYPE, GoodsMoveActivity.context);
        httpReq.start();
    }

    public String buildGoodsMoveJson(String employer){
        Map returnMap = new HashMap<>();
        Map<String, String> headMap = new HashMap<>();
        headMap.put("implant",wmsService.imgsBoxList.get(0).getImgsplant());
        headMap.put("imm16",employer);
        returnMap.put("head", headMap);
        List<Map> bodyList1 = new ArrayList<>();
        for (ImgsBox imgsBox : wmsService.imgsBoxList) {
            Map<String, String> bodyMap1 = new HashMap<>();
            bodyMap1.put("imn04", imgsBox.getImgs02());//库位
            bodyMap1.put("imn05", imgsBox.getImgs05());//储位
            bodyMap1.put("imn09", imgsBox.getImgs07());//拨出单位
            bodyMap1.put("barcode", imgsBox.getImgs06());//箱号
            bodyMap1.put("imn03", imgsBox.getImgs01() + "");//物料编号
            bodyMap1.put("imn10", imgsBox.getImgs08()+"");//实际数量
            bodyMap1.put("imn15", imgsBox.getImn15());//拨入库位
            bodyMap1.put("imn16", imgsBox.getImn16());//拨入储位
            bodyList1.add(bodyMap1);
        }
        returnMap.put("body1", bodyList1);
        System.out.println(JSON.toJSONString(returnMap));
        return JSON.toJSONString(returnMap);
    }


}
