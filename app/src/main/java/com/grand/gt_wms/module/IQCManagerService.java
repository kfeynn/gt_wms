package com.grand.gt_wms.module;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.google.gson.Gson;
import com.grand.gt_wms.bean.IqcItem;
import com.grand.gt_wms.bean.RnBox;
import com.grand.gt_wms.service.wmsBroadcastReceiver;
import com.grand.gt_wms.service.wmsService;
import com.grand.gt_wms.tool.ActionList;
import com.grand.gt_wms.tool.HttpPath;
import com.grand.gt_wms.tool.HttpReq;
import com.grand.gt_wms.tool.PopUtil;
import com.grand.gt_wms.ui.activity.GroundingActivity;
import com.grand.gt_wms.ui.activity.IQCActivity;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Administrator on 2018/12/27.
 */

public class IQCManagerService implements wmsBroadcastReceiver.HalIQCCallback  {
    private List<BasicNameValuePair> list = null;
    private JSONObject jsonObject;
    private Context context;
    private String qr_data;

    public IQCManagerService(Context context) {
        this.context = context;
        wmsBroadcastReceiver.getInstance().registerHalIQCCallback(this);
    }

    @Override
    public void onScanBoxnum(Intent intent, String qr_data) {
        this.qr_data = qr_data;
        wmsService.iqcItemList = null;
        list = new ArrayList<BasicNameValuePair>();
        list.add(new BasicNameValuePair("box_num", qr_data));
        getUrlData(list, HttpPath.getIQCFormPath(),
                ActionList.GET_IQCFORM_BY_BOXNUM_HANDLER_TYPE, IQCActivity.context);
    }

    /**
     * 通过一个箱号查出关联收货单列表
     *
     * @param
     */
    public void getIqcItemListByBoxnum(String backData) {
        if (backData.equals("获取失败")) {
            wmsService.iqcItemList = null;
            PopUtil.showPopUtil("送货单号错误或者网络连接失败，请重新检查");
        } else {
            try {
                jsonObject = new JSONObject(backData);
                String msg = jsonObject.getString("message");
                String data = jsonObject.getString("data");
                Gson gson = new Gson();
                if (msg.equals("success")) {
                    wmsService.iqcItemList = JSONArray.parseArray(data, IqcItem.class);
                } else {
                    PopUtil.showPopUtil(msg);
                    wmsService.iqcItemList = null;
                }
                Intent intent = new Intent(ActionList.ACTION_SHOW_BOXLIST);
                intent.putExtra("box_num", qr_data);
                context.sendBroadcast(intent);
            } catch (JSONException e) {
                e.printStackTrace();
            }
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
                case ActionList.GET_IQCFORM_BY_BOXNUM_HANDLER_TYPE:
                    getIqcItemListByBoxnum(data);
                    break;
            }
        }
    };
}
