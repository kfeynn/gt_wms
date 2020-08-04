package com.grand.gt_wms.ui.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.grand.gt_wms.R;
import com.grand.gt_wms.bean.Box;
import com.grand.gt_wms.bean.ImptAndEmptItem;
import com.grand.gt_wms.bean.PNSub;
import com.grand.gt_wms.bean.RnBox;
import com.grand.gt_wms.service.wmsService;
import com.grand.gt_wms.tool.MyGroupedRecyclerViewAdapter;
import com.grand.gt_wms.ui.groupedadapter.holder.BaseViewHolder;
/**
 * Created by Administrator on 2018/8/24.
 */

public class GoodsIptAndEptAdapter extends MyGroupedRecyclerViewAdapter {
    private String boxNum = "";
    public GoodsIptAndEptAdapter(Context context, String boxNum) {
        super(context);
        this.boxNum = boxNum;
    }

    @Override
    public int getGroupCount() {
        if(wmsService.imptAndEmptItemsList != null){
            return wmsService.imptAndEmptItemsList.size();
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
        return R.layout.grid_goodsiptandept_item;
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
        if (wmsService.imptAndEmptItemsList != null) {
            ImptAndEmptItem item = wmsService.imptAndEmptItemsList.get(groupPosition);
            holder.setText(R.id.tv_tlf06, "时间："+item.getTlf06() + "");
            holder.setText(R.id.tv_tlf902, "仓库："+item.getTlf902() + "");   //仓库
            if(item.getTlf907().equals("1")){
                holder.setText(R.id.tv_tlf907,"操作类型：入库");
            }else if(item.getTlf907().equals("-1")){
                holder.setText(R.id.tv_tlf907,"操作类型：出库");
            }
            holder.setText(R.id.tv_tlf10,"数量："+item.getTlf10()+"");
            holder.setText(R.id.tv_gen02,"操作人员："+item.getGen02()+"");
            holder.setText(R.id.tv_tlf09,"工号："+item.getTlf09()+"");
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
