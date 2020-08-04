package com.grand.gt_wms.service;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.Vibrator;
import android.widget.Toast;

import com.grand.gt_wms.tool.ActionList;
import com.grand.gt_wms.tool.PopUtil;
import com.grand.gt_wms.ui.activity.BaseActivity;
import com.grand.gt_wms.ui.activity.LoginActivity;
import com.grand.gt_wms.ui.activity.MainActivity;
import com.grand.gt_wms.ui.activity.ReceivingActivity;

import java.util.Calendar;

/**
 * Created by Administrator on 2018/7/26.
 */

public class wmsBroadcastReceiver extends BroadcastReceiver {
    private Context context;
    private static wmsBroadcastReceiver wmsReceiver;
    public final static int SCAN_STATUS_RECEIVING_DNNUM = 1;
    public final static int SCAN_STATUS_RECEIVING_BOXNUM = 2;
    public final static int SCAN_STATUS_GROUNDING_BOXNUM = 3;
    public final static int SCAN_STATUS_GROUNDING_WADDR = 4;
    public final static int SCAN_STATUS_FALSE = 0;
    public final static int SCAN_STATUS_INPUT_FALSE = 5;
    public final static int SCAN_STATUS_PACKING_PKNUM = 6;
    public final static int SCAN_STATUS_PACKING_BOXNUM = 7;
    public final static int SCAN_STATUS_WORETURN_WORNUM = 8;
    public final static int SCAN_STATUS_WORETURN_BOXNUM = 9;
    public final static int SCAN_STATUS_IQC_BOXNUM = 10;
    public final static int SCAN_STATUS_WHRETURN_WHRNUM = 11;
    public final static int SCAN_STATUS_WHRETURN_BOXNUM = 12;
    public final static int SCAN_STATUS_GDSIPTANDEPT_BOXNUM = 13;
    public final static int SCAN_STATUS_GDSIPTANDEPT_WADDR = 14;
    public final static int SCAN_STATUS_GOODSMOVE_BOXNUM = 15;
    public final static int SCAN_STATUS_GOODSMOVE_WADDR = 16;
    public final static int SCAN_STATUS_GROUNDING_LABEL = 17;
    public final static int SCAN_STATUS_ALLOT_AKNUM = 18;     //调拨查询
    public final static int SCAN_STATUS_ALLOT_BOXNUM = 19;    //厂内调拨扫描条码
    public final static int SCAN_STATUS_DUMPING_BKNUM = 20;     //下阶料查询
    public final static int SCAN_STATUS_APART_BKNUM = 21;     //拆解单查询
    public final static int SCAN_STATUS_WHRETURN1_WHRNUM = 22;    //仓退交接查询
    public final static int SCAN_STATUS_RECEIVING1_BOXNUM = 23;    //送货单交接查询
    public final static int SCAN_STATUS_RETURN_DNNUM = 24;  //退货单常量
    public final static int SCAN_STATUS_RETURN_BOXNUM = 25;       //退货单扫描小箱号常量
    public static int scan_status;
    public static long lastClickTime=0;
    public static final int MIN_CLICK_DELAY_TIME = 500;
    public String qr_data;

