package com.grand.gt_wms.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/12/11.
 * 收货单实体类
 */

public class RN {
    /*收货单号*/
    private String rvb01;
    private List<RnBox> rnBoxList = new ArrayList<RnBox>();

    public String getRvb01() {
        return rvb01;
    }

    public void setRvb01(String rvb01) {
        this.rvb01 = rvb01;
    }

    public List<RnBox> getRnBoxList() {
        return rnBoxList;
    }

    public void setRnBoxList(List<RnBox> rnBoxList) {
        this.rnBoxList = rnBoxList;
    }
}
