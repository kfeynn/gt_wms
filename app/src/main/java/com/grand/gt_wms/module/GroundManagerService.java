package com.grand.gt_wms.module;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.google.gson.Gson;
import com.grand.gt_wms.bean.Label;
import com.grand.gt_wms.bean.RnBox;
import com.grand.gt_wms.service.wmsBroadcastReceiver;
import com.grand.gt_wms.service.wmsService;
import com.grand.gt_wms.tool.ActionList;
import com.grand.gt_wms.tool.HttpPath;
import com.grand.gt_wms.tool.HttpReq;
import com.grand.gt_wms.tool.PopUtil;
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

public class GroundManagerService implements wmsBroadcastReceiver.HalGroundingCallback {

    private List<BasicNameValuePair> list = null;
    private JSONObject jsonObject;
    private Context context;
    private String qr_data;

    public GroundManagerService(Context context) {
        this.context = context;
        wmsBroadcastReceiver.getInstance().registerHalGroundingCallback(this);
    }

    @Override
    public void onScanBoxnum(Intent intent, String qr_data) {
        this.qr_data = qr_data;
        if (wmsService.RnBoxList == null) {
            list = new ArrayList<BasicNameValuePair>();
            list.add(new BasicNameValuePair("box_num", qr_data));
            getUrlData(list, HttpPath.getPNByBoxnumPath(),
                    ActionList.GET_PN_BY_BOXNUM_HANDLER_TYPE, GroundingActivity.context);
        } else {
            int i = 0;
            for (RnBox rnbox : wmsService.RnBoxList) {
                if (rnbox.getBarcode().equals(qr_data)) {
                    if (!rnbox.getRvv32().equals("")) {
                        rnbox.setRvv32("");
                        rnbox.setRvv33("");
                    }
                    rnbox.setIfScanedBox(true);
                } else {
                    //rnbox.setIfScanedBox(false);
                    i++;
                }
            }
            if (i == wmsService.RnBoxList.size()) {
                list = new ArrayList<BasicNameValuePair>();
                list.add(new BasicNameValuePair("box_num", qr_data));
                getUrlData(list, HttpPath.getPNByBoxnumPath(),
                        ActionList.GET_PN_BY_BOXNUM_HANDLER_TYPE, GroundingActivity.context);
            } else {
                intent = new Intent(ActionList.ACTION_SHOW_BOXLIST);
                intent.putExtra("box_num", qr_data);
                context.sendBroadcast(intent);
            }
        }
    }

    @Override
    public void onScanLabel(Intent intent, String qr_data) {
        int i = 0;
        qr_data = qr_data.substring(2);
        Gson gson = new Gson();
        Label label = gson.fromJson(qr_data,Label.class);
        if(label == null){
            PopUtil.showPopUtil("内标贴格式有误，请检查");
            return;
        }
        if(wmsService.RnBoxList==null){
            PopUtil.showPopUtil("请先扫描外箱标签的二维码");
            return;
        }else {
            for(RnBox rnBox : wmsService.RnBoxList){
                if(rnBox.isIfScanedBox()){
                    i++;
                }
            }
            if(i >= 2){
                for (RnBox rnBox : wmsService.RnBoxList){
                    if(rnBox.isIfScanedBox()){
                        rnBox.setIfScanedBox(false);
                    }
                }
                PopUtil.showPopUtil("只允许关联一个外箱，请重新扫描");
                return;
            }
        }
        int groupPosition = 9999;
        for (RnBox rnBox : wmsService.RnBoxList){
            for(Label label1 : rnBox.getLabels()){
                if(label1.getLabelId().equals(label.getLabelId())){
                    PopUtil.showPopUtil("重复扫描内标贴");
                    return;
                }
            }
        }
        for (RnBox rnBox : wmsService.RnBoxList){
            if(rnBox.getRvb05().equals(label.getRvb05()) && rnBox.isIfScanedBox()){
                rnBox.getLabels().add(label);
                rnBox.setRemaining_label(rnBox.getRemaining_label()-Double.valueOf(label.getQty()));
                groupPosition = wmsService.RnBoxList.indexOf(rnBox);
                rnBox.setExpand(true);
            }else{
                rnBox.setExpand(false);
            }
        }
        intent = new Intent(ActionList.ACTION_SHOW_LABEL);
        intent.putExtra("groupPosition", groupPosition);
        context.sendBroadcast(intent);
    }

