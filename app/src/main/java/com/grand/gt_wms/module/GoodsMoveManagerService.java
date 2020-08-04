package com.grand.gt_wms.module;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.google.gson.Gson;
import com.grand.gt_wms.bean.ImgsBox;
import com.grand.gt_wms.bean.RnBox;
import com.grand.gt_wms.service.wmsBroadcastReceiver;
import com.grand.gt_wms.service.wmsService;
import com.grand.gt_wms.tool.ActionList;
import com.grand.gt_wms.tool.HttpPath;
import com.grand.gt_wms.tool.HttpReq;
import com.grand.gt_wms.tool.PopUtil;
import com.grand.gt_wms.ui.activity.GoodsMoveActivity;
import com.grand.gt_wms.ui.activity.GroundingActivity;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Administrator on 2018/10/15.
 */

public class GoodsMoveManagerService implements wmsBroadcastReceiver.HalGoodsMoveCallback {

    private List<BasicNameValuePair> list = null;
    private JSONObject jsonObject;
    private Context context;
    private String qr_data;

    public GoodsMoveManagerService(Context context) {
        this.context = context;
        wmsBroadcastReceiver.getInstance().registerHalGoodsMoveCallback(this);
    }

    @Override
    public void onScanBoxnum(Intent intent, String qr_data) {
        this.qr_data = qr_data;
        if (wmsService.imgsBoxList == null) {
            list = new ArrayList<BasicNameValuePair>();
            list.add(new BasicNameValuePair("box_num", qr_data));
            getUrlData(list, HttpPath.appGetImgsBoxPath(),
                    ActionList.GET_IMGSBOX_BY_BOXNUM_HANDLER_TYPE, GoodsMoveActivity.context);
        } else {
            int i = 0;
            for (ImgsBox imgsBox : wmsService.imgsBoxList) {
                if (imgsBox.getImgs06().equals(qr_data)) {
                    if (!imgsBox.getImn15().equals("")) {
                        imgsBox.setImn15("");
                        imgsBox.setImn16("");
                    }
                    imgsBox.setIfScanedBox(true);
                } else {
                    imgsBox.setIfScanedBox(false);
                    i++;
                }
            }
            if (i == wmsService.imgsBoxList.size()) {
                list = new ArrayList<BasicNameValuePair>();
                list.add(new BasicNameValuePair("box_num", qr_data));
                getUrlData(list, HttpPath.appGetImgsBoxPath(),
                        ActionList.GET_IMGSBOX_BY_BOXNUM_HANDLER_TYPE, GoodsMoveActivity.context);
            } else {
                intent = new Intent(ActionList.ACTION_SHOW_BOXLIST);
                intent.putExtra("box_num", qr_data);
                context.sendBroadcast(intent);
            }
        }
    }

    @Override
    public void onScanWaddr(Intent intent, String qr_data) {
        intent = new Intent(ActionList.ACTION_SHOW_BOXWADDR);
        this.qr_data = qr_data;
        int i = 0;
        if (wmsService.imgsBoxList != null) {
            for (ImgsBox imgsBox : wmsService.imgsBoxList) {
                if (imgsBox.isIfScanedBox()) {
                    try {
                        jsonObject = new JSONObject(qr_data);
                        String imn15 = jsonObject.getString("rvv32");
                        String imn16 = jsonObject.getString("rvv33");
                        if(!imn15.equals(imgsBox.getImgs02()) || !imn16.equals(imgsBox.getImgs05())){
                            imgsBox.setImn15(imn15);
                            imgsBox.setImn16(imn16);
                        }else {
                            PopUtil.showPopUtil("需要调拨的位置和原位置一样，请检查");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            for (ImgsBox imgsBox1 : wmsService.imgsBoxList) {
                if (!imgsBox1.getImn15().equals("")) {
                    i++;
                }
                if (i == wmsService.imgsBoxList.size()) {
                    if (!ifSameWaddr(wmsService.imgsBoxList)) {
                        PopUtil.showPopUtil("同物料仓储位不同，请检查");
                        intent.putExtra("ifStop", false);
                        break;
                    }
                    intent.putExtra("ifStop", true);
                } else {
                    intent.putExtra("ifStop", false);
                }
            }
            context.sendBroadcast(intent);
            return;
        } else {
            PopUtil.showPopUtil("请先扫描包装票的二维码");
        }
    }

    private boolean ifSameWaddr(List<ImgsBox> imgsBoxList) {
        for (int i = 0; i < imgsBoxList.size() - 1; i++) {
            if (!imgsBoxList.get(i).getImn15().equals(imgsBoxList.get(i + 1).getImn15()) ||
                    !imgsBoxList.get(i).getImn16().equals(imgsBoxList.get(i + 1).getImn16())) {
                return false;
            }
        }
        return true;
    }

    /**
     * 通过一个箱号查出关联收货单列表
     *
     * @param
     */
    public void getRnBoxListByBoxnum(String backData) {
        if (backData.equals("获取失败")) {
            wmsService.pn = null;
            PopUtil.showPopUtil("送货单号错误或者网络连接失败，请重新检查");
        } else {
            try {
                jsonObject = new JSONObject(backData);
                String msg = jsonObject.getString("message");
                String data = jsonObject.getString("data");
                Gson gson = new Gson();
                if (msg.equals("success")) {
                    wmsService.imgsBoxList = JSONArray.parseArray(data, ImgsBox.class);
                    for (ImgsBox imgsBox : wmsService.imgsBoxList) {
                        if (imgsBox.getImgs06().equals(qr_data)) {
/*                            if (Double.valueOf(rnbox.getRvb33()) != 0) {
                                if (rnbox.getRvb39().equals("N") || rnbox.getQcs14().equals("Y")) {
                                    rnbox.setIfScanedBox(true);
                                } else {
                                    Toast.makeText(context, "包含未IQC检验物料，不能入库",
                                            Toast.LENGTH_LONG).show();
                                    return;
                                }
                            } else {
                                Toast.makeText(context, "此物料已入库",
                                        Toast.LENGTH_LONG).show();
                                wmsService.RnBoxList = null;
                                return;
                            }*/
                            imgsBox.setIfScanedBox(true);
/*                            if(!rnbox.getRvv32().equals("")){
                                rnbox.setIfScanedBox(true);
                            }else {
                                wmsService.RnBoxList = null;
                                Toast.makeText(context, "此物料未上架入库",
                                        Toast.LENGTH_LONG).show();
                                return;
                            }*/
                        }
                    }
                } else {
                    PopUtil.showPopUtil(msg);
                    wmsService.imgsBoxList = null;
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
                case ActionList.GET_IMGSBOX_BY_BOXNUM_HANDLER_TYPE:
                    getRnBoxListByBoxnum(data);
                    break;
            }
        }
    };
}
