package com.grand.gt_wms.tool;

import android.drm.DrmStore;

/**
 * Created by Administrator on 2018/8/4.
 */

public  class ActionList {

    //public static final String UPDATE_URL = "http://192.168.22.250:8080/BarcodeWeb/update/gt_wms.apk";//测试安装包下载地址
    public static final String UPDATE_URL = "http://192.168.0.63:9009/BarcodeWeb/update/gt_wms.apk";//正式安装包下载地址
    private static final  String ACTION_SERVICE = "com.grand.gt_wms.service.";
    private static final String ACTION_SHOW = "com.grand.gt_wms.show";
    public static final String action_boot ="android.intent.action.BOOT_COMPLETED"; //开机启动广播
    /**
     * Activity发送给Service 广播
     */

    public static final String ACTION_LOGIN = ACTION_SERVICE +"login";
    public static final String ACTION_QUERY_DNNUM = ACTION_SERVICE + "queryDn";
    public static final String ACTION_QUERY_PN_BY_BOXNUM = ACTION_SERVICE + "queryPnByBoxnum";
    public static final String ACTION_INPUT_RECEIVING = ACTION_SERVICE + "inputReceiving";
    public static final String ACTION_QUERY_DNNUM1 = ACTION_SERVICE + "queryDn";   //委外退货
    public static final String ACTION_QUERY_PN_BY_BOXNUM1 = ACTION_SERVICE + "queryPnByBoxnum";   //委外退货
    public static final String ACTION_INPUT_RETURN = ACTION_SERVICE + "inputReturn";   //委外退货
    public static final String ACTION_UPDATE = ACTION_SERVICE + "update";
    public static final String ACTION_QUERY_PKBOX_BY_PKNUM = ACTION_SERVICE + "queryPkboxByPknum";
    public static final String ACTION_INPUT_GROUNDING = ACTION_SERVICE + "inputGrounding";

    /***************************************************************************/

    /**
     * Service发送给Activity 广播
     */

    public static final String ACTION_SHOW_PNSUBS = ACTION_SHOW + "Pnsubs";    //收货单
    public static final String ACTION_SHOW_PNSUBS1 = ACTION_SHOW + "Pnsubs1";   //退货单
    public static final String ACTION_SHOW_LOGIN = ACTION_SHOW + "login";
    public static final String ACTION_SHOW_UPDATE_PNSUBS = ACTION_SHOW + "updatePnSubs";
    public static final String ACTION_SHOW_UPDATE_PNSUBS1 = ACTION_SHOW + "updatePnSubs1";    //退货单列表更新
    public static final String ACTION_SHOW_BOXLIST = ACTION_SHOW + "boxList";
    public static final String ACTION_SHOW_BOXWADDR = ACTION_SHOW +"boxWaddr";
    public static final String ACTION_SHOW_UPDATE = ACTION_SHOW + "update";
    public static final String ACTION_SHOW_LIST = ACTION_SHOW + "list";   //发料、仓退、仓退记录
    public static final String ACTION_SHOW_LIST1 = ACTION_SHOW + "list1";   //厂内调拨
    public static final String ACTION_SHOW_LIST2 = ACTION_SHOW + "list2";   //下阶报废
    public static final String ACTION_SHOW_LIST3 = ACTION_SHOW + "list3";   //组合拆解
    public static final String ACTION_SHOW_LIST4 = ACTION_SHOW + "list4";   //仓退交接扫描
    public static final String ACTION_SHOW_LIST5 = ACTION_SHOW + "list5";   //送货单交接扫描
    public static final String ACTION_SHOW_GoodsIptAndEptItem = ACTION_SHOW + "GoodsIptAndEptItem";
    public static final String ACTION_SHOW_LABEL = ACTION_SHOW + "label";
    //public static final String ACTION_START_SCAN = "start_scan";

    /***************************************************************************/

    /**
     * scan data 接收广播
     */
    public static final String ACTION_QR_DATA_RECEIVED = "ACTION_QR_DATA_RECEIVED";
    public static final String ACTION_QR_DATA_RETURN = "ACTION_QR_DATA_RECEIVED";
    //public static final String ACTION_QR_DATA_RECEIVED = "com.android.receive_scan_action";
    public static final int GET_PN_HANDLER_TYPE = 0;
    public static final int GET_LOGIN_HANDLER_TYPE = 1;
    public static final int GET_PN_BY_BOXNUM_HANDLER_TYPE = 2;
    public static final int Get_UPDATE_HANDLER_TYPE = 3;
    public static final int GET_CONFIRMRECEIVING_HANDLER_TYPE = 4;  //收货扫描提交
    public static final int GET_CHANGEPASSWORD_HANDLER_TYPE = 5;
    public static final int GET_CONFIRMGROUNDING_HANDLER_TYPE = 6;
    public static final int GET_PK_BY_PKNUM_HANDLER_TYPE = 7;
    public static final int GET_CONFIRMPACKING_HANDLER_TYPE = 8;
    public static final int GET_BOX_BY_BOXNUM_HANDLER_TYPE = 9;
    public static final int GET_WOR_BY_WORNUM_HANDLER_TYPE = 10;
    public static final int GET_CONFIRMWORETURN_HANDLER_TYPE = 11;
    public static final int GET_CONFIRMIQC_HANDLER_TYPE = 12;
    public static final int GET_WHR_BY_WHNUM_HANDLER_TYPE = 13;     //仓退数据查询
    public static final int GET_CONFIRMWHRETURN_HANDLER_TYPE = 14;  //仓退审核
    public static final int GET_ITEM_BY_BOX_HANDLER_TYPE = 15;
    public static final int GET_CONFIRMGOODSMOVE_HANDLER_TYPE = 16;
    public static final int GET_IMGSBOX_BY_BOXNUM_HANDLER_TYPE = 17;
    public static final int GET_IQCFORM_BY_BOXNUM_HANDLER_TYPE = 18;
    public static final int GET_AK_BY_AKNUM_HANDLER_TYPE = 19;     //厂内调拨查询
    public static final int GET_CONFIRMALLOT_HANDLER_TYPE = 20;     //厂内调拨过账
    public static final int GET_BK_BY_BKNUM_HANDLER_TYPE = 21;     //下阶报废查询
    public static final int GET_CONFIRMDUMPING_HANDLER_TYPE = 22;     //下阶报废过账
    public static final int GET_CK_BY_CKNUM_HANDLER_TYPE = 23;     //拆解单查询
    public static final int GET_CONFIRMAPART_HANDLER_TYPE = 24;     //拆解单过账
    public static final int GET_WHR_BY_WHNUMA_HANDLER_TYPE = 25;     //交接仓退数据查询
    public static final int GET_CONFIRMJOIN_HANDLER_TYPE = 26;     //仓退交接记录
    public static final int GET_WHR_BY_RNNUM_HANDLER_TYPE = 27;     //送货单对应收货单数据查询
    public static final int GET_CONFIRMJOIN1_HANDLER_TYPE = 28;     //送货单交接记录
    public static final int GET_CONFIRMRETURN_HANDLER_TYPE = 29;  //退货扫描提交

}
