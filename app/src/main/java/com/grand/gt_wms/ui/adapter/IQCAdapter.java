package com.grand.gt_wms.ui.adapter;

import android.content.Context;
import android.graphics.Color;

import com.grand.gt_wms.R;
import com.grand.gt_wms.bean.IqcItem;
import com.grand.gt_wms.bean.PNSub;
import com.grand.gt_wms.bean.RnBox;
import com.grand.gt_wms.service.wmsService;
import com.grand.gt_wms.tool.MyGroupedRecyclerViewAdapter;
import com.grand.gt_wms.ui.groupedadapter.holder.BaseViewHolder;

/**
 * Created by Administrator on 2018/8/24.
 */

public class IQCAdapter extends MyGroupedRecyclerViewAdapter {
    private String boxNum = "";
    public IQCAdapter(Context context,String boxNum) {
        super(context);
        this.boxNum = boxNum;
    }

    @Override
    public int getGroupCount() {
        if(wmsService.iqcItemList != null){
            return wmsService.iqcItemList.size();
        }else {
            return 0;
        }

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
        return R.layout.grid_iqc_itemlist;
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
        if (wmsService.iqcItemList != null) {
            IqcItem iqcItem = wmsService.iqcItemList.get(groupPosition);
            holder.setText(R.id.tv_qct03, "行序："+iqcItem.getQct03() + "");
            holder.setText(R.id.tv_azf03,"检验项目："+iqcItem.getAzf03()+"");
            holder.setText(R.id.tv_qct11,"检验数:" + iqcItem.getQct11());
            //holder.setText(R.id.tv_qct08,"判断结果:");
            holder.setText(R.id.tv_qctud02,"技术标准："+iqcItem.getQctud02());
            holder.setEditText(R.id.et_qct07,iqcItem.getQct07(),groupPosition,"qct07",holder);
            holder.setEditText(R.id.et_ta_qctt01,iqcItem.getQctud02(),groupPosition,"ta_qctt01",holder);
            //holder.setRadioGroup(R.id.radiogrounp,groupPosition);
        } else {
            holder.setText(R.id.tv_boxnum, "");
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

    /**
     * 收起一个组
     *
     * @param groupPosition
     */
    public void collapseGroup(int groupPosition) {
        collapseGroup(groupPosition, false);
    }

    /**
     * 收起一个组
     *
     * @param groupPosition
     * @param animate
     */
    public void collapseGroup(int groupPosition, boolean animate) {
        PNSub entity = wmsService.pn.getPnsubs().get(groupPosition);
        entity.setExpand(false);
        if (animate) {
            notifyChildrenRemoved(groupPosition);
        } else {
            notifyDataChanged();
        }
    }
}
