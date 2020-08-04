package com.grand.gt_wms.module;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.grand.gt_wms.bean.PKbox;
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
import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2018/11/12.
 */

public class PackManagerService implements wmsBroadcastReceiver.HalPackingCallback{
    private Context context;
    private List<BasicNameValuePair> list = null;
    private JSONObject jsonObject;
    private String qr_data;
    public PackManagerService(Context context) {
        this.context = context;
        wmsBroadcastReceiver.getInstance().registerHalPackingCallback(this);
    }

    @Override
    public void onQueryPkNum(Intent intent, String qr_data) {
        wmsService.PKBoxList = null;
        this.qr_data = qr_data;
        list = new ArrayList<BasicNameValuePair>();
        list.add(new BasicNameValuePair("pkNum", qr_data));
        getUrlData(list, HttpPath.appGetPKboxPath(), ActionList.GET_PK_BY_PKNUM_HANDLER_TYPE, PackingActivity.context);
    }

    public void getPkBoxList(String data) throws JSONException {
        int i = 0;
        jsonObject = new JSONObject(data);
        String msg = jsonObject.getString("message");
        String data1 = jsonObject.getString("data");
        int code = jsonObject.getInt("status");
        Gson gson = new Gson();
        if (code==0) {
            wmsService.PKBoxList = gson.fromJson(data1, new TypeToken<List<PKbox>>(){}.getType());
            //Collections.sort(wmsService.PKBoxList);
            Intent intent = new Intent(ActionList.ACTION_SHOW_LIST);
            for (PKbox pKbox : wmsService.PKBoxList){
                if(pKbox.getRvbs04().equals("")){
                    i++;
                    pKbox.setIfScanedBox(true);
                }
                if(i==wmsService.PKBoxList.size()){
                    intent.putExtra("ifAutoPacking",true);
                    intent.putExtra("sfp01",qr_data);
                }else {
                    intent.putExtra("ifAutoPacking",false);
                    intent.putExtra("sfp01",qr_data);
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
        if(wmsService.PKBoxList == null){
            PopUtil.showPopUtil("请先扫描发料单二维码");
            return;
        }
        for(PKbox pKbox : wmsService.PKBoxList){
            if(pKbox.getRvbs04().equals(qr_data)){
                pKbox.setIfScanedBox(true);
                flag = true;
                Intent intent = new Intent(ActionList.ACTION_SHOW_LIST);
                intent.putExtra("sfp01",this.qr_data);
                context.sendBroadcast(intent);
            }
        }
        if(!flag){
            PopUtil.showPopUtil("此物料不在这张发料单中");
            return;
        }
    }

    public void getUrlData(List<BasicNameValuePair> list, String url, int type, Context context) {
        HttpReq httpReq = new HttpReq(list, url, mhandler, type, context);
        httpReq.start();
    }

    private Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String data = msg.getData().getString("data");
            switch (msg.what) {
                case ActionList.GET_PK_BY_PKNUM_HANDLER_TYPE:
                    System.out.println(data);
                    try {
                        getPkBoxList(data);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };
}
