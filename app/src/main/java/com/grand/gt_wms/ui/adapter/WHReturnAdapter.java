package com.grand.gt_wms.ui.adapter;

import android.content.Context;

import com.grand.gt_wms.ui.groupedadapter.holder.BaseViewHolder;
import com.grand.gt_wms.R;
import com.grand.gt_wms.bean.PNSub;
import com.grand.gt_wms.service.wmsService;
import com.grand.gt_wms.tool.MyGroupedRecyclerViewAdapter;

/**
 * Created by Administrator on 2019/1/8.
 */

public class WHReturnAdapter extends MyGroupedRecyclerViewAdapter {
    public WHReturnAdapter(Context context) {
        super(context);
    }

    @Override
    public int getGroupCount() {
        return wmsService.whrBoxList == null ? 0 :wmsService.whrBoxList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 0;
    }

    @Override
    public boolean hasHeader(int groupPosition) {
        return true;
    }

    @Override
    public boolean hasFooter(int groupPosition) {
        return false;
    }

    @Override
    public int getHeaderLayout(int viewType) {
        return R.layout.grid_pack_boxlist;
    }

    @Override
    public int getFooterLayout(int viewType) {
        return 0;
    }

    @Override
    public int getChildLayout(int viewType) {
        return 0;
    }

    @Override
    public void onBindHeaderViewHolder(BaseViewHolder holder, int groupPosition) {
        if(wmsService.whrBoxList != null){
            if(wmsService.whrBoxList.get(groupPosition).isIfScanedBox()){
                holder.setBackgroundRes(R.id.linearLayout1,R.color.red);
            }else {
                holder.setBackgroundRes(R.id.linearLayout1,R.layout.bg_group);
            }
            holder.setText(R.id.tv_ima02, "品名："+wmsService.whrBoxList.get(groupPosition).getIma02());
            holder.setText(R.id.tv_sfs04, "料号："+wmsService.whrBoxList.get(groupPosition).getRvv31());
            holder.setText(R.id.tv_sfs05, "退料总数："+wmsService.whrBoxList.get(groupPosition).getRvv17());
            holder.setText(R.id.tv_sfs0708, "仓库/储位："+wmsService.whrBoxList.get(groupPosition).getRvv32()+"/"
                    +wmsService.whrBoxList.get(groupPosition).getRvv33());
            holder.setText(R.id.tv_rvbs03, "箱号："+wmsService.whrBoxList.get(groupPosition).getRvbs04());
            holder.setText(R.id.tv_rvbs06, "此箱退料数量："+wmsService.whrBoxList.get(groupPosition).getRvbs06());
        }
    }

    @Override
    public void onBindFooterViewHolder(BaseViewHolder holder, int groupPosition) {

    }

    @Override
    public void onBindChildViewHolder(BaseViewHolder holder, int groupPosition, int childPosition) {

    }


    /**
     * 判断当前组是否展开
     *
     * @param groupPosition
     * @return
     */
    public boolean isExpand(int groupPosition) {
        PNSub entity = wmsService.pn.getPnsubs().get(groupPosition);
        return entity.isExpand();
    }

    /**
     * 展开一个组
     *
     * @param groupPosition
     */
    public void expandGroup(int groupPosition) {
        expandGroup(groupPosition, false);
    }

    /**
     * 展开一个组
     *
     * @param groupPosition
     * @param animate
     */
    public void expandGroup(int groupPosition, boolean animate) {
        PNSub entity = wmsService.pn.getPnsubs().get(groupPosition);
        entity.setExpand(true);
        if (animate) {
            notifyChildrenInserted(groupPosition);
        } else {
            notifyDataChanged();
        }
    }
}
