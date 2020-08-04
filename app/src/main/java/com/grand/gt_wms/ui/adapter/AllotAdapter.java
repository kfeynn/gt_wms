package com.grand.gt_wms.ui.adapter;

import android.content.Context;

import com.grand.gt_wms.R;
import com.grand.gt_wms.bean.PNSub;
import com.grand.gt_wms.service.wmsService;
import com.grand.gt_wms.tool.MyGroupedRecyclerViewAdapter;
import com.grand.gt_wms.ui.groupedadapter.holder.BaseViewHolder;

/**
 * 此类为调拨activity对应的RecyclerView的适配器
 */
public class AllotAdapter extends MyGroupedRecyclerViewAdapter {
    public AllotAdapter(Context context) {
        super(context);
    }

    @Override
    public int getGroupCount() {
        return wmsService.AKBoxList == null ? 0 :wmsService.AKBoxList.size();
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
    public int getHeaderLayout(int viewType)
        {
            return R.layout.grid_allot_boxlist;
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
        if(wmsService.AKBoxList != null){
            if(wmsService.AKBoxList.get(groupPosition).isIfScanedBox()){
                holder.setBackgroundRes(R.id.linearLayout1,R.color.red);
            }else {
                holder.setBackgroundRes(R.id.linearLayout1,R.layout.bg_group);
            }
            holder.setText(R.id.tv_imn03, "料号："+wmsService.AKBoxList.get(groupPosition).getImn03());
            holder.setText(R.id.tv_ima02, "品名："+wmsService.AKBoxList.get(groupPosition).getIma02());
            holder.setText(R.id.tv_rvbs03, "箱号："+wmsService.AKBoxList.get(groupPosition).getRvbs04());
            holder.setText(R.id.tv_rvbs06, "此箱调拨数量："+wmsService.AKBoxList.get(groupPosition).getRvbs06());
            holder.setText(R.id.tv_imn10, "调拨总数："+wmsService.AKBoxList.get(groupPosition).getImn10());
            holder.setText(R.id.tv_imn0405, "拨出仓库/储位："+wmsService.AKBoxList.get(groupPosition).getImn04()+"/"
                        +wmsService.AKBoxList.get(groupPosition).getImn05());
            holder.setText(R.id.tv_imn1516, "拨入仓库/储位："+wmsService.AKBoxList.get(groupPosition).getImn15()+"/"
                    +wmsService.AKBoxList.get(groupPosition).getImn16());

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
