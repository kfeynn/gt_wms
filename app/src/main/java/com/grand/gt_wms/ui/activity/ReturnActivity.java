package com.grand.gt_wms.ui.activity;

import android.content.Intent;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;

import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import com.grand.gt_wms.R;
import com.grand.gt_wms.bean.Box;
import com.grand.gt_wms.bean.PN;
import com.grand.gt_wms.bean.PNSub;
import com.grand.gt_wms.service.wmsService;
import com.grand.gt_wms.tool.ActionList;
import com.grand.gt_wms.tool.HttpPath;
import com.grand.gt_wms.tool.HttpReq;
import com.grand.gt_wms.tool.PopUtil;
import com.grand.gt_wms.ui.adapter.ReturnAdapter;
import com.grand.gt_wms.ui.dialog.EditTextDialog;
import com.grand.gt_wms.ui.dialog.InputDialog;
import com.grand.gt_wms.ui.adapter.ReceivingAdapter;
import com.grand.gt_wms.ui.groupedadapter.adapter.GroupedRecyclerViewAdapter;
import com.grand.gt_wms.ui.groupedadapter.holder.BaseViewHolder;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/8/2.
 * 退货页面
 */

public class ReturnActivity extends BaseActivity implements View.OnClickListener {
    private Button btn_input_dnnum, btn_input_boxnum, btn_input_return;
    private TextView tv_show_dnnum;
    private String dn_num;
    private RecyclerView mRecyclerView;
    private ReturnAdapter returnAdapter;
    private JSONObject jsonObject;
    private List<BasicNameValuePair> list = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return);
        init();
    }

    private void init() {
        setTitle(itemArray[13]);  //对应到数组12
        back();
        btn_input_boxnum = findViewById(R.id.btn_input_boxnum);
        btn_input_dnnum = findViewById(R.id.btn_input_dnnum);
        btn_input_return = findViewById(R.id.btn_input_return);
        tv_show_dnnum = findViewById(R.id.tv_show_dnnum);
        btn_input_dnnum.setOnClickListener(this);
        btn_input_boxnum.setOnClickListener(this);
        btn_input_return.setOnClickListener(this);
        mRecyclerView = findViewById(R.id.gridview_return);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        returnAdapter = new ReturnAdapter(this);
        returnAdapter.setOnHeaderClickListener(new GroupedRecyclerViewAdapter.OnHeaderClickListener() {
            @Override
            public void onHeaderClick(GroupedRecyclerViewAdapter adapter, BaseViewHolder holder,
                                      int groupPosition) {
                ReturnAdapter expandableAdapter = (ReturnAdapter) adapter;
                if (expandableAdapter.isExpand(groupPosition)) {
                    expandableAdapter.collapseGroup(groupPosition);
                } else {
                    expandableAdapter.expandGroup(groupPosition);
                }
            }
        });
/*        receivingAdapter.setOnChildClickListener(new GroupedRecyclerViewAdapter.OnChildClickListener() {
            @Override
            public void onChildClick(GroupedRecyclerViewAdapter adapter, BaseViewHolder holder,
                                     int groupPosition, int childPosition) {
*//*                Toast.makeText(ReceivingActivity.this, "子项：groupPosition = " + groupPosition
                                + ", childPosition = " + childPosition,
                        Toast.LENGTH_LONG).show();*//*
            }
        });*/
        mRecyclerView.setAdapter(returnAdapter);
    }

    @Override
    protected void showPnsubs1(String dnnum) {
        super.showPnsubs1(dnnum);
        tv_show_dnnum.setText("退货单号：" + dnnum);
        closeKeyboard();
        returnAdapter.notifyDataChanged();
    }

    @Override
    protected void updatePnsubs1(double weight, boolean ifStop, String pmn04, boolean ifInput) {
        super.updatePnsubs1(weight, ifStop, pmn04, ifInput);
        returnAdapter.notifyDataChanged();
        if (weight != 0.0) {
            inputWeightDialog(weight, ifStop, pmn04, ifInput);
        }
        if (ifStop && weight == 0.0) {
            if (ifInput) {
                commonDialog("实际退货数量与退货单不符，是否强制确定退货", 10);
            } else {
                commonDialog("是否确定退货", 10);
            }
        }
    }

    //退货扫描完提交请求
    @Override
    protected void confirmReturn(String hintString) {
        super.confirmReturn(hintString);
        list = new ArrayList<BasicNameValuePair>();
        list.add(new BasicNameValuePair("PN", buildReturnJson(wmsService.pn, wmsService.USER.getEmployee(),hintString)));
        list.add(new BasicNameValuePair("plant", wmsService.pn.getPlant()));
        HttpReq httpReq = new HttpReq(list, HttpPath.getConfirmReturnPath(),
                mhandler, ActionList.GET_CONFIRMRETURN_HANDLER_TYPE, ReturnActivity.context);
        httpReq.start();
    }




    private boolean inputWeightDialog(final double weight, final boolean ifStop, final String pmn04, final boolean ifInput) {
        //final InputWeightDialog dialog = new InputWeightDialog(this, weight, pmn04);
        final EditTextDialog dialog = new EditTextDialog(this,weight+"","请输入"+pmn04+"的重量","kg");
        dialog.setOnPositiveListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dialog.editText.getText().toString().equals("")){
                    PopUtil.showPopUtil("输入有误，请重新输入");
                    return;
                }
                for (PNSub pnSub : wmsService.pn.getPnsubs()) {
                    if (pnSub.isIfChangeWeight() && pnSub.getPmn04().equals(pmn04)) {
                        DecimalFormat df = new DecimalFormat("#.000");
                        //按比例分配计价数量
                        double real_pmn87 = (((pnSub.getPmn20_copy() - pnSub.getPmn20()) / pnSub.getPmn20_copy())
                                * pnSub.getPmn87() / weight) * Double.parseDouble(dialog.editText.getText().toString());
                        pnSub.setReal_pmn87(Double.parseDouble(df.format(real_pmn87)));
                        pnSub.setIfChangeWeight(false);
                    }
                }
                dialog.cancel();
                if (ifStop) {
                    commonDialog("是否确定退货", 2);
                }
                if (ifInput) {
                    Intent intent = new Intent(ActionList.ACTION_INPUT_RETURN);
                    sendBroadcast(intent);     //发送广播给对应的Service
                }
            }
        });
        dialog.setOnNegativeListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        dialog.show();
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth()); //设置宽度
        dialog.getWindow().setAttributes(lp);
        return false;
    }

    public String buildReturnJson(PN pn, String employee, String hintString) {
        Map returnMap = new HashMap<>();
        Map<String, String> headMap = new HashMap<>();
        headMap.put("pmm01", pn.getDnnum().substring(2));
        headMap.put("pmm09", pn.getSupid());
        headMap.put("pmm04", pn.getPmn33());
        headMap.put("rvauser", employee);
        headMap.put("rvaud03", pn.getImporttype()!= null ? pn.getImporttype():"");
        if(hintString.equals("实际退货数量与退货单不符，是否强制确定退货")){
            headMap.put("qzf", "Y");
        }else {
            headMap.put("qzf", "N");
        }
        returnMap.put("head", headMap);
        List<Map> bodyList = new ArrayList<>();
        List<PNSub> pnSubs = pn.getPnsubs();
        for (PNSub pnSub : pnSubs) {
            List<Box> boxs = pnSub.getBoxs();
            for (Box box : boxs) {
                Map<String, String> bodyMap = new HashMap<>();
                bodyMap.put("pmn01", pnSub.getPmm01());
                bodyMap.put("pmn02", pnSub.getPmn02() + "");
                bodyMap.put("lotnumber", box.getBoxnum());
                bodyMap.put("pmn04", pnSub.getPmn04());
                bodyMap.put("pmn20", String.valueOf(box.getPmn20()));
                //bodyMap.put("pmn20a", pnSub.getReal_pmn87() + "");
                bodyMap.put("pmn20a", pnSub.getPmn87() + "");
                bodyMap.put("rvbud04",pn.getImportnumber()!= null ? pn.getImportnumber():"");
                bodyList.add(bodyMap);
            }
        }
        returnMap.put("body", bodyList);
        System.out.println(JSON.toJSONString(returnMap));
        return JSON.toJSONString(returnMap);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_input_dnnum:
                inputDialog("查询退货单", "退货单号", 1);
                break;
            case R.id.btn_input_boxnum:
                inputDialog("查询箱号", "箱号", 2);
                break;
            case R.id.btn_input_return:
                Intent intent = new Intent(ActionList.ACTION_INPUT_RETURN);
                sendBroadcast(intent);
                break;
        }
    }

    private void inputDialog(String title, String key, final int Listenertype) {
        final InputDialog dialog = new InputDialog(this, title, key);
        final Intent intent = new Intent(ActionList.ACTION_QR_DATA_RETURN);
        dialog.setOnPositiveListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (Listenertype) {
                    case 1://输入送货单号
                        String dnnum = dialog.et_value.getText().toString() + "\r";
                        intent.putExtra("qr_data", dnnum);
                        sendBroadcast(intent);
                        break;
                    case 2://输入箱号
                        String boxnum = dialog.et_value.getText().toString() + "\r";
                        intent.putExtra("qr_data", boxnum);
                        sendBroadcast(intent);
                        break;
                }
                dialog.cancel();
            }
        });
        dialog.setOnNegativeListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        dialog.show();
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth()); //设置宽度
        dialog.getWindow().setAttributes(lp);
    }

}
