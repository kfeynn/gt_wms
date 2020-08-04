package com.grand.gt_wms.ui.adapter;

import android.content.Context;

import com.grand.gt_wms.R;
import com.grand.gt_wms.bean.PNSub;
import com.grand.gt_wms.service.wmsService;
import com.grand.gt_wms.tool.MyGroupedRecyclerViewAdapter;
import com.grand.gt_wms.ui.groupedadapter.holder.BaseViewHolder;

/*
此为报废对应的适配器
 */
public class DumpimgAdapter extends MyGroupedRecyclerViewAdapter {
  public DumpimgAdapter (Context context){
     super(context);
  }
    @Override
    public int getGroupCount() {
        return wmsService.BKBoxList == null ? 0 :wmsService.BKBoxList.size();
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
        return R.layout.grid_dumpimg_boxlist;
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
        if(wmsService.BKBoxList != null){
//            if(wmsService.BKBoxList.get(groupPosition).isIfScanedBox()){
//                holder.setBackgroundRes(R.id.linearLayout1,R.color.red);
//            }else {
//                holder.setBackgroundRes(R.id.linearLayout1,R.layout.bg_group);
//            }
            holder.setText(R.id.tv_sfl03, "料号："+wmsService.BKBoxList.get(groupPosition).getSfl03());
            holder.setText(R.id.tv_ima02, "品名："+wmsService.BKBoxList.get(groupPosition).getIma02());
            holder.setText(R.id.tv_sfl07, "本次报废数量："+wmsService.BKBoxList.get(groupPosition).getSfl07());
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
