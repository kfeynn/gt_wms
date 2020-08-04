package com.grand.gt_wms.module;
/*
   Created by zouhs on 2019/12/16
 * 此service主要用于仓退单交接的逻辑处理
 */
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.grand.gt_wms.bean.WhrBox;
import com.grand.gt_wms.service.wmsService;
import com.grand.gt_wms.service.wmsBroadcastReceiver;
import com.grand.gt_wms.tool.ActionList;
import com.grand.gt_wms.tool.HttpPath;
import com.grand.gt_wms.tool.HttpReq;
import com.grand.gt_wms.tool.PopUtil;
import com.grand.gt_wms.ui.activity.DumpingActivity;
import com.grand.gt_wms.ui.activity.JoinActivity;
import com.grand.gt_wms.ui.activity.PackingActivity;
import com.grand.gt_wms.ui.activity.WHReturnActivity;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JoinManagerService implements wmsBroadcastReceiver.HalJoinCallback  {
    private Context context;
    private List<BasicNameValuePair> list = null;
    private JSONObject jsonObject;
    private String qr_data;
    public JoinManagerService(Context context) {
        this.context=context;
        wmsBroadcastReceiver.getInstance().registerHalJoinCallback(this);  //创建一个广播接收器并重新注册方法
    }

    @Override //查询数据
    public void onQueryWhReturnNum1(Intent intent, String qr_data) {
        wmsService.whrBoxList=null;   //初始化List
        this.qr_data=qr_data;
        list=new ArrayList<BasicNameValuePair>();
        list.add(new BasicNameValuePair("whNum",qr_data));
        getUrlData(list, HttpPath.appGetWHRboxaPath(), ActionList.GET_WHR_BY_WHNUMA_HANDLER_TYPE,JoinActivity.context);
    }
    //处理获取到的数据字符串
    public void getWhReturnBoxList1(String data) throws JSONException {
        int i = 0;
        jsonObject = new JSONObject(data);
        String msg = jsonObject.getString("message");
        String data1 = jsonObject.getString("data");
        int code = jsonObject.getInt("status");
        Gson gson = new Gson();
        if (code==0) {
            wmsService.whrBoxList = gson.fromJson(data1, new TypeToken<List<WhrBox>>(){}.getType());
            //Collections.sort(wmsService.PKBoxList);
            Intent intent = new Intent(ActionList.ACTION_SHOW_LIST4);
            for (WhrBox whrBox: wmsService.whrBoxList){    //循环判断是否有条码
//                if(bKbox.getRvbs04().equals("")){
                i++;
                whrBox.setIfScanedBox(true);   //如果没条码就直接把扫描否给是，仓退不需要扫描物料条码
//                }
                if(i==wmsService.whrBoxList.size()){        //处理完
                    intent.putExtra("ifAutoJoin",true);
                    intent.putExtra("rvv01",qr_data);
                }else {
                    intent.putExtra("ifAutoJoin",false);
                    intent.putExtra("rvv01",qr_data);
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
        if(wmsService.whrBoxList== null){
            PopUtil.showPopUtil("请先扫描仓退单二维码");
            return;
        }
        for(WhrBox whrBox : wmsService.whrBoxList){
//            if(bkBox.getRvbs04().equals(qr_data)){
            whrBox.setIfScanedBox(true);
            flag = true;
            Intent intent = new Intent(ActionList.ACTION_SHOW_LIST4);
            intent.putExtra("rvv01",this.qr_data);
            context.sendBroadcast(intent);
//            }
        }
        if(!flag){
            PopUtil.showPopUtil("此物料不在这张仓退单中");
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
                case ActionList.GET_WHR_BY_WHNUMA_HANDLER_TYPE :
                    System.out.println(data);
                    try {
                        getWhReturnBoxList1(data);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };
}
