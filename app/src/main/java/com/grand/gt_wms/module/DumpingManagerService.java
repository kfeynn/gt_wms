package com.grand.gt_wms.module;
/*
 * 此service主要用于下阶报废的逻辑处理
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
import com.grand.gt_wms.bean.BKbox;
import com.grand.gt_wms.bean.WhrBox;
import com.grand.gt_wms.service.wmsService;
import com.grand.gt_wms.service.wmsBroadcastReceiver;
import com.grand.gt_wms.tool.ActionList;
import com.grand.gt_wms.tool.HttpPath;
import com.grand.gt_wms.tool.HttpReq;
import com.grand.gt_wms.tool.PopUtil;
import com.grand.gt_wms.ui.activity.DumpingActivity;
import com.grand.gt_wms.ui.activity.PackingActivity;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DumpingManagerService implements wmsBroadcastReceiver.HalDumpingCallback  {
    private Context context;
    private List<BasicNameValuePair> list = null;
    private JSONObject jsonObject;
    private String qr_data;
    public DumpingManagerService(Context context) {
        this.context=context;
        wmsBroadcastReceiver.getInstance().registerHalDumpingCallback(this);  //创建一个广播接收器并重新注册方法
    }

    @Override //查询数据
    public void onQueryBkNum(Intent intent, String qr_data) {
        wmsService.BKBoxList=null;   //初始化List
        this.qr_data=qr_data;
        list=new ArrayList<BasicNameValuePair>();
        list.add(new BasicNameValuePair("bkNum",qr_data));
        getUrlData(list, HttpPath.appGetBKboxPath(), ActionList.GET_BK_BY_BKNUM_HANDLER_TYPE, DumpingActivity.context);
    }
    //处理获取到的数据字符串
    public void getBkBoxList(String data) throws JSONException {
        int i = 0;
        jsonObject = new JSONObject(data);
        String msg = jsonObject.getString("message");
        String data1 = jsonObject.getString("data");
        int code = jsonObject.getInt("status");
        Gson gson = new Gson();
        if (code==0) {
            wmsService.BKBoxList = gson.fromJson(data1, new TypeToken<List<BKbox>>(){}.getType());
            //Collections.sort(wmsService.PKBoxList);
            Intent intent = new Intent(ActionList.ACTION_SHOW_LIST2);
            for (BKbox bKbox : wmsService.BKBoxList){    //循环判断是否有条码
//                if(bKbox.getRvbs04().equals("")){
                    i++;
                    bKbox.setIfScanedBox(true);   //如果没条码就直接把扫描否给是，报废不需要扫描物料条码
//                }
                if(i==wmsService.BKBoxList.size()){        //处理完
                    intent.putExtra("ifAutoDumping",true);
                    intent.putExtra("sfk01",qr_data);
                }else {
                    intent.putExtra("ifAutoDumping",false);
                    intent.putExtra("sfk01",qr_data);
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
        if(wmsService.BKBoxList== null){
            PopUtil.showPopUtil("请先扫描报废单二维码");
            return;
        }
        for(BKbox bkBox : wmsService.BKBoxList){
//            if(bkBox.getRvbs04().equals(qr_data)){
                bkBox.setIfScanedBox(true);
                flag = true;
                Intent intent = new Intent(ActionList.ACTION_SHOW_LIST2);
                intent.putExtra("sfk01",this.qr_data);
                context.sendBroadcast(intent);
//            }
        }
        if(!flag){
            PopUtil.showPopUtil("此物料不在这张报废单中");
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
                case ActionList.GET_BK_BY_BKNUM_HANDLER_TYPE:
                    System.out.println(data);
                    try {
                        getBkBoxList(data);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };
}
