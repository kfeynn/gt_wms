package com.grand.gt_wms.module;
/*
   Created by zouhs on 2019/12/16
 * 此service主要用于送货单交接的逻辑处理
 */

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.grand.gt_wms.bean.RnBox;
import com.grand.gt_wms.bean.WhrBox;
import com.grand.gt_wms.service.wmsBroadcastReceiver;
import com.grand.gt_wms.service.wmsService;
import com.grand.gt_wms.tool.ActionList;
import com.grand.gt_wms.tool.HttpPath;
import com.grand.gt_wms.tool.HttpReq;
import com.grand.gt_wms.tool.PopUtil;
import com.grand.gt_wms.ui.activity.Join1Activity;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Join1ManagerService implements wmsBroadcastReceiver.HalJoin1Callback  {
    private Context context;
    private List<BasicNameValuePair> list = null;
    private JSONObject jsonObject;
    private String qr_data;
    public Join1ManagerService(Context context) {
        this.context=context;
        wmsBroadcastReceiver.getInstance().registerHalJoin1Callback(this);  //创建一个广播接收器并重新注册方法
    }

    @Override //查询数据
    public void onQueryDnnum1(Intent intent, String qr_data) {
        wmsService.RnBoxList=null;   //初始化List
        this.qr_data=qr_data;
        list=new ArrayList<BasicNameValuePair>();
        list.add(new BasicNameValuePair("box_num",qr_data.substring(2)));
        getUrlData(list, HttpPath.appGetRnboxaPath(), ActionList.GET_WHR_BY_RNNUM_HANDLER_TYPE,Join1Activity.context);
    }
    //处理获取到的数据字符串
    public void getRecevingBoxList1(String data) throws JSONException {
        int i = 0;
        jsonObject = new JSONObject(data);
        String msg = jsonObject.getString("message");
        String data1 = jsonObject.getString("data");
        int code = jsonObject.getInt("status");
        Gson gson = new Gson();
        if (code==0) {
            wmsService.RnBoxList = gson.fromJson(data1, new TypeToken<List<RnBox>>(){}.getType());
            //Collections.sort(wmsService.PKBoxList);
            Intent intent = new Intent(ActionList.ACTION_SHOW_LIST5);
            for (RnBox rnBox: wmsService.RnBoxList){    //循环判断是否有条码
//                if(bKbox.getRvbs04().equals("")){
                String plant=rnBox.getRvb01().substring(4,7);   //运营中心
                i++;
                rnBox.setIfScanedBox(true);   //如果没条码就直接把扫描否给是，仓退不需要扫描物料条码
//                }
                if(i==wmsService.RnBoxList.size()){        //处理完
                    intent.putExtra("ifAutoJoin1",true);
                    intent.putExtra("rva07",qr_data);
                    intent.putExtra("plant",plant);
                }else {
                    intent.putExtra("ifAutoJoin1",false);
                    intent.putExtra("rva07",qr_data);
                    intent.putExtra("plant",plant);
                }
                context.sendBroadcast(intent);
            }
        }/*else if(code==1){
            Toast.makeText(context, msg,
                    Toast.LENGTH_LONG).show();
            Intent intent = new Intent(ActionList.ACTION_SHOW_LIST);
            intent.putExtra("ifAutoPacking",true);
            intent.putExtra("sfp01",qr_data);
            context.sendBroadcast(intent);
        }*/else {
            PopUtil.showPopUtil(msg);
        }
    }
    @Override
    public void onScanBoxNum(String qr_data) {
        boolean flag = false;
        int i = 0;
        if(wmsService.RnBoxList== null){
            PopUtil.showPopUtil("请先扫描送货单二维码");
            return;
        }
        for(RnBox rnBox : wmsService.RnBoxList){
//            if(bkBox.getRvbs04().equals(qr_data)){
            rnBox.setIfScanedBox(true);
            flag = true;
            Intent intent = new Intent(ActionList.ACTION_SHOW_LIST5);
            intent.putExtra("rva07",this.qr_data);
            context.sendBroadcast(intent);
//            }
        }
        if(!flag){
            PopUtil.showPopUtil("此物料不在这张送货单中");
            return;
        }
    }
    private void getUrlData(List<BasicNameValuePair> list, String url, int type, Context context) {
        HttpReq httpReq = new HttpReq(list, url, mhandler, type, context);
        httpReq.start();
    }
    private Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String data = msg.getData().getString("data");
            switch (msg.what) {
                case ActionList.GET_WHR_BY_RNNUM_HANDLER_TYPE :
                    System.out.println(data);
                    try {
                        getRecevingBoxList1(data);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };
}
