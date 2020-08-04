package com.grand.gt_wms.module;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.google.gson.Gson;
import com.grand.gt_wms.bean.Box;
import com.grand.gt_wms.bean.PN;
import com.grand.gt_wms.bean.PNSub;
import com.grand.gt_wms.service.wmsBroadcastReceiver;
import com.grand.gt_wms.service.wmsService;
import com.grand.gt_wms.tool.ActionList;
import com.grand.gt_wms.tool.HttpPath;
import com.grand.gt_wms.tool.HttpReq;
import com.grand.gt_wms.tool.PopUtil;
import com.grand.gt_wms.ui.activity.ReceivingActivity;
import com.grand.gt_wms.ui.activity.ReturnActivity;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/10/15.退货扫描接口逻辑（委外退货）
 */

public class ReturnManagerService implements wmsBroadcastReceiver.HalReturnCallback {
    private List<BasicNameValuePair> list = null;
    private JSONObject jsonObject;
    private Context context;
    private String qr_data;

    public ReturnManagerService(Context context) {
        this.context = context;
        wmsBroadcastReceiver.getInstance().registerHalReturnCallback(this);
    }

    @Override
    public void onQueryDnnum(Intent intent) {
        String dn_num = intent.getStringExtra("dn_num");
        list = new ArrayList<BasicNameValuePair>();
        list.add(new BasicNameValuePair("dn_num", dn_num));
        getUrlData(list, HttpPath.getDnMaterialList1Path(), ActionList.GET_PN_HANDLER_TYPE, ReturnActivity.context);
    }

    @Override
    public void onInputReturn(Intent intent) {
        intent = new Intent(ActionList.ACTION_SHOW_UPDATE_PNSUBS1);
        boolean flag = true;
        intent.putExtra("ifInput", true);
        if (wmsService.pn == null) {
            PopUtil.showPopUtil("您要退货的物料为空，请检查");
            return;
        }
        double inputWeight = 0;
        for (PNSub pnSub : wmsService.pn.getPnsubs()) {
            if (pnSub.getPmn86().equals("kg") && (pnSub.getBoxs().size() > 0) && pnSub.isIfChangeWeight()) {
                flag = false;
                for (PNSub pnSub1 : wmsService.pn.getPnsubs()) {
                    if (pnSub.getPmn04().equals(pnSub1.getPmn04()) && pnSub.getBoxs().size() > 0) {
                        inputWeight = inputWeight + ((pnSub1.getPmn20_copy() - pnSub1.getPmn20()) / pnSub1.getPmn20_copy()) * pnSub1.getPmn87();
                    }
                }
                intent.putExtra("inputWeight", inputWeight);
                intent.putExtra("pmn04", pnSub.getPmn04());
                context.sendBroadcast(intent);
                return;
            }

        }
        if (flag) {
            intent.putExtra("ifStop", true);
            context.sendBroadcast(intent);
        }
    }

