package com.grand.gt_wms.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.grand.gt_wms.R;
import com.grand.gt_wms.service.wmsService;
import com.grand.gt_wms.tool.PopUtil;
import com.grand.gt_wms.ui.activity.AllotActivity;
import com.grand.gt_wms.ui.activity.ApartActivity;
import com.grand.gt_wms.ui.activity.BaseActivity;
import com.grand.gt_wms.ui.activity.DumpingActivity;
import com.grand.gt_wms.ui.activity.GoodsIptAndEptActivity;
import com.grand.gt_wms.ui.activity.GoodsMoveActivity;
import com.grand.gt_wms.ui.activity.GroundingActivity;
import com.grand.gt_wms.ui.activity.IQCActivity;
import com.grand.gt_wms.ui.activity.Join1Activity;
import com.grand.gt_wms.ui.activity.JoinActivity;
import com.grand.gt_wms.ui.activity.PackingActivity;
import com.grand.gt_wms.ui.activity.ReceivingActivity;
import com.grand.gt_wms.ui.activity.ReturnActivity;
import com.grand.gt_wms.ui.activity.UserActivity;
import com.grand.gt_wms.ui.activity.WHReturnActivity;
import com.grand.gt_wms.ui.activity.WOReturnActivity;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ListHolder>{

    private Context mContext;
    public MainAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public ListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view =  View.inflate(parent.getContext(), R.layout.grid_item, null);
        return new ListHolder(view);
    }

