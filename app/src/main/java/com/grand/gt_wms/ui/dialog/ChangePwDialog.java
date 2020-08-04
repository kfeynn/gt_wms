package com.grand.gt_wms.ui.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.grand.gt_wms.R;
import com.grand.gt_wms.service.wmsService;

/**
 * Created by Administrator on 2018/8/17.
 */

public class ChangePwDialog extends android.app.Dialog{
    private Button positiveButton, negativeButton;
    public TextView title, hint;
    public EditText old_pw, new0_pw, new1_pw;
    Context context;

    public ChangePwDialog(Context context) {
        super(context, R.style.MyDialogStyle);
        setSettingDialog();
    }

    private void setSettingDialog() {
        View mView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_changepw, null);
        title = (TextView) mView.findViewById(R.id.title);
        hint = (TextView) mView.findViewById(R.id.hint);
        old_pw = mView.findViewById(R.id.et_old_pw);
        new0_pw = mView.findViewById(R.id.et_new0_pw);
        new1_pw = mView.findViewById(R.id.et_new1_pw);
        positiveButton = (Button) mView.findViewById(R.id.positiveButton);
        negativeButton = (Button) mView.findViewById(R.id.negativeButton);
        //this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        super.setContentView(mView);
    }


    @Override
    public void setContentView(int layoutResID) {
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
    }

    @Override
    public void setContentView(View view) {
    }

    /**
     * 确定键监听器
     * @param listener
     */
    public void setOnPositiveListener(View.OnClickListener listener){
        positiveButton.setOnClickListener(listener);
    }
    /**
     * 取消键监听器
     * @param listener
     */
    public void setOnNegativeListener(View.OnClickListener listener){
        negativeButton.setOnClickListener(listener);
    }

}