    HalLoginCallback loginCallback;
    HalReceivingCallback receivingCallback;
    HalReturnCallback returnCallback;    //退货单接口
    HalGroundingCallback groundingCallback;
    HalPackingCallback packingCallback;
    HalAllotCallback allotCallback;   //调拨接口
    HalDumpingCallback dumpingCallback;   //下阶料报废接口
    HalApartCallback apartCallback;   //拆解单废接口
    HalJoinCallback joinCallback;   //仓退交接接口
    HalJoin1Callback join1Callback;   //送货单交接接口
    HalWoReturnCallback woReturnCallback;
    HalIQCCallback iqcCallback;
    HalWhReturnCallback whReturnCallback;
    HalGoodsIptAndEptCallback goodsIptAndEptCallback;
    HalGoodsMoveCallback goodsMoveCallback;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(!wmsService.ifScan){
            wmsService.sp.play(wmsService.soundPoolMap.get(1), 1, 1, 0, 0, 1);
            Vibrator vibrator = (Vibrator)context.getSystemService(context.VIBRATOR_SERVICE);
            vibrator.vibrate(3000);
            return;
        }
        if (intent.getAction().equals(ActionList.action_boot)) {
            Intent sayHelloIntent=new Intent(context,LoginActivity.class);
            sayHelloIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(sayHelloIntent);
        } else if (action.equals(ActionList.ACTION_LOGIN)) {
            loginCallback.onLogin(intent);
        }else if (action.equals(ActionList.ACTION_QUERY_DNNUM)) {
            receivingCallback.onQueryDnnum(intent);
        } else if (action.equals(ActionList.ACTION_INPUT_RECEIVING)) {//强制收货
            receivingCallback.onInputReceiving(intent);
        }else if (action.equals(ActionList.ACTION_QUERY_DNNUM1)) {  //强制退货
            returnCallback.onQueryDnnum(intent);
        } else if (action.equals(ActionList.ACTION_INPUT_RETURN)) {   //强制退货
            returnCallback.onInputReturn(intent);
        } else if (action.equals(ActionList.ACTION_INPUT_GROUNDING)) {//强制上架
            groundingCallback.onInputGrounding(intent);
        } else if (action.equals(ActionList.ACTION_QR_DATA_RECEIVED)) {
            long currentTime = Calendar.getInstance().getTimeInMillis();
            if (currentTime - lastClickTime < MIN_CLICK_DELAY_TIME) {
                lastClickTime = currentTime;
                PopUtil.showPopUtil("两次扫描时间过短，请间隔0.5秒");
                return;
            }else {
                lastClickTime = currentTime;
            }

            qr_data = intent.getStringExtra("qr_data");
            getScanStatus(qr_data);
            if (!qr_data.equals("") && qr_data.substring(qr_data.length() - 1).equals("\r")) {
                //if (!qr_data.equals("")) {
                qr_data = qr_data.replaceAll("\r", "");
                switch (scan_status) {
                    /**********************************收货模块***************************************/
                    case SCAN_STATUS_RECEIVING_DNNUM://收货扫描送货单
                        receivingCallback.onScanDnnum(qr_data);
                        break;
                    case SCAN_STATUS_RECEIVING_BOXNUM://收货扫描箱号
                        receivingCallback.onScanBoxnum(intent,qr_data);
                        break;
                    /**********************************委外退货模块***************************************/
                    case SCAN_STATUS_RETURN_DNNUM://退货扫描送货单
                        returnCallback.onScanDnnum(qr_data);
                        break;
                    case SCAN_STATUS_RETURN_BOXNUM://退货扫描箱号
                        returnCallback.onScanBoxnum(intent,qr_data);
                        break;

                    /**********************************IQC模块***************************************/
                    case SCAN_STATUS_IQC_BOXNUM://IQC扫描箱号
                        iqcCallback.onScanBoxnum(intent,qr_data);
                        break;

                    /**********************************上架模块***************************************/
                    case SCAN_STATUS_GROUNDING_BOXNUM://上架扫描箱号
                        groundingCallback.onScanBoxnum(intent,qr_data);
                        break;
                    case SCAN_STATUS_GROUNDING_LABEL://上架扫描内标签
                        groundingCallback.onScanLabel(intent,qr_data);
                        break;
                    case SCAN_STATUS_GROUNDING_WADDR://上架扫描库位
                        if(wmsService.RnBoxList != null){
                            groundingCallback.onScanWaddr(intent,qr_data);
                        }else {
                            PopUtil.showPopUtil("请先扫描包装票二维码");
                        }
                        break;
                    /**********************************发料模块***************************************/
                    case SCAN_STATUS_PACKING_PKNUM://发料扫描发料单号
                        packingCallback.onQueryPkNum(intent,qr_data);
                        break;
                    case SCAN_STATUS_PACKING_BOXNUM://发料扫描箱号
                        packingCallback.onScanBoxNum(qr_data);
                        break;
                    /**********************************调拨模块2-zouhs191119***************************************/
                    case SCAN_STATUS_ALLOT_AKNUM://调拨扫描调拨单
                        allotCallback.onQueryAkNum(intent,qr_data);
                        break;
                    case SCAN_STATUS_ALLOT_BOXNUM://调拨扫描箱号
                        allotCallback.onScanBoxNum(qr_data);
                        break;
                    /**********************************下阶料报废模块-zouhs191213***************************************/
                    case SCAN_STATUS_DUMPING_BKNUM:  //下阶扫描拆解单
                        dumpingCallback.onQueryBkNum(intent,qr_data);
                        break;
                    /**********************************组合拆解模块-zouhs200310***************************************/
                    case SCAN_STATUS_APART_BKNUM:  //组合拆解
                        apartCallback.onQueryCkNum(intent,qr_data);
                        break;
                    case SCAN_STATUS_WHRETURN1_WHRNUM://交接扫描仓库退料单号
                        joinCallback.onQueryWhReturnNum1(intent,qr_data);
                        break;
                    case SCAN_STATUS_RECEIVING1_BOXNUM://送货单交接扫描送货单号
                        join1Callback.onQueryDnnum1(intent,qr_data);
                        break;
                    /**********************************工单退料模块***************************************/
                    case SCAN_STATUS_WORETURN_WORNUM://扫描工单退料单号
                        woReturnCallback.onQueryWoReturnNum(intent,qr_data);
                        break;
                    case SCAN_STATUS_WORETURN_BOXNUM://退料扫描箱号
                        woReturnCallback.onScanBoxNum(qr_data);
                        break;
                    /**********************************仓退验货模块***************************************/
                    case SCAN_STATUS_WHRETURN_WHRNUM://扫描仓库退料单号
                        whReturnCallback.onQueryWhReturnNum(intent,qr_data);
                        break;
                    case SCAN_STATUS_WHRETURN_BOXNUM://仓退扫描箱号
                        whReturnCallback.onScanBoxNum(qr_data);
                        break;
                    /**********************************物料进出追踪模块***************************************/
                    case SCAN_STATUS_GDSIPTANDEPT_BOXNUM://物料进出扫描箱条码
                        if(wmsService.START_TIME.equals("") || wmsService.END_TIME.equals("")){
                            PopUtil.showPopUtil("请输入开始/结束时间");
                            return;
                        }
                        goodsIptAndEptCallback.onQueryBoxNum(intent,qr_data);
                        break;
                    /**********************************物料调拨模块***************************************/
                    case SCAN_STATUS_GOODSMOVE_BOXNUM://调拨扫描箱号
                        goodsMoveCallback.onScanBoxnum(intent,qr_data);
                        break;
                    case SCAN_STATUS_GOODSMOVE_WADDR://调拨扫描库位
                        if(wmsService.imgsBoxList != null){
                            goodsMoveCallback.onScanWaddr(intent,qr_data);
                        }else {
                            PopUtil.showPopUtil("请先扫描包装票二维码");
                        }
                        break;
                    case SCAN_STATUS_FALSE:
                        PopUtil.showPopUtil("请打开相应页面再扫码");
                        break;
                    case SCAN_STATUS_INPUT_FALSE:
                        PopUtil.showPopUtil("请输入正确的条码");
                        break;
                    default:
                        PopUtil.showPopUtil("请扫描正确的二维码");
                        break;
                }
            }
        }
    }

    public static wmsBroadcastReceiver getInstance() {
        if (wmsReceiver != null) {
            return wmsReceiver;
        }
        wmsReceiver = new wmsBroadcastReceiver();
        return wmsReceiver;
    }

    public void registerReceiver(Context txt) {
        context = txt.getApplicationContext();
        IntentFilter inFilter = new IntentFilter();
        inFilter.addAction(ActionList.ACTION_LOGIN);
        inFilter.addAction(ActionList.ACTION_QUERY_DNNUM);
        inFilter.addAction(ActionList.ACTION_QUERY_DNNUM1);  //退货扫描
        inFilter.addAction(ActionList.ACTION_QR_DATA_RECEIVED);
        inFilter.addAction(ActionList.ACTION_QR_DATA_RETURN);    //退货扫描
        inFilter.addAction(ActionList.ACTION_QUERY_PN_BY_BOXNUM);
        inFilter.addAction(ActionList.ACTION_QUERY_PN_BY_BOXNUM1);   //退货扫描
        inFilter.addAction(ActionList.ACTION_INPUT_RECEIVING);
        inFilter.addAction(ActionList.ACTION_UPDATE);
        inFilter.addAction(ActionList.ACTION_INPUT_GROUNDING);
        context.registerReceiver(this, inFilter);
    }

    public static interface HalLoginCallback{
        public void onLogin(Intent intent);
    }

    public static interface HalReceivingCallback {
        public void onQueryDnnum(Intent intent);
        public void onInputReceiving(Intent intent);
        public void onScanDnnum(String qr_data);
        public void onScanBoxnum(Intent intent,String qr_data);
    }
    //定义退货接口
    public static interface HalReturnCallback {
        public void onQueryDnnum(Intent intent);
        public void onInputReturn(Intent intent);
        public void onScanDnnum(String qr_data);
        public void onScanBoxnum(Intent intent,String qr_data);
    }
    public static interface HalIQCCallback{
        public void onScanBoxnum(Intent intent,String qr_data);
    }

    public static interface HalGroundingCallback{
        public void onScanBoxnum(Intent intent,String qr_data);
        public void onScanWaddr(Intent intent,String qr_data);
        public void onScanLabel(Intent intent,String qr_data);
        public void onInputGrounding(Intent intent);
    }

    public static interface HalPackingCallback{
        public void onQueryPkNum(Intent intent, String qr_data);
        public void onScanBoxNum(String qr_data);
    }
    //调拨过账：定义方法扫单号跟扫条码，将在后面具体的子类中实现
    public static interface HalAllotCallback{
        public void onQueryAkNum(Intent intent, String qr_data);
        public void onScanBoxNum(String qr_data);
    }
    //下阶料报废过账：定义方法扫单号跟扫条码，将在后面具体的子类中实现
    public static interface HalDumpingCallback{
        public void onQueryBkNum(Intent intent, String qr_data);
        public void onScanBoxNum(String qr_data);
    }
    //拆解单数据查询及过账：定义方法扫描拆解单，将在后面具体的子类中实现
    public static interface HalApartCallback{
        public void onQueryCkNum(Intent intent, String qr_data);
        public void onScanBoxNum(String qr_data);
    }
    //仓退单数据查询及交接：定义方法扫描仓退单，将在后面具体的子类中实现
    public static interface HalJoinCallback{
        public void onQueryWhReturnNum1(Intent intent, String qr_data);
        public void onScanBoxNum(String qr_data);
    }
    //收货单数据查询及交接：定义方法扫描仓退单，将在后面具体的子类中实现
    public static interface HalJoin1Callback{
        public void onQueryDnnum1(Intent intent, String qr_data);
        public void onScanBoxNum(String qr_data);
    }
    public static interface HalWoReturnCallback{
        public void onQueryWoReturnNum(Intent intent, String qr_data);
        public void onScanBoxNum(String qr_data);
    }

    public static interface HalWhReturnCallback{
        public void onQueryWhReturnNum(Intent intent, String qr_data);
        public void onScanBoxNum(String qr_data);
    }

    public static interface HalGoodsIptAndEptCallback{
        public void onQueryBoxNum(Intent intent, String qr_data);
    }

    public static interface HalGoodsMoveCallback{
        public void onScanBoxnum(Intent intent,String qr_data);
        public void onScanWaddr(Intent intent,String qr_data);
    }
   //广播注册
    public void registerHalLoginCallback(HalLoginCallback callback) {
        loginCallback = callback;
    }
    public void unregisterHalLoginCallback() {
        loginCallback = null;
    }
    public void registerHalReceivingCallback(HalReceivingCallback callback) {receivingCallback = callback;}
    public void unregisterHalReceivingCallback() {
        receivingCallback = null;
    }
    public void registerHalReturnCallback(HalReturnCallback callback) {returnCallback = callback;} //退货单
    public void unregisterHalReturnCallback() {
        returnCallback = null;
    }
    public void registerHalGroundingCallback(HalGroundingCallback callback) {groundingCallback = callback;}
    public void unregisterHalGroundingCallback() {
        groundingCallback = null;
    }
    public void registerHalPackingCallback(HalPackingCallback callback) {packingCallback = callback;}
    public void unregisterHalPackingCallback() {
        packingCallback = null;
    }
    public void registerHalWoReturnCallback(HalWoReturnCallback callback) {woReturnCallback = callback;}
    public void unregisterHalWoReturnCallback() {
        woReturnCallback = null;
    }
    public void registerHalIQCCallback(HalIQCCallback callback) {
        iqcCallback = callback;
    }
    public void unregisterHalIQCCallback() {
        iqcCallback = null;
    }
    public void registerHalWhReturnCallback(HalWhReturnCallback callback) {whReturnCallback = callback;}
    public void unregisterHalWhReturnCallback() {
        whReturnCallback = null;
    }
    public void registerHalGoodsIptAndEptCallback(HalGoodsIptAndEptCallback callback) {goodsIptAndEptCallback = callback;}
    public void unregisterHalGoodsIptAndEptCallback() {
        goodsIptAndEptCallback = null;
    }
    public void registerHalGoodsMoveCallback(HalGoodsMoveCallback callback) {goodsMoveCallback = callback;}
    public void unregisterHalGoodsMoveCallback() {
        goodsMoveCallback = null;
    }
    public void registerHalAllotCallback(HalAllotCallback callback) {allotCallback = callback;}  //调拨注册广播
    public void unregisterHalAllotCallback() {                             //调拨取消广播注册
        allotCallback = null;
    }
    public void registerHalDumpingCallback(HalDumpingCallback callback) {dumpingCallback = callback;}  //下阶料报废注册广播
    public void unregisterHalDumpingCallback() {                             //调拨取消广播注册
        dumpingCallback = null;
    }
    public void registerHalApartCallback(HalApartCallback callback) {apartCallback = callback;}  //拆解单注册广播
    public void unregisterHalApartCallback() {                             //调拨取消广播注册
        apartCallback = null;
    }                //拆解单广播注销
    public void registerHalJoinCallback(HalJoinCallback callback) {joinCallback = callback;}  //仓退交接注册广播
    public void unregisterHalJoinCallback() {                            //仓退交接广播注销
        joinCallback = null;
    }
    public void registerHalJoin1Callback(HalJoin1Callback callback) {join1Callback = callback;}  //送货单交接注册广播
    public void unregisterHalJoin1Callback() {                            //仓退交接广播注销
        join1Callback = null;
    }
    private void getScanStatus(String qr_data){
        //qr_data = intent.getStringExtra("data");
        if(qr_data.length()<=8){
            scan_status = SCAN_STATUS_INPUT_FALSE;
            return;
        }
        String markString = qr_data.substring(0, 2);
        String activityName = getRunningActivityName();
        if (markString.equals("GD")) {
            if (activityName.equals("ReceivingActivity")) {         //送货单收货
                scan_status = SCAN_STATUS_RECEIVING_DNNUM;
            } else if (activityName.equals("Join1Activity")){       //送货单交接
                scan_status = SCAN_STATUS_RECEIVING1_BOXNUM;
            } else if (activityName.equals("ReturnActivity")){       //退货单交接
                scan_status = SCAN_STATUS_RETURN_DNNUM;
            } else {
                scan_status = SCAN_STATUS_FALSE;
            }
        } else if (markString.equals("GB")) {   //小跳吗
            if (activityName.equals("ReceivingActivity")) {
                scan_status = SCAN_STATUS_RECEIVING_BOXNUM;
            } else if (activityName.equals("ReturnActivity")) {  //退货扫描小箱号
                scan_status = SCAN_STATUS_RETURN_BOXNUM;
            } else if (activityName.equals("GroundingActivity")) {
                scan_status = SCAN_STATUS_GROUNDING_BOXNUM;
            } else if (activityName.equals("PackingActivity")) {
                scan_status = SCAN_STATUS_PACKING_BOXNUM;
            } else if (activityName.equals("AllotActivity")) {    //厂内调拨条码扫描
                scan_status = SCAN_STATUS_ALLOT_BOXNUM;
            } else if (activityName.equals("WOReturnActivity")) {
                scan_status = SCAN_STATUS_WORETURN_BOXNUM;
            } else if (activityName.equals("IQCActivity")) {
                scan_status = SCAN_STATUS_IQC_BOXNUM;
            } else if (activityName.equals("WHReturnActivity")) {
                scan_status = SCAN_STATUS_WHRETURN_BOXNUM;
            } else if (activityName.equals("GoodsIptAndEptActivity")) {
                scan_status = SCAN_STATUS_GDSIPTANDEPT_BOXNUM;
            } else if (activityName.equals("GoodsMoveActivity")) {
                scan_status = SCAN_STATUS_GOODSMOVE_BOXNUM;
            } else {
                scan_status = SCAN_STATUS_FALSE;
            }
        } else if (markString.equals("33")) {
            if (activityName.equals("PackingActivity")) {
                scan_status = SCAN_STATUS_PACKING_PKNUM;
            } else {
                scan_status = SCAN_STATUS_FALSE;
            }
        } else if (markString.equals("34")) {
            if (activityName.equals("WOReturnActivity")) {
                scan_status = SCAN_STATUS_WORETURN_WORNUM;
            } else {
                scan_status = SCAN_STATUS_FALSE;
            }
        } else if (markString.equals("11")) {
            if (activityName.equals("PackingActivity")) {
                scan_status = SCAN_STATUS_PACKING_PKNUM;
            } else {
                scan_status = SCAN_STATUS_FALSE;
            }
        } else if (markString.equals("24")) {
            if (activityName.equals("WHReturnActivity")) {
                scan_status = SCAN_STATUS_WHRETURN_WHRNUM;
            }else if(activityName.equals("JoinActivity")){     //仓退交接
                scan_status = SCAN_STATUS_WHRETURN1_WHRNUM;
            } else {
                scan_status = SCAN_STATUS_FALSE;
            }
        }else if (markString.equals("LB")) {
            if (activityName.equals("GroundingActivity")) {
                scan_status = SCAN_STATUS_GROUNDING_LABEL;
            } else {
                scan_status = SCAN_STATUS_FALSE;
            }
        }else if (markString.equals("14")) {              //厂内调拨
            if (activityName.equals("AllotActivity")) {
                scan_status = SCAN_STATUS_ALLOT_AKNUM;
            } else {
                scan_status = SCAN_STATUS_FALSE;
            }
        }else if (markString.equals("3D")) {              //下阶报废
            if (activityName.equals("DumpingActivity")) {
                scan_status = SCAN_STATUS_DUMPING_BKNUM;
            } else {
                scan_status = SCAN_STATUS_FALSE;
            }
        }else if (markString.equals("38")) {              //组合拆解单
            if (activityName.equals("ApartActivity")) {
                scan_status = SCAN_STATUS_APART_BKNUM;
            } else {
                scan_status = SCAN_STATUS_FALSE;
            }
        } else{
            markString = qr_data.substring(2, 7);
            if (markString.equals("rvv32")) {
                if (activityName.equals("GroundingActivity")) {
                    scan_status = SCAN_STATUS_GROUNDING_WADDR;
                }else if(activityName.equals("GoodsIptAndEptActivity")){
                    scan_status = SCAN_STATUS_GDSIPTANDEPT_WADDR;
                } else if (activityName.equals("GoodsMoveActivity")) {
                    scan_status = SCAN_STATUS_GOODSMOVE_WADDR;
                }
            }else if(activityName.equals("GoodsIptAndEptActivity")){
                scan_status = SCAN_STATUS_GDSIPTANDEPT_BOXNUM;
            }
        }

    }

    private String getRunningActivityName() {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        String runningActivity = activityManager.getRunningTasks(1).get(0).topActivity.getClassName();
        runningActivity = runningActivity.replace("com.grand.gt_wms.ui.activity.", "");
        return runningActivity;
    }

}