    @Override
    public void onScanDnnum(String qr_data) {
        wmsService.scanedBox.clear();
        this.qr_data = qr_data;
        list = new ArrayList<BasicNameValuePair>();
        list.add(new BasicNameValuePair("dn_num", qr_data));
        getUrlData(list, HttpPath.getDnMaterialList1Path(), ActionList.GET_PN_HANDLER_TYPE, ReturnActivity.context);
    }

/*    @Override
    public void onScanBoxnum(Intent intent,String qr_data) {
        boolean flag = true;
        intent = new Intent(ActionList.ACTION_SHOW_UPDATE_PNSUBS);
        for (Box boxs : wmsService.scanedBox) {
            if (boxs.getBoxnum().equals(qr_data)) {
                Toast.makeText(context, "此箱已扫描，请重新扫描",
                        Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if (wmsService.pn == null) {
            Toast.makeText(context, "请先扫描送货单",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        for (PNSub pnSub : wmsService.pn.getPnsubs()) {
            for (Box box : pnSub.getCopy_boxs()) {
                if (box.getBoxnum().equals(qr_data)) {
                    flag = false;
                    wmsService.scanedBox.add(box);
                    for (Box box1 : pnSub.getBoxs()) {
                        if (box1.getBoxnum().equals(qr_data)) {
                            box1.setIfScanedBox(true);
                            pnSub.setReal_pmn87(pnSub.getReal_pmn87()+box.getPmn20());
                        }
                    }
                    pnSub.setPmn20_copy(pnSub.getPmn20_copy() - box.getPmn20());
                    pnSub.getCopy_boxs().remove(box);
                    if (pnSub.getPmn86().equals("kg") && pnSub.getCopy_boxs().size() == 0) {
                        intent.putExtra("inputWeight", pnSub.getPmn87());
                        intent.putExtra("pmn04", pnSub.getPmn04());
                        pnSub.setIfChangeWeight(true);
                    } else {
                        intent.putExtra("inputWeight", 0.0);
                    }
                    if (wmsService.scanedBox.size() == wmsService.pn.getBoxs().size()) {
                        intent.putExtra("ifStop", true);
                    } else {
                        intent.putExtra("ifStop", false);
                    }
                    intent.putExtra("ifInput", false);
                    context.sendBroadcast(intent);
                    return;
                }

            }
        }
        if (flag) {
            Toast.makeText(context, "此箱不属于这张送货单，请重新扫描对应箱号",
                    Toast.LENGTH_SHORT).show();
        }
    }*/

    @Override
    public void onScanBoxnum(Intent intent, String qr_data) {
        if (wmsService.pn != null) {
            for (Box box : wmsService.scanedBox) {
                if (qr_data.equals(box.getBoxnum())) {
                    PopUtil.showPopUtil("此标贴已扫描，请务重复扫描");
                    return;
                }
            }
            list = new ArrayList<BasicNameValuePair>();
            list.add(new BasicNameValuePair("boxnum", qr_data));
            getUrlData(list, HttpPath.getBoxByBoxnum1Path(), ActionList.GET_BOX_BY_BOXNUM_HANDLER_TYPE, ReceivingActivity.context);
        } else {
            PopUtil.showPopUtil("请先扫描退货单的二维码");
        }

    }

