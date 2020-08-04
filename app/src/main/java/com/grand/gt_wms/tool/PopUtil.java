package com.grand.gt_wms.tool;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;


import com.grand.gt_wms.R;
import com.grand.gt_wms.service.wmsService;
import com.grand.gt_wms.ui.activity.BaseActivity;

public class PopUtil extends PopupWindow {

    private static Context mContext;
    private static View mPopWindow;
    public static TextView textView;

    public PopUtil(Context context) {
        this.mContext = context;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        mPopWindow = inflater.inflate(R.layout.toast, null);
        textView = (TextView) mPopWindow.findViewById(R.id.tv_toast);
    }

    public static PopUtil createPopUtil(Context context){
        PopUtil popUtil = new PopUtil(context);
        // 把View添加到PopWindow中
        popUtil.setContentView(mPopWindow);
        //设置SelectPicPopupWindow弹出窗体的宽
        popUtil.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        popUtil.setHeight(dip2px(context, 60));
        //  设置SelectPicPopupWindow弹出窗体可点击
        popUtil.setFocusable(false);
        //   设置背景透明
        popUtil.setBackgroundDrawable(new ColorDrawable(0x00000000));
        popUtil.setBackgroundDrawable(new BitmapDrawable());
        popUtil.setOutsideTouchable(true);
        return popUtil;
    }

    public static void showPopUtil(String msg){
        wmsService.popUtil = PopUtil.createPopUtil(BaseActivity.getCurrentActivity());
        LayoutInflater inflater = LayoutInflater.from(BaseActivity.getCurrentActivity());
        // 引入窗口配置文件 - 即弹窗的界面
        View view = inflater.inflate(R.layout.toast, null);
        wmsService.popUtil.textView.setText(msg);
        wmsService.popUtil.showAtLocation(view, Gravity.BOTTOM,0,0);
        wmsService.sp.play(wmsService.soundPoolMap.get(1), 1, 1, 0, 0, 1);
        wmsService.ifScan = false;
        Vibrator vibrator = (Vibrator)mContext.getSystemService(mContext.VIBRATOR_SERVICE);
        vibrator.vibrate(3000);
    }

    public static void showPopUtil(String msg,boolean ifMusic){
        wmsService.popUtil = PopUtil.createPopUtil(BaseActivity.getCurrentActivity());
        LayoutInflater inflater = LayoutInflater.from(BaseActivity.getCurrentActivity());
        // 引入窗口配置文件 - 即弹窗的界面
        View view = inflater.inflate(R.layout.toast, null);
        wmsService.popUtil.textView.setText(msg);
        wmsService.popUtil.showAtLocation(view, Gravity.BOTTOM,0,0);
        if(ifMusic){
            wmsService.sp.play(wmsService.soundPoolMap.get(1), 1, 1, 0, 0, 1);
            wmsService.ifScan = false;
        }
    }

    public static void closePopUtil(){
        wmsService.popUtil.dismiss();
    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }



}
