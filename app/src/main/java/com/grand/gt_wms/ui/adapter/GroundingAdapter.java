package com.grand.gt_wms.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.widget.ImageView;

import com.grand.gt_wms.ui.groupedadapter.holder.BaseViewHolder;
import com.grand.gt_wms.R;
import com.grand.gt_wms.bean.Box;
import com.grand.gt_wms.bean.Label;
import com.grand.gt_wms.bean.PNSub;
import com.grand.gt_wms.bean.RnBox;
import com.grand.gt_wms.service.wmsService;
import com.grand.gt_wms.tool.MyGroupedRecyclerViewAdapter;

/**
 * Created by Administrator on 2018/8/24.
 */

public class GroundingAdapter extends MyGroupedRecyclerViewAdapter {
    private String boxNum = "";
    public GroundingAdapter(Context context,String boxNum) {
        super(context);
        this.boxNum = boxNum;
    }

    @Override
    public int getGroupCount() {
        if(wmsService.RnBoxList != null){
            return wmsService.RnBoxList.size();
        }else {
            return 0;
        }

    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (!isExpand(groupPosition)) {
            return 0;
        }
        return wmsService.RnBoxList == null ? 0 : wmsService.RnBoxList.get(groupPosition).getLabels().size();
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
        return R.layout.grid_ground_boxlist;
    }

    @Override
    public int getFooterLayout(int viewType) {
        return 0;
    }

    @Override
    public int getChildLayout(int viewType) {
        return R.layout.grid_ground_label;
    }

    @Override
    public void onBindHeaderViewHolder(BaseViewHolder holder, int groupPosition) {
        if (wmsService.RnBoxList != null) {
            RnBox rnbox = wmsService.RnBoxList.get(groupPosition);
            if(!rnbox.isIfScanedBox()){
                holder.setBackgroundRes(R.id.linearLayout1,R.layout.bg_group);
            }else {
                holder.setBackgroundColor(R.id.linearLayout1, Color.YELLOW);
            }
            if(!rnbox.getRvv33().equals("") && !rnbox.getRvv32().equals("")){
                holder.setBackgroundColor(R.id.linearLayout1,Color.GREEN);
            }
                holder.setText(R.id.tv_boxnum, "箱号："+ rnbox.getBarcode() + "");
                holder.setText(R.id.tv_box_rvv32,"库位："+rnbox.getRvv32()+"");
                holder.setText(R.id.tv_box_rvv33,"储位："+rnbox.getRvv33()+"");
                holder.setText(R.id.tv_rvb39,"是否IQC检验："+rnbox.getQcs14()+"");
                holder.setText(R.id.tv_ima24,"是否需要IQC检验："+rnbox.getRvb39()+"");
                holder.setText(R.id.tv_rvb05,"料号:" + rnbox.getRvb05());
                holder.setText(R.id.tv_ima02,"品名:" + rnbox.getIma02());
                holder.setText(R.id.tv_rvb33,"入库数量:" + rnbox.getIbb07());
                holder.setText(R.id.tv_label,"剩余内标贴数量:"+ rnbox.getRemaining_label());
                holder.setText(R.id.tv_box_rvbud01,"备注:" + rnbox.getRvbud01()+"");
        } else {
            holder.setText(R.id.tv_boxnum, "");
        }
    }

    @Override
    public void onBindFooterViewHolder(BaseViewHolder holder, int groupPosition) {

    }

    @Override
    public void onBindChildViewHolder(BaseViewHolder holder, int groupPosition, int childPosition) {
        RnBox rnbox = wmsService.RnBoxList.get(groupPosition);
        if(rnbox.getLabels().size()!= 0){
            holder.setText(R.id.tv_labelid, "标贴号："+ rnbox.getLabels().get(childPosition).getLabelId() + "");
            holder.setText(R.id.tv_qty, "数量："+ rnbox.getLabels().get(childPosition).getQty() + "");
        }

    }

    /**
     * 判断当前组是否展开
     *
     * @param groupPosition
     * @return
     */
    public boolean isExpand(int groupPosition) {
        RnBox entity = wmsService.RnBoxList.get(groupPosition);
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
        RnBox entity = wmsService.RnBoxList.get(groupPosition);
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
        RnBox entity = wmsService.RnBoxList.get(groupPosition);
        entity.setExpand(false);
        if (animate) {
            notifyChildrenRemoved(groupPosition);
        } else {
            notifyDataChanged();
        }
    }
}
