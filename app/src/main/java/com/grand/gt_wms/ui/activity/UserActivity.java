package com.grand.gt_wms.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.grand.gt_wms.R;
import com.grand.gt_wms.service.wmsService;
import com.grand.gt_wms.tool.ActionList;
import com.grand.gt_wms.tool.HttpPath;
import com.grand.gt_wms.tool.HttpReq;
import com.grand.gt_wms.ui.dialog.ChangePwDialog;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/9/20.
 * 个人中心页面
 */

public class UserActivity extends BaseActivity {

    private TextView tv_name, tv_employee, tv_email, tv_version;
    private Button btn_password, btn_log_off;
    private String oldPw, newPw0, newPw1;
    private Context context;
    private List<BasicNameValuePair> list = null;
    private ChangePwDialog changePwDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        context = this;
        init();
        tv_name.setText("姓名："+ wmsService.USER.getName());
        tv_employee.setText("工号："+wmsService.USER.getEmployee());
        tv_email.setText("邮箱：" + wmsService.USER.getEmail());
        tv_version.setText("版本名："+ getVersionName(this));
        btn_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePwDialog();
            }
        });
        btn_log_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void changePwDialog(){
        changePwDialog = new ChangePwDialog(this);

        changePwDialog.setOnNegativeListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePwDialog.cancel();
            }
        });
        changePwDialog.setOnPositiveListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oldPw = changePwDialog.old_pw.getText().toString();
                newPw0 = changePwDialog.new0_pw.getText().toString();
                newPw1 = changePwDialog.new1_pw.getText().toString();
                if(ifChangePw(oldPw,newPw0,newPw1)){
                    list = new ArrayList<BasicNameValuePair>();
                    list.add(new BasicNameValuePair("employee",wmsService.USER.getEmployee()));
                    list.add(new BasicNameValuePair("password",newPw1));
                    HttpReq httpReq = new HttpReq(list, HttpPath.getChangePwPath(),
                            mhandler, ActionList.GET_CHANGEPASSWORD_HANDLER_TYPE, context);
                    httpReq.start();
                }

            }
        });
        changePwDialog.show();
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = changePwDialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth()); //设置宽度
        changePwDialog.getWindow().setAttributes(lp);
    }

    private boolean ifChangePw(String oldPw,String newPw0, String newPw1){
        if(oldPw.equals(wmsService.USER.getPassword())){
            if(newPw0.equals(newPw1)){
                return true;
            }else {
                Toast.makeText(this, "新密码不一致，请重新输入",
                        Toast.LENGTH_SHORT).show();
                return false;
            }
        }else {
            Toast.makeText(this, "原密码错误，请重新输入",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void init(){
        setTitle(itemArray[14]);
        back();
        tv_name = findViewById(R.id.name);
        tv_email = findViewById(R.id.email);
        tv_employee = findViewById(R.id.employee);
        btn_password = findViewById(R.id.btn_password);
        btn_log_off = findViewById(R.id.btn_log_off);
        tv_version = findViewById(R.id.tv_version);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private Handler mhandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String data = msg.getData().getString("data");
            getChangePw(data);
        }
    };

    private void getChangePw(String backData){
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(backData);
            String msg = jsonObject.getString("message");
            Toast.makeText(this, msg,
                    Toast.LENGTH_SHORT).show();
            if(msg.equals("密码修改成功")){
                changePwDialog.cancel();
                Intent intent = new Intent(this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    //版本名
    public static String getVersionName(Context context) {
        return getPackageInfo(context).versionName;
    }

    private static PackageInfo getPackageInfo(Context context) {
        PackageInfo pi = null;

        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);

            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pi;
    }


}