//主界面图标显示处理
    @Override
    public void onBindViewHolder(final ListHolder holder, final int position) {
        holder.setData(position);
		holder.tv.setText(BaseActivity.itemArray[position]);
        String drawableName = "home"+position;
        int resid = mContext.getResources().getIdentifier(drawableName , "drawable", mContext.getPackageName());
		holder.icon.setBackgroundResource(resid);  //主界面主要功能图标显示
	}

    @Override
    public int getItemCount() {
        return BaseActivity.itemArray.length;
        //return 1;
    }

    OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = (Integer) v.getTag();
            System.out.println("已被点击位置:" + position);
            switch (position){
                case 0:
                    if(wmsService.USER.getJob().equals("1") || wmsService.USER.getJob().equals("0")){
                        Intent intent0 = new Intent(mContext,ReceivingActivity.class);
                        mContext.startActivity(intent0);
                        break;
                    }else {
                        Toast.makeText(mContext, "您没有权限访问这个模块",
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 1:
                    if(wmsService.USER.getJob().equals("3") || wmsService.USER.getJob().equals("0")){
                        Intent intent0 = new Intent(mContext,IQCActivity.class);
                        mContext.startActivity(intent0);
                        break;
                    }else {
                        Toast.makeText(mContext, "您没有权限访问这个模块",
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 2:
                    if(wmsService.USER.getJob().equals("2") || wmsService.USER.getJob().equals("0")){
                        Intent intent2 = new Intent(mContext,GroundingActivity.class);
                        mContext.startActivity(intent2);
                        break;
                    }else {
                        Toast.makeText(mContext, "您没有权限访问这个模块",
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 3:
                    if(wmsService.USER.getJob().equals("2") || wmsService.USER.getJob().equals("0")){
                        Intent intent3 = new Intent(mContext,PackingActivity.class);
                        mContext.startActivity(intent3);
                        break;
                    }else {
                        Toast.makeText(mContext, "您没有权限访问这个模块",
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 4:
                    if(wmsService.USER.getJob().equals("2")||wmsService.USER.getJob().equals("1")
                            ||wmsService.USER.getJob().equals("0")){
                        Intent intent3 = new Intent(mContext,WOReturnActivity.class);
                        mContext.startActivity(intent3);
                        break;
                    }else {
                        Toast.makeText(mContext, "您没有权限访问这个模块",
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 5:
                    if(wmsService.USER.getJob().equals("2")||wmsService.USER.getJob().equals("1")
                            ||wmsService.USER.getJob().equals("0")){
                        Intent intent3 = new Intent(mContext,WHReturnActivity.class);
                        mContext.startActivity(intent3);
                        break;
                    }else {
                        Toast.makeText(mContext, "您没有权限访问这个模块",
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 6:
                    if(wmsService.USER.getJob().equals("2") || wmsService.USER.getJob().equals("0")){
                        Intent intent3 = new Intent(mContext,GoodsMoveActivity.class);
                        mContext.startActivity(intent3);
                        break;
                    }else {
                        Toast.makeText(mContext, "您没有权限访问这个模块",
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 7:
                    if(wmsService.USER.getJob().equals("2") || wmsService.USER.getJob().equals("0")){
                        Intent intent3 = new Intent(mContext,GoodsIptAndEptActivity.class);
                        mContext.startActivity(intent3);
                        break;
                    }else {
                        Toast.makeText(mContext, "您没有权限访问这个模块",
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 8:     //厂内调拨 add by zouhs 191125
                    if(wmsService.USER.getJob().equals("1")||wmsService.USER.getJob().equals("2") || wmsService.USER.getJob().equals("0")){
                        Intent intent3 = new Intent(mContext,AllotActivity.class);
                        mContext.startActivity(intent3);
                        break;
                    }else {
                        Toast.makeText(mContext, "您没有权限访问这个模块",
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
                    case 9:     //下阶报废 add by zouhs 191125
                    if(wmsService.USER.getJob().equals("1")||wmsService.USER.getJob().equals("2") || wmsService.USER.getJob().equals("0")){
                        Intent intent3 = new Intent(mContext,DumpingActivity.class);
                        mContext.startActivity(intent3);
                        break;
                    }else {
                        Toast.makeText(mContext, "您没有权限访问这个模块",
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 10:     //组合拆解 add by zouhs 200312
                    if(wmsService.USER.getJob().equals("1")||wmsService.USER.getJob().equals("2") || wmsService.USER.getJob().equals("0")){
                        Intent intent3 = new Intent(mContext,ApartActivity.class);
                        mContext.startActivity(intent3);
                        break;
                    }else {
                        Toast.makeText(mContext, "您没有权限访问这个模块",
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
                    case 11:     //仓退交接 add by zouhs 200413
                    if(wmsService.USER.getJob().equals("1")||wmsService.USER.getJob().equals("2") || wmsService.USER.getJob().equals("0")){
                        Intent intent3 = new Intent(mContext,JoinActivity.class);
                        mContext.startActivity(intent3);
                        break;
                    }else {
                        Toast.makeText(mContext, "您没有权限访问这个模块",
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 12:     //送货单交接 add by zouhs 200512
                    if(wmsService.USER.getJob().equals("1")||wmsService.USER.getJob().equals("2") || wmsService.USER.getJob().equals("0")){
                        Intent intent3 = new Intent(mContext,Join1Activity.class);
                        mContext.startActivity(intent3);
                        break;
                    }else {
                        Toast.makeText(mContext, "您没有权限访问这个模块",
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 13:     //委外退货单交接 add by zouhs 2000605
                    if(wmsService.USER.getJob().equals("1")|| wmsService.USER.getJob().equals("0")){
                        Intent intent3 = new Intent(mContext,ReturnActivity.class);
                        mContext.startActivity(intent3);
                        break;
                    }else {
                        Toast.makeText(mContext, "您没有权限访问这个模块",
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
                 case 14:   //个人中心
                    Intent intent6 = new Intent(mContext,UserActivity.class);
                    mContext.startActivity(intent6);
                    break;

                default:
                    Toast.makeText(mContext, "功能正在开发中，敬请关注", Toast.LENGTH_SHORT).show();
                    break;

            }
        }
    };
    private ImageView getView(int position) {
        ImageView imgView = new ImageView(mContext);
        imgView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        System.out.println(position);
        return imgView;
    }

    class ListHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView tv;
        public ListHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(onClickListener);
            icon = itemView.findViewById(R.id.iv_item);
			tv = itemView.findViewById(R.id.tv_name);
        }

        public void setData(int position){
            itemView.setTag(position);
        }
    }

}