    @Override
    public void onScanWaddr(Intent intent, String qr_data) {
        intent = new Intent(ActionList.ACTION_SHOW_BOXWADDR);
        this.qr_data = qr_data;
        int i = 0;
        if (wmsService.RnBoxList != null) {
            for (RnBox rnBoxbox : wmsService.RnBoxList) {
                if (rnBoxbox.isIfScanedBox()) {
                    try {
                        jsonObject = new JSONObject(qr_data);
                        rnBoxbox.setRvv32(jsonObject.getString("rvv32"));
                        rnBoxbox.setRvv33(jsonObject.getString("rvv33"));
                        rnBoxbox.setIfScanedBox(false);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            for (RnBox rnBoxbox1 : wmsService.RnBoxList) {
                if (!rnBoxbox1.getRvv32().equals("")) {
                    i++;
                }
                if (i == wmsService.RnBoxList.size()) {
                    if (!ifSameWaddr(wmsService.RnBoxList)) {
                        PopUtil.showPopUtil("同物料仓储位不同，请检查");
                        intent.putExtra("ifStop", false);
                        break;
                    }
                    intent.putExtra("ifStop", true);
                } else {
                    intent.putExtra("ifStop", false);
                }
            }
            for (RnBox rnBox : wmsService.RnBoxList){
                double qty = 0;
                if(rnBox.getLabels().size()!=0){
                    for (Label label : rnBox.getLabels()){
                        qty = Double.valueOf(label.getQty()) + qty;
                    }
                    if(Double.valueOf(rnBox.getIbb07()) != qty){
                        PopUtil.showPopUtil("物料内外标签数量不一致，请检查");
                        intent.putExtra("ifStop", false);
                    }
                }
            }
            context.sendBroadcast(intent);
            return;
        } else {
            PopUtil.showPopUtil("请先扫描包装票的二维码");
        }
    }

    private boolean ifSameWaddr(List<RnBox> rnboxList) {
        for (int i = 0; i < rnboxList.size() - 1; i++) {
            if (!rnboxList.get(i).getRvv32().equals(rnboxList.get(i + 1).getRvv32()) ||
                    !rnboxList.get(i).getRvv33().equals(rnboxList.get(i + 1).getRvv33())) {
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
                    wmsService.RnBoxList = JSONArray.parseArray(data, RnBox.class);
                    String rvb05_temp = "";
                    for(RnBox rnBox : wmsService.RnBoxList){
                        if(rnBox.getBarcode().equals(qr_data)){
                            if(Double.valueOf(rnBox.getRvb31())<=0){
                                PopUtil.showPopUtil("此物料已入库");
                                wmsService.RnBoxList = null;
                                return;
                            }
                            rvb05_temp = rnBox.getRvb05();
                        }
                    }
                    Iterator<RnBox> it = wmsService.RnBoxList.iterator();
                    //移除不是对应物料
                    while (it.hasNext())
                    {
                        RnBox rnBox = it.next();
                        rnBox.setRemaining_label(Double.valueOf(rnBox.getIbb07()));
                        if (!rnBox.getRvb05().equals(rvb05_temp))
                        {
                            it.remove();
                        }
                    }
                    for (RnBox rnbox : wmsService.RnBoxList) {
                        if (rnbox.getBarcode().equals(qr_data)) {
                            if (rnbox.getRvb39().equals("N") || rnbox.getQcs14().equals("Y")) {
                                rnbox.setIfScanedBox(true);
                            } else {
                                PopUtil.showPopUtil("包含未IQC检验物料，不能入库");
                                wmsService.RnBoxList = null;
                                return;
                            }
                        }
                    }
                } else {
                    PopUtil.showPopUtil(msg);
                    wmsService.RnBoxList = null;
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
                case ActionList.GET_PN_BY_BOXNUM_HANDLER_TYPE:
                    getRnBoxListByBoxnum(data);
                    break;
            }
        }
    };

    @Override
    public void onInputGrounding(Intent intent) {
        for (RnBox rnBox : wmsService.RnBoxList){
            if(rnBox.getRvv32().equals("")||rnBox.getRvv33().equals("")){
                PopUtil.showPopUtil("库位或储位为空，不能强制上架");
                return;
            }
        }
        intent = new Intent(ActionList.ACTION_SHOW_BOXWADDR);
        intent.putExtra("ifStop", true);
        intent.putExtra("ifInput_Grounding", true);
        context.sendBroadcast(intent);
    }
}
