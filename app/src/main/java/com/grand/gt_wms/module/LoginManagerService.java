package com.grand.gt_wms.module;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.google.gson.Gson;
import com.grand.gt_wms.bean.User;
import com.grand.gt_wms.service.wmsBroadcastReceiver;
import com.grand.gt_wms.service.wmsService;
import com.grand.gt_wms.tool.ActionList;
import com.grand.gt_wms.tool.HttpPath;
import com.grand.gt_wms.tool.HttpReq;
import com.grand.gt_wms.ui.activity.LoginActivity;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/10/15.
 */

public class LoginManagerService implements wmsBroadcastReceiver.HalLoginCallback {
    private List<BasicNameValuePair> list = null;
    private JSONObject jsonObject;
    private Context context;


    public LoginManagerService(Context context) {
        this.context = context;
        wmsBroadcastReceiver.getInstance().registerHalLoginCallback(this);
    }

    @Override
    public void onLogin(Intent intent) {
        String username = intent.getStringExtra("username");
        String password = intent.getStringExtra("password");
        list = new ArrayList<BasicNameValuePair>();
        list.add(new BasicNameValuePair("employee", username));
        list.add(new BasicNameValuePair("password", password));
        getUrlData(list, HttpPath.getLoginPath(), ActionList.GET_LOGIN_HANDLER_TYPE, LoginActivity.context);
    }

    public void login(String backData) {
        try {
            jsonObject = new JSONObject(backData);
            String msg = jsonObject.getString("message");
            String data = jsonObject.getString("data");
            Gson gson = new Gson();
            wmsService.USER = gson.fromJson(data, User.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(ActionList.ACTION_SHOW_LOGIN);
        if (wmsService.USER == null) {
            intent.putExtra("ifLogin", false);
            context.sendBroadcast(intent);
        } else {
            intent.putExtra("ifLogin", true);
            context.sendBroadcast(intent);
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
                case ActionList.GET_LOGIN_HANDLER_TYPE:
                    login(data);
                    break;
            }
        }
    };
}
