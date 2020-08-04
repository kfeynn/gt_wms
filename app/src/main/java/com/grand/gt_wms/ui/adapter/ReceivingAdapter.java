package com.grand.gt_wms.ui.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.grand.gt_wms.ui.groupedadapter.holder.BaseViewHolder;
import com.grand.gt_wms.R;
import com.grand.gt_wms.bean.PNSub;
import com.grand.gt_wms.service.wmsService;
import com.grand.gt_wms.tool.MyGroupedRecyclerViewAdapter;

/**
 * Created by Administrator on 2018/8/9.
 */

public class ReceivingAdapter extends MyGroupedRecyclerViewAdapter {


    public ReceivingAdapter(Context context) {
        super(context);

    }


    @Override
    public int getGroupCount() {
        return wmsService.pn == null ? 0 : wmsService.pn.getPnsubs().size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (!isExpand(groupPosition)) {
            return 0;
        }
        return wmsService.pn == null ? 0 : wmsService.pn.getPnsubs().get(groupPosition).getBoxs().size();
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
        return R.layout.grid_pnsub;
    }

    @Override
    public int getFooterLayout(int viewType) {
        return 0;
    }

    @Override
    public int getChildLayout(int viewType) {
        return R.layout.grid_boxlist;
    }


    @Override
    public void onBindHeaderViewHolder(BaseViewHolder holder, int groupPosition) {
        if (wmsService.pn != null) {
            holder.setText(R.id.pnSub_pmn20, wmsService.pn.getPnsubs().get(groupPosition).getPmn20() + "");
            holder.setText(R.id.pnSub_pmn04, wmsService.pn.getPnsubs().get(groupPosition).getPmn04() + "");
            holder.setText(R.id.pnSub_pmn07,wmsService.pn.getPnsubs().get(groupPosition).getPmn07() +"");
        } else {
            holder.setText(R.id.pnSub_pmn20, "");
            holder.setText(R.id.pnSub_pmn04, "");
            holder.setText(R.id.pnSub_pmn07, "");
        }
        ImageView ivState = holder.get(R.id.iv_state);
        if (wmsService.pn.getPnsubs().get(groupPosition).isExpand()) {
            ivState.setRotation(90);
        } else {
            ivState.setRotation(0);
        }
    }

    @Override
    public void onBindFooterViewHolder(BaseViewHolder holder, int groupPosition) {

    }

    @Override
    public void onBindChildViewHolder(BaseViewHolder holder, int groupPosition, int childPosition) {
        holder.setText(R.id.tv_boxnum, "箱号：" + wmsService.pn.getPnsubs().get(groupPosition).getBoxs().
                get(childPosition).getBoxnum() + "");
        holder.setText(R.id.tv_pmn041, "品名：" + wmsService.pn.getPnsubs().get(groupPosition).getBoxs().
                get(childPosition).getPmn041() + "");
        holder.setText(R.id.tv_box_pmn20, "采购数量：" + wmsService.pn.getPnsubs().get(groupPosition).getBoxs().
                get(childPosition).getPmn20() + "");

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
