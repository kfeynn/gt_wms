package com.grand.gt_wms.ui.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.grand.gt_wms.service.wmsService;
import com.grand.gt_wms.tool.ActionList;
import com.grand.gt_wms.tool.PopUtil;
import com.grand.gt_wms.ui.dialog.EditTextDialog;
import com.grand.gt_wms.ui.dialog.MyDialog;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2018/7/26.
 */

public abstract class parentActivity extends Activity {
    public static  Context context;
    private JSONObject jsonObject;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ActionList.ACTION_SHOW_LOGIN);
        intentFilter.addAction(ActionList.ACTION_SHOW_PNSUBS);
        intentFilter.addAction(ActionList.ACTION_SHOW_UPDATE_PNSUBS);
        intentFilter.addAction(ActionList.ACTION_SHOW_BOXLIST);
        intentFilter.addAction(ActionList.ACTION_SHOW_BOXWADDR);
        intentFilter.addAction(ActionList.ACTION_SHOW_UPDATE);
        intentFilter.addAction(ActionList.ACTION_SHOW_LIST);
        intentFilter.addAction(ActionList.ACTION_SHOW_LIST1);   //厂内调拨
        intentFilter.addAction(ActionList.ACTION_SHOW_LIST2);   //下阶报废
        intentFilter.addAction(ActionList.ACTION_SHOW_LIST3);   //组合拆解
        intentFilter.addAction(ActionList.ACTION_SHOW_LIST4);   //仓退交接
        intentFilter.addAction(ActionList.ACTION_SHOW_LIST5);   //送货单交接
        intentFilter.addAction(ActionList.ACTION_SHOW_PNSUBS1);  //退货单扫描请求
        intentFilter.addAction(ActionList.ACTION_SHOW_UPDATE_PNSUBS1);  //退货单列表更新
        intentFilter.addAction(ActionList.ACTION_SHOW_GoodsIptAndEptItem);
        intentFilter.addAction((ActionList.ACTION_SHOW_LABEL));
        registerReceiver(receiver, intentFilter);
    }

    protected void login(boolean ifLogin) {}
    protected void showPnsubs(String dunum) {}
    protected void showPnsubs1(String dunum) {}   //退货单查询
    protected void showBoxList(String boxNum) {}  //收货退货子表数据
    protected void showBoxWaddr(boolean ifStop,boolean ifInput_Grounding) {}
    protected void showBoxWaddr(boolean ifStop) {}
    protected void showList(boolean ifAuto, String title) {}
    protected void confirmPacking(){};
    protected void showList1(boolean ifAuto, String title) {}      //厂内数据显示
    protected void confirmAllot(){};        //厂内过账方法
    protected void showList2(boolean ifAuto, String title) {}      //下阶报废数据显示
    protected void confirmDumpimg(){};        //厂内过账方法
    protected void showList3(boolean ifAuto, String title) {}      //组合拆解数据显示
    protected void confirmApart(){};        //组合拆解过账方法
    protected void showList4(boolean ifAuto, String title) {}      //仓退交接数据显示
    protected void confirmJoin(){};        //仓退单交接
    protected void showList5(boolean ifAuto, String title) {}      //送货单交接数据显示
    protected void confirmJoin1(){};        //送货单交接
    protected void showItems1(String tse01){};       //显示单头信息
    protected void updatePnsubs(double Weight, boolean ifStop, String pmn04,boolean ifInput) {};
    protected void updatePnsubs1(double Weight, boolean ifStop, String pmn04,boolean ifInput) {};   //退货更新方法
    protected void confirmReceiving(String hintString) {};
    protected void confirmReturn(String hintString) {};   //退货单提交
    protected void getVersion(String content) {};
    protected void confirmGrounding(){};
    protected void confirmWoReturn(){};
    protected void confirmWhReturn(){};
    protected void confirmIQC(){};
    protected void confirmIQCAmount(String et_value,int rnBoxID){};
    protected void showItems(String boxNum){};       //显示单头信息
    protected void confirmGoodsMove(){};
    protected void showLabel(int groupPosition){};

    BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            System.out.println("parentActivity receiver");
            String action = intent.getAction();
            if (action.equals(ActionList.ACTION_SHOW_LOGIN)) {
                boolean ifLogin = intent.getBooleanExtra("ifLogin", false);
                login(ifLogin);
            } else if (action.equals(ActionList.ACTION_SHOW_PNSUBS)) {
                String dnnum = intent.getStringExtra("dnnum");
                showPnsubs(dnnum);
            } else if (action.equals(ActionList.ACTION_SHOW_PNSUBS1)) {   //退货单显示
                String dnnum = intent.getStringExtra("dnnum");
                showPnsubs1(dnnum);
            } else if (action.equals(ActionList.ACTION_SHOW_UPDATE_PNSUBS)) {
                boolean ifStop = intent.getBooleanExtra("ifStop", false);
                double inputWeight = intent.getDoubleExtra("inputWeight",0.00);
                String pmn04 = intent.getStringExtra("pmn04");
                boolean ifInput = intent.getBooleanExtra("ifInput", false);
                updatePnsubs(inputWeight, ifStop,pmn04, ifInput);
            } else if (action.equals(ActionList.ACTION_SHOW_UPDATE_PNSUBS1)) {  //退货单显示更新
                boolean ifStop = intent.getBooleanExtra("ifStop", false);
                double inputWeight = intent.getDoubleExtra("inputWeight",0.00);
                String pmn04 = intent.getStringExtra("pmn04");
                boolean ifInput = intent.getBooleanExtra("ifInput", false);
                updatePnsubs1(inputWeight, ifStop,pmn04, ifInput);
            } else if (action.equals(ActionList.ACTION_SHOW_BOXLIST)) {
                String boxNum = intent.getStringExtra("box_num");
                showBoxList(boxNum);
            } else if (action.equals(ActionList.ACTION_SHOW_BOXWADDR)) {
                boolean ifStop = intent.getBooleanExtra("ifStop", false);
                boolean ifInput_Grounding = intent.getBooleanExtra("ifInput_Grounding", false);
                showBoxWaddr(ifStop,ifInput_Grounding);
            } else if (action.equals(ActionList.ACTION_SHOW_LIST)){   //发料
                boolean ifAutoPacking = intent.getBooleanExtra("ifAutoPacking",false);
                String sfp01 = intent.getStringExtra("sfp01");
                showList(ifAutoPacking,sfp01);
            } else if (action.equals(ActionList.ACTION_SHOW_LIST1)){   //厂内调拨过账
                boolean ifAutoAllot = intent.getBooleanExtra("ifAutoAllot",false);
                String imm01 = intent.getStringExtra("imm01");
                showList1(ifAutoAllot,imm01);    //
            } else if (action.equals(ActionList.ACTION_SHOW_LIST2)){   //下阶报废过账
                boolean ifAutoAllot = intent.getBooleanExtra("ifAutoAllot",false);
                String sfk01 = intent.getStringExtra("sfk01");
                showList2(ifAutoAllot,sfk01);    //
            } else if (action.equals(ActionList.ACTION_SHOW_LIST3)){   //组合拆解过账
                boolean ifAutoApart = intent.getBooleanExtra("ifAutoApart",false);
                String tse01 = intent.getStringExtra("tse01");
                showList3(ifAutoApart,tse01);    //
                showItems1(tse01);        //单头信息显示
            } else if (action.equals(ActionList.ACTION_SHOW_LIST4)){   //仓退交接
                boolean ifAutoJoin = intent.getBooleanExtra("ifAutoJoin",false);
                String rvv01 = intent.getStringExtra("rvv01");
                showList4(ifAutoJoin,rvv01);    //
//                showItems1(rvv01);
            } else if (action.equals(ActionList.ACTION_SHOW_LIST5)){   //送货单交接
                boolean ifAutoJoin1 = intent.getBooleanExtra("ifAutoJoin1",false);
                String rva07 = intent.getStringExtra("rva07");
                showList5(ifAutoJoin1,rva07);    //
//                showItems1(rvv01);
            }else if (action.equals(ActionList.ACTION_SHOW_GoodsIptAndEptItem)){
                String boxNum = intent.getStringExtra("box_num");
                showItems(boxNum);
            }else if (action.equals(ActionList.ACTION_SHOW_LABEL)){
                int groupPosition = intent.getIntExtra("groupPosition",9999);
                showLabel(groupPosition);
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        wmsService.pn = null;
        wmsService.PKBoxList = null;
        wmsService.AKBoxList = null;    //销毁
        wmsService.BKBoxList = null;    //销毁
        wmsService.CKBoxList = null;    //销毁
        wmsService.whrBoxList = null;    //仓退数据列表销毁
        wmsService.RnBoxList = null;
        wmsService.imptAndEmptItemsList = null;
        wmsService.imgsBoxList = null;
        wmsService.iqcItemList = null;
        wmsService.scanedBox.clear();
        finish();
        System.out.println("close activty");
    }

    public void closeKeyboard() {
        View view = getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

//以下为相关activity中对应按钮触发相关操作
    public void commonDialog(final String hintString, final int Listenertype) {
        final MyDialog dialog = new MyDialog(this, hintString);
        dialog.setOnPositiveListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (Listenertype) {
                    case 1://是否扫描包装票
                        dialog.cancel();
                        break;
                    case 2://是否确定收货
                        confirmReceiving(hintString);
                        break;
                    case 3://是否确定上架
                        confirmGrounding();
                        break;
                    case 4://是否确定发料
                        confirmPacking();
                        closeKeyboard();
                        break;
                    case 5://是否确定退料
                        confirmWoReturn();
                        closeKeyboard();
                        break;
                    case 6://是否确认检测结果
                        confirmIQC();
                        break;
                    case 7://是否确认仓退
                        confirmWhReturn();
                        break;
                    case 8://是否确认调拨
                        confirmGoodsMove();
                        break;
                    case 9://是否确认强制上架
                        confirmGrounding();
                        break;
                    case 10://是否确定退货
                        confirmReturn(hintString);
                        break;
                }
                dialog.cancel();
            }
        });
        dialog.setOnNegativeListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (Listenertype) {
                    case 1://是否扫描包装票
                        dialog.cancel();
                        break;
                    case 2://是否确定收货
                        break;
                    case 3://是否确定上架
                        break;
                    case 4://是否确定发料
                        break;
                    case 5://是否确定退料
                        break;
                    case 6://是否确认检测结果
                        break;
                    case 7://是否确认仓退
                        break;
                    case 8://是否确认调拨
                        break;
                    case 9://是否确认强制上架
                        break;
                    case 10://是否确认强制退货
                        break;
                }
                dialog.cancel();
            }
        });
        dialog.show();
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth()); //设置宽度
        dialog.getWindow().setAttributes(lp);
    }

    public void backStauts(String backData) {
        if (backData.equals("获取失败")) {
            PopUtil.showPopUtil("网络连接失败，请重新检查");
        } else {
            try {
                jsonObject = new JSONObject(backData);
                String message = jsonObject.getString("message");
                int status = jsonObject.getInt("status");
                System.out.println(message);
                if(status==200){
                    Toast.makeText(this, message,
                            Toast.LENGTH_SHORT).show();
                }else {
                    PopUtil.showPopUtil(message);
                }
                // String data = jsonObject.getString("data");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void editTextDialog(String et_default, String title, String units, final int id, final int type) {
        final EditTextDialog dialog = new EditTextDialog(this, et_default,title,units);
        dialog.setOnPositiveListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (type) {
                    case 1://输入IQC合格数量
                        String et_value = dialog.editText.getText().toString();
                        if(!et_value.equals("") && 0<Double.valueOf(et_value)
                                && Double.valueOf(et_value)<=Double.valueOf(wmsService.RnBoxList.get(id).getIbb07())){
                            confirmIQCAmount(et_value,id);
                            dialog.cancel();
                        }else {
                            PopUtil.showPopUtil("输入数量有误，请重新输入");
                        }
                        break;
                }
            }
        });
        dialog.setOnNegativeListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (type) {
                    case 1://是否扫描包装票
                        dialog.cancel();
                        break;
                }
                dialog.cancel();
            }
        });
        dialog.show();
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth()); //设置宽度
        dialog.getWindow().setAttributes(lp);
    }

    public Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String data = msg.getData().getString("data");
            backStauts(data);
        }
    };

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                wmsService.ifScan = true;
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}
