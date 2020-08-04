package com.grand.gt_wms.module;
/**
 * 此service主要用于场内调拨的逻辑处理
 */
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.grand.gt_wms.bean.AKbox;
import com.grand.gt_wms.bean.AKbox;
import com.grand.gt_wms.service.wmsBroadcastReceiver;
import com.grand.gt_wms.service.wmsService;
import com.grand.gt_wms.tool.ActionList;
import com.grand.gt_wms.tool.HttpPath;
import com.grand.gt_wms.tool.HttpReq;
import com.grand.gt_wms.tool.PopUtil;
import com.grand.gt_wms.ui.activity.PackingActivity;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AllotManagerService implements wmsBroadcastReceiver.HalAllotCallback {
    private Context context;
    private List<BasicNameValuePair> list = null;
    private JSONObject jsonObject;
    private String qr_data;
    public AllotManagerService(Context context) {
        this.context=context;
        wmsBroadcastReceiver.getInstance().registerHalAllotCallback(this);   //创建一个广播接收器并重写注册方法
    }


    @Override
    public void onQueryAkNum(Intent intent, String qr_data) {
        wmsService.AKBoxList=null;     //初始化值List
        this.qr_data = qr_data;
       list=new ArrayList<BasicNameValuePair>();
       list.add(new BasicNameValuePair("akNum",qr_data));
       getUrlData(list, HttpPath.appGetAKboxPath(), ActionList.GET_AK_BY_AKNUM_HANDLER_TYPE, PackingActivity.context);
    }
    //处理获取到的数据字符串
    public void getAkBoxList(String data) throws JSONException {
        int i = 0;
        jsonObject = new JSONObject(data);
        String msg = jsonObject.getString("message");
        String data1 = jsonObject.getString("data");
        int code = jsonObject.getInt("status");
        Gson gson = new Gson();
        if (code==0) {
            wmsService.AKBoxList = gson.fromJson(data1, new TypeToken<List<AKbox>>(){}.getType());
            //Collections.sort(wmsService.PKBoxList);
            Intent intent = new Intent(ActionList.ACTION_SHOW_LIST1);
            for (AKbox aKbox : wmsService.AKBoxList){    //循环判断是否有条码
                if(aKbox.getRvbs04().equals("")){
                    i++;
                    aKbox.setIfScanedBox(true);   //如果没条码就直接把扫描否给是
                }
                if(i==wmsService.AKBoxList.size()){        //处理完
                    intent.putExtra("ifAutoAllot",true);
                    intent.putExtra("imm01",qr_data);
                }else {
                    intent.putExtra("ifAutoAllot",false);
                    intent.putExtra("imm01",qr_data);
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
    @Override //扫描物料条码
    public void onScanBoxNum(String qr_data) {
        boolean flag = false;
        int i = 0;
        if(wmsService.AKBoxList == null){
            PopUtil.showPopUtil("请先扫描调拨单单二维码");
            return;
        }
        for(AKbox aKbox : wmsService.AKBoxList){
            if(aKbox.getRvbs04().equals(qr_data)){
                aKbox.setIfScanedBox(true);
                flag = true;
                Intent intent = new Intent(ActionList.ACTION_SHOW_LIST1);   //
                intent.putExtra("imm01",this.qr_data);
                context.sendBroadcast(intent);  //发送广播给activity
            }
        }
        if(!flag){
            PopUtil.showPopUtil("此物料不在这张调拨单中");
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
                case ActionList.GET_AK_BY_AKNUM_HANDLER_TYPE:
                    System.out.println(data);
                    try {
                        getAkBoxList(data);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };


}
