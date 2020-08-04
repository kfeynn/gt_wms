package com.grand.gt_wms.ui.adapter;

import android.content.Context;
import android.graphics.Color;

import com.grand.gt_wms.ui.groupedadapter.holder.BaseViewHolder;
import com.grand.gt_wms.R;
import com.grand.gt_wms.bean.ImgsBox;
import com.grand.gt_wms.bean.PNSub;
import com.grand.gt_wms.bean.RnBox;
import com.grand.gt_wms.service.wmsService;
import com.grand.gt_wms.tool.MyGroupedRecyclerViewAdapter;

/**
 * Created by Administrator on 2019/1/10.
 */

public class GoodsMoveAdapter extends MyGroupedRecyclerViewAdapter {
    private String boxNum = "";
    public GoodsMoveAdapter(Context context, String boxNum) {
        super(context);
        this.boxNum = boxNum;
    }

    @Override
    public int getGroupCount() {
        if(wmsService.imgsBoxList != null){
            return wmsService.imgsBoxList.size();
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
        return R.layout.grid_goodsmove;
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
        if (wmsService.imgsBoxList != null) {
            ImgsBox imgsBox = wmsService.imgsBoxList.get(groupPosition);
            if(!imgsBox.isIfScanedBox()){
                holder.setBackgroundRes(R.id.linearLayout1,R.layout.bg_group);
            }else {
                holder.setBackgroundColor(R.id.linearLayout1, Color.RED);
            }
            holder.setText(R.id.tv_boxnum, "箱号："+ imgsBox.getImgs06() + "");
            holder.setText(R.id.tv_box_rvv32,"拨出库位："+imgsBox.getImgs02()+"");
            holder.setText(R.id.tv_box_rvv33,"拨出储位："+imgsBox.getImgs05()+"");
            holder.setText(R.id.tv_rvb05,"料号:" + imgsBox.getImgs01());
            holder.setText(R.id.tv_ima02,"品名:" + imgsBox.getIma02());
            holder.setText(R.id.tv_rvb33,"调拨数量:" + imgsBox.getImgs08());
            holder.setText(R.id.tv_box_imn15,"拨入库位："+imgsBox.getImn15()+"");
            holder.setText(R.id.tv_box_imn16,"拨入储位："+imgsBox.getImn16()+"");
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
