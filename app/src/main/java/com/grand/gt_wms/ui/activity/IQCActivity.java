package com.grand.gt_wms.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.grand.gt_wms.R;
import com.grand.gt_wms.bean.IqcItem;
import com.grand.gt_wms.bean.RnBox;
import com.grand.gt_wms.service.wmsService;
import com.grand.gt_wms.tool.ActionList;
import com.grand.gt_wms.tool.HttpPath;
import com.grand.gt_wms.tool.HttpReq;
import com.grand.gt_wms.tool.PopUtil;
import com.grand.gt_wms.ui.adapter.IQCAdapter;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/12/27.
 * IQC检验页面
 */

public class IQCActivity extends BaseActivity {

    private String box_num;
    private IQCAdapter iqcAdapter;
    private RecyclerView mRecyclerView;
    private List<BasicNameValuePair> list = null;
    private JSONObject jsonObject;
    private TextView tv_qcs01, tv_qcs02, tv_qcs021, tv_ima02, tv_ima021, tv_qcs22;
    private Button btn_UpdateAqct110;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iqc);
        init();


    }
    private void init(){
        setTitle(itemArray[1]);
        back();
        setSearchEdit("箱号");
        onSearchButtonListen();
        tv_qcs01 = findViewById(R.id.tv_qcs01);
        tv_qcs02 = findViewById(R.id.tv_qcs02);
        tv_qcs021 = findViewById(R.id.tv_qcs021);
        tv_ima02 = findViewById(R.id.tv_ima02);
        tv_ima021 = findViewById(R.id.tv_ima021);
        tv_qcs22 = findViewById(R.id.tv_qcs22);
        btn_UpdateAqct110 = findViewById(R.id.ok_button);
        mRecyclerView = findViewById(R.id.gridview_iqcItem);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

/*        iqcAdapter.setOnHeaderClickListener(new GroupedRecyclerViewAdapter.OnHeaderClickListener() {
            @Override
            public void onHeaderClick(GroupedRecyclerViewAdapter adapter, BaseViewHolder holder,
                                      int groupPosition) {
*//*                IQCAdapter expandableAdapter = (IQCAdapter) adapter;
                if (expandableAdapter.isExpand(groupPosition)) {
                    expandableAdapter.collapseGroup(groupPosition);
                } else {
                    expandableAdapter.expandGroup(groupPosition);
                }*//*
                editTextDialog(wmsService.RnBoxList.get(groupPosition).getIbb07(),
                        "请输入此包装的合格数量",wmsService.RnBoxList.get(groupPosition).getRvb86(),groupPosition,1);
            }
        });*/
    }

    @Override
    protected void showBoxList(String boxNum) {
        super.showBoxList(boxNum);
        box_num = boxNum;
        et_search.setText(boxNum);
        int i=0;
        closeKeyboard();
        if (wmsService.iqcItemList != null) {
            tv_qcs01.setText("QC单号："+wmsService.iqcItemList.get(0).getQcs01());
            tv_qcs02.setText("项次："+wmsService.iqcItemList.get(0).getQcs02());
            tv_qcs021.setText("料号："+wmsService.iqcItemList.get(0).getQcs021());
            tv_ima02.setText("品名："+wmsService.iqcItemList.get(0).getIma02());
            tv_ima021.setText("规格："+wmsService.iqcItemList.get(0).getIma021());
            tv_qcs22.setText("送检数量："+wmsService.iqcItemList.get(0).getQcs22());
        }
        iqcAdapter = new IQCAdapter(this,box_num);
        mRecyclerView.setAdapter(iqcAdapter);
        btn_UpdateAqct110.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmIQC();
                closeKeyboard();
            }
        });
    }

/*    public void iQCdialog(String hintString, final int boxId) {
        final MyDialog dialog = new MyDialog(this, hintString);
        dialog.positiveButton.setText("合格");
        dialog.negativeButton.setText("不合格");
        dialog.setOnPositiveListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wmsService.RnBoxList.get(boxId).setQcs14("Y");
                wmsService.RnBoxList.get(boxId).setIqcStatus(1);
                showBoxList(box_num);
                dialog.cancel();
            }
        });
        dialog.setOnNegativeListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wmsService.RnBoxList.get(boxId).setQcs14("N");
                wmsService.RnBoxList.get(boxId).setIqcStatus(2);
                showBoxList(box_num);
                dialog.cancel();
            }
        });
        dialog.show();
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth()); //设置宽度
        dialog.getWindow().setAttributes(lp);
    }*/

    @Override
    protected void confirmIQC() {
        super.confirmIQC();
        list = new ArrayList<BasicNameValuePair>();
        String iQCJson = buildIQCJson(wmsService.USER.getEmployee());
        if(iQCJson != null){
            list.add(new BasicNameValuePair("RN",iQCJson ));
            list.add(new BasicNameValuePair("plant", wmsService.iqcItemList.get(0).getQcs01().substring(4,7)));
            HttpReq httpReq = new HttpReq(list, HttpPath.getConfirmIQCPath(),
                    mhandler, ActionList.GET_CONFIRMIQC_HANDLER_TYPE, IQCActivity.context);
            httpReq.start();
        }
    }


    public String buildIQCJson(String employer){
        Map returnMap = new HashMap<>();
        Map<String, String> headMap = new HashMap<>();
        headMap.put("qcs01", wmsService.iqcItemList.get(0).getQcs01());
        headMap.put("qcs02", wmsService.iqcItemList.get(0).getQcs02());
        headMap.put("qcs021", wmsService.iqcItemList.get(0).getQcs021());
        headMap.put("qcs13", employer);
        returnMap.put("head",headMap);
        List<Map> bodyList = new ArrayList<>();
        for(IqcItem iqcItem : wmsService.iqcItemList){
            Map<String, String> bodyMap = new HashMap<>();
            bodyMap.put("qct03",iqcItem.getQct03());
            bodyMap.put("qct04",iqcItem.getQct04());
            bodyMap.put("azf03",iqcItem.getAzf03());
            bodyMap.put("qctud02",iqcItem.getQctud02());
            bodyMap.put("qct11",iqcItem.getQct11());
            if(iqcItem.getQct07().equals("")){
                bodyMap.put("qct07",Double.valueOf("0").toString());
            }else {
                bodyMap.put("qct07",Double.valueOf(iqcItem.getQct07()).toString());
            }
            bodyMap.put("ta_qctt01",iqcItem.getTa_qctt01());
            bodyList.add(bodyMap);
        }
        returnMap.put("head", headMap);
        returnMap.put("body",bodyList);
        System.out.println(JSON.toJSONString(returnMap));
        return JSON.toJSONString(returnMap);
    }


}