    public void getPnBean(String data) {
        if (data.equals("获取失败")) {
            wmsService.pn = null;
            PopUtil.showPopUtil("退货单号错误或者网络连接失败，请重新检查");
        } else {
            try {
                jsonObject = new JSONObject(data);
                String msg = jsonObject.getString("message");
                String data1 = jsonObject.getString("data");
                Gson gson = new Gson();
                if (msg.equals("success")) {
                    wmsService.pn = gson.fromJson(data1, PN.class);
                    for(PNSub pnSub : wmsService.pn.getPnsubs()){
                        pnSub.setPmn20_copy(pnSub.getPmn20());
                    }
                    //if(wmsService.pn.getReceiving_status()!=0){
                    if (false) {
                        //送货单已收货
                        PopUtil.showPopUtil("此退货单已退货");
                        wmsService.pn = null;
                    }
                    Intent intent = new Intent(ActionList.ACTION_SHOW_PNSUBS1);
                    intent.putExtra("dnnum", qr_data);
                    context.sendBroadcast(intent);

                } else {
                    PopUtil.showPopUtil(msg);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void getBoxBean(String data) {
        Intent intent = new Intent(ActionList.ACTION_SHOW_UPDATE_PNSUBS1);
        if (data.equals("获取失败")) {
            wmsService.pn = null;
            PopUtil.showPopUtil("退货单号错误或者网络连接失败，请重新检查");
        } else {
            try {
                jsonObject = new JSONObject(data);
                String msg = jsonObject.getString("message");
                String data1 = jsonObject.getString("data");
                Gson gson = new Gson();
                if (msg.equals("success")) {
                    Box box = gson.fromJson(data1, Box.class);
                    double boxPmn20 = box.getPmn20();
                    double inputWeight = 0;
                    double i = 0;
                    boolean flag1 = true;
                    for (PNSub pnSub : wmsService.pn.getPnsubs()){
                        if(box.getPmn04().equals(pnSub.getPmn04())){
                            i = i + pnSub.getPmn20();
                            flag1 = false;//判断物料是否属于这张送货单
                        }
                    }
                    if(flag1){
                        PopUtil.showPopUtil("物料不属于此退货单，请检查并重新扫描标签");
                        return;
                    }
                    if(boxPmn20 > i){
                        PopUtil.showPopUtil("标贴条码数量超出退货数量，请检查并重新扫描标签");
                        return;
                    }
                    for1:
                    for (PNSub pnSub : wmsService.pn.getPnsubs()) {
                        if (box.getPmn04().equals(pnSub.getPmn04()) && pnSub.getPmn20()!=0 ) {
                            double pnsubPmn20 = pnSub.getPmn20();
                            if (pnSub.getPmn86().equals("kg")) {
                                pnSub.setIfChangeWeight(true);
                            }
                            if (box.getPmn20() >= pnSub.getPmn20()) {
                                box.setPmn20(pnSub.getPmn20());
                                Box box1 = (Box) box.clone();
                                pnSub.boxs.add(box1);
                                pnSub.setReal_pmn87(pnSub.getReal_pmn87() + box1.getPmn20());
                                boxPmn20 = boxPmn20 - pnSub.getPmn20();
                                box.setPmn20(boxPmn20);
                                pnSub.setPmn20(0);

                            } else if (box.getPmn20() < pnSub.getPmn20()) {
                                pnSub.setPmn20(pnSub.getPmn20() - box.getPmn20());
                                Box box1 = (Box) box.clone();
                                pnSub.boxs.add(box1);
                                pnSub.setReal_pmn87(pnSub.getReal_pmn87() + box1.getPmn20());
                                box.setPmn20(0);
                            }
                            if (box.getPmn20() == 0) {
                                boolean flag = true;
                                if (pnSub.getPmn86().equals("kg")) {
                                    for2:
                                    for (PNSub pnSub1 : wmsService.pn.getPnsubs()) {
                                        if (pnSub.getPmn04().equals(pnSub1.getPmn04())) {
                                            if (pnSub1.getPmn20() != 0) {
                                                flag = false;
                                                break for2;
                                            } else {
                                                inputWeight = inputWeight + pnSub1.getPmn87();
                                            }
                                        }
                                    }
                                    if (flag) {
                                        intent.putExtra("inputWeight", inputWeight);
                                        intent.putExtra("pmn04", pnSub.getPmn04());
                                    } else {
                                        intent.putExtra("inputWeight", 0.0);
                                    }
                                }
                                break for1;
                            }
                        }
                    }
                    if (box.getPmn20() != 0) {
                        PopUtil.showPopUtil("标贴条码数量超出退货数量，请检查并重新扫描标签");
                        wmsService.pn = null;
                        Intent intent1 = new Intent(ActionList.ACTION_SHOW_PNSUBS1);
                        intent1.putExtra("dnnum", "");
                        context.sendBroadcast(intent);
                        return;
                    }
                    wmsService.scanedBox.add(box);
                    boolean ifStopFlag = true;
                    for (PNSub pnSub : wmsService.pn.getPnsubs()) {
                        if (pnSub.getPmn20() != 0) {
                            ifStopFlag = false;
                            break;
                        }
                    }
                    if (ifStopFlag) {
                        intent.putExtra("ifStop", true);
                    } else {
                        intent.putExtra("ifStop", false);
                    }
                    intent.putExtra("ifInput", false);
                    context.sendBroadcast(intent);
                    return;
                } else {
                    PopUtil.showPopUtil(msg);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (CloneNotSupportedException e) {
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
                case ActionList.GET_PN_HANDLER_TYPE:
                    System.out.println(data);
                    getPnBean(data);
                    break;
                case ActionList.GET_BOX_BY_BOXNUM_HANDLER_TYPE:
                    System.out.println(data);
                    getBoxBean(data);
            }
        }
    };
}
