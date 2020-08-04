package com.grand.gt_wms.module;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.grand.gt_wms.bean.ImptAndEmptItem;
import com.grand.gt_wms.bean.RnBox;
import com.grand.gt_wms.bean.WhrBox;
import com.grand.gt_wms.service.wmsBroadcastReceiver;
import com.grand.gt_wms.service.wmsService;
import com.grand.gt_wms.tool.ActionList;
import com.grand.gt_wms.tool.HttpPath;
import com.grand.gt_wms.tool.HttpReq;
import com.grand.gt_wms.tool.PopUtil;
import com.grand.gt_wms.ui.activity.GoodsIptAndEptActivity;
import com.grand.gt_wms.ui.activity.GroundingActivity;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Administrator on 2019/1/10.
 */

public class GoodsIptAndEptManagerService implements wmsBroadcastReceiver.HalGoodsIptAndEptCallback {
    private JSONObject jsonObject;
    private Context context;
    private List<BasicNameValuePair> list = null;
    private String qr_data;
    private String tlf902;    //仓库
    public GoodsIptAndEptManagerService(Context context) {
        this.context = context;
        wmsBroadcastReceiver.getInstance().registerHalGoodsIptAndEptCallback(this);
    }
    @Override
    public void onQueryBoxNum(Intent intent, String qr_data) {
        this.qr_data = qr_data;
//        tlf902="2160";
        list = new ArrayList<BasicNameValuePair>();
        list.add(new BasicNameValuePair("box_num", qr_data));
        list.add(new BasicNameValuePair("tlf902", wmsService.TLF902));   //仓库
        list.add(new BasicNameValuePair("start_time",wmsService.START_TIME));
        list.add(new BasicNameValuePair("end_time",wmsService.END_TIME));
        getUrlData(list, HttpPath.appGetIptAndEptPath(),
                ActionList.GET_ITEM_BY_BOX_HANDLER_TYPE, GoodsIptAndEptActivity.context);

    }

    /**
     * 通过一个箱号查出对应物料进出明细列表
     *
     * @param
     */
    public void getGoodsIptAndEptItemByBoxnum(String backData) throws JSONException {
        jsonObject = new JSONObject(backData);
        String msg = jsonObject.getString("message");
        String data1 = jsonObject.getString("data");
        int code = jsonObject.getInt("status");
        Gson gson = new Gson();
        if (code==0) {
            wmsService.imptAndEmptItemsList = gson.fromJson(data1, new TypeToken<List<ImptAndEmptItem>>(){}.getType());
            //Collections.sort(wmsService.imptAndEmptItemsList);
            Intent intent = new Intent(ActionList.ACTION_SHOW_GoodsIptAndEptItem);
            intent.putExtra("box_num",qr_data);
            context.sendBroadcast(intent);
        }else {
            PopUtil.showPopUtil(msg);

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
                case ActionList.GET_ITEM_BY_BOX_HANDLER_TYPE:
                    try {
                        getGoodsIptAndEptItemByBoxnum(data);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };
}
