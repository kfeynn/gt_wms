/**
  * Copyright 2018 bejson.com 
  */
package com.grand.gt_wms.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Auto-generated: 2018-10-23 10:18:42
 *收货单列表条码实体类
 */
public class RnBox {

    /*允收数量*/
    private String rvb33;
    /*规格*/
    private String ima021;
    /*入库数量*/
    private String rvb30;
    /*检验码*/
    private String ima24;
    /*检验否*/
    private String rvb39;
    /*供应商编号*/
    private String rva05;
    /*收货单号*/
    private String rvb01;
    /*品名*/
    private String ima02;
    /*仓库*/
    private String rvb36;
    /*料号*/
    private String rvb05;
    /*收货单项次*/
    private String rvb02;
    /*实验退数量*/
    private String rvb29;
    /*供应商名称*/
    private String pmc03;
    /*实收数量*/
    private String rvb07;
    /*计价单位*/
    private String rvb86;
    /* 可入库数量 */
    private String rvb31;
    /* 单个条码收货数量 */
    private String ibb07;
    /*审核码*/
    private String qcs14;
    /*箱号*/
    private String barcode;
    /*库位*/
    private String rvv32="";
    /*储位*/
    private String rvv33="";
    /*备注*/
    private String rvbud01;
    /**
     * 标记是否被扫描
     */
    private boolean ifScanedBox = false;

    private List<Label> labels = new ArrayList<Label>();

    private boolean isExpand;

    private double remaining_label;

    private int iqcStatus = 0;//0代表未检；1代表合格，2代表不合格

    public void setRvb33(String rvb33) {
         this.rvb33 = rvb33;
     }
     public String getRvb33() {
         return rvb33;
     }

    public void setIma021(String ima021) {
         this.ima021 = ima021;
     }
     public String getIma021() {
         return ima021;
     }

    public void setRvb30(String rvb30) {
         this.rvb30 = rvb30;
     }
     public String getRvb30() {
         return rvb30;
     }

    public void setIma24(String ima24) {
         this.ima24 = ima24;
     }
     public String getIma24() {
         return ima24;
     }

    public void setRvb39(String rvb39) {
         this.rvb39 = rvb39;
     }
     public String getRvb39() {
         return rvb39;
     }

    public void setRva05(String rva05) {
         this.rva05 = rva05;
     }
     public String getRva05() {
         return rva05;
     }

    public void setRvb01(String rvb01) {
         this.rvb01 = rvb01;
     }
     public String getRvb01() {
         return rvb01;
     }

    public void setIma02(String ima02) {
         this.ima02 = ima02;
     }
     public String getIma02() {
         return ima02;
     }

    public void setRvb05(String rvb05) {
         this.rvb05 = rvb05;
     }
     public String getRvb05() {
         return rvb05;
     }

    public void setRvb02(String rvb02) {
         this.rvb02 = rvb02;
     }
     public String getRvb02() {
         return rvb02;
     }

    public void setRvb29(String rvb29) {
         this.rvb29 = rvb29;
     }
     public String getRvb29() {
         return rvb29;
     }

    public void setPmc03(String pmc03) {
         this.pmc03 = pmc03;
     }
     public String getPmc03() {
         return pmc03;
     }

    public void setRvb07(String rvb07) {
         this.rvb07 = rvb07;
     }
     public String getRvb07() {
         return rvb07;
     }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getRvb31() {
        return rvb31;
    }

    public void setRvb31(String rvb31) {
        this.rvb31 = rvb31;
    }

    public String getIbb07() {
        return ibb07;
    }

    public void setIbb07(String ibb07) {
        this.ibb07 = ibb07;
    }

    public String getQcs14() {
        return qcs14;
    }

    public void setQcs14(String qcs14) {
        this.qcs14 = qcs14;
    }

    public boolean isIfScanedBox() {
        return ifScanedBox;
    }

    public void setIfScanedBox(boolean ifScanedBox) {
        this.ifScanedBox = ifScanedBox;
    }

    public String getRvv32() {
        return rvv32;
    }

    public void setRvv32(String rvv32) {
        this.rvv32 = rvv32;
    }

    public String getRvv33() {
        return rvv33;
    }

    public void setRvv33(String rvv33) {
        this.rvv33 = rvv33;
    }

    public int getIqcStatus() {
        return iqcStatus;
    }

    public void setIqcStatus(int iqcStatus) {
        this.iqcStatus = iqcStatus;
    }

    public String getRvb86() {
        return rvb86;
    }

    public void setRvb86(String rvb86) {
        this.rvb86 = rvb86;
    }

    public String getRvb36() {
        return rvb36;
    }

    public void setRvb36(String rvb36) {
        this.rvb36 = rvb36;
    }

    public List<Label> getLabels() {
        return labels;
    }

    public void setLabels(List<Label> labels) {
        this.labels = labels;
    }

    public boolean isExpand() {
        return isExpand;
    }

    public void setExpand(boolean expand) {
        isExpand = expand;
    }

    public double getRemaining_label() {
        return remaining_label;
    }

    public void setRemaining_label(double remaining_label) {
        this.remaining_label = remaining_label;
    }

    public String getRvbud01() {
        return rvbud01;
    }

    public void setRvbud01(String rvbud01) {
        this.rvbud01 = rvbud01;
    }
}