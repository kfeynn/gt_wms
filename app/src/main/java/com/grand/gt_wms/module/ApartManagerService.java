package com.grand.gt_wms.module;
/*
 * 此service主要用于拆解单的逻辑处理
 */
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.grand.gt_wms.service.wmsBroadcastReceiver;
import com.grand.gt_wms.service.wmsService;
import com.grand.gt_wms.tool.ActionList;
import com.grand.gt_wms.tool.HttpPath;
import com.grand.gt_wms.tool.HttpReq;
import com.grand.gt_wms.tool.PopUtil;
import com.grand.gt_wms.bean.CKbox;
import com.grand.gt_wms.ui.activity.ApartActivity;


import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ApartManagerService implements wmsBroadcastReceiver.HalApartCallback {
    private Context context;
    private List<BasicNameValuePair> list = null;
    private JSONObject jsonObject;
    private String qr_data;
    public ApartManagerService(Context context) {
        this.context=context;
        wmsBroadcastReceiver.getInstance().registerHalApartCallback(this);   //实例话一个对象，此对象为static的
    }


    @Override
    public void onQueryCkNum(Intent intent, String qr_data) {
        wmsService.CKBoxList = null;
        this.qr_data = qr_data;
        list = new ArrayList<BasicNameValuePair>();
        list.add(new BasicNameValuePair("ckNum", qr_data));
        getUrlData(list, HttpPath.appGetCKboxPath(), ActionList.GET_CK_BY_CKNUM_HANDLER_TYPE, ApartActivity.context);
    }
    //处理获取到的数据字符串
    public void getCkBoxList(String data) throws JSONException {
        int i = 0;
        jsonObject = new JSONObject(data);
        String msg = jsonObject.getString("message");
        String data1 = jsonObject.getString("data");
        int code = jsonObject.getInt("status");
        Gson gson = new Gson();
        if (code==0) {
            wmsService.CKBoxList = gson.fromJson(data1, new TypeToken<List<CKbox>>(){}.getType());
            //Collections.sort(wmsService.PKBoxList);
            Intent intent = new Intent(ActionList.ACTION_SHOW_LIST3);
            for (CKbox cKbox : wmsService.CKBoxList){    //循环判断是否有条码
//                if(cKbox.getRvbs04().equals("")){
                    i++;
                    cKbox.setIfScanedBox(true);   //如果没条码就直接把扫描否给是
//                }
                if(i==wmsService.CKBoxList.size()){        //处理完
                    intent.putExtra("ifAutoApart",true);
                    intent.putExtra("tse01",qr_data);
                }else {
                    intent.putExtra("ifAutoApart",false);
                    intent.putExtra("tse01",qr_data);
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
                case ActionList.GET_CK_BY_CKNUM_HANDLER_TYPE:
                    System.out.println(data);
                    try {
                        getCkBoxList(data);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };
    @Override
    public void onScanBoxNum(String qr_data) {
        boolean flag = false;
        int i = 0;
        if(wmsService.CKBoxList== null){
            PopUtil.showPopUtil("请先扫描报废单二维码");
            return;
        }
        for(CKbox ckBox : wmsService.CKBoxList){
//            if(bkBox.getRvbs04().equals(qr_data)){
            ckBox.setIfScanedBox(true);
            flag = true;
            Intent intent = new Intent(ActionList.ACTION_SHOW_LIST3);
            intent.putExtra("tse01",this.qr_data);
            context.sendBroadcast(intent);
//            }
        }
        if(!flag){
            PopUtil.showPopUtil("此物料不在这张报废单中");
            return;
        }
    }
}
