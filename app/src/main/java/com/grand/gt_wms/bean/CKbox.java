package com.grand.gt_wms.bean;

import android.support.annotation.NonNull;

/*
此值对象为拆解所用
 */
public class CKbox  implements Comparable<CKbox>{
    /*拆解单*/
    private String tse01;
    /*拆解料号*/
    private String tse03;
    /*拆解仓库*/
    private String tse12;
    /*拆解数量*/
    private String tse05;
    /*拆解项次*/
    private String tsf02;
    /*分解料号*/
    private String tsf03;
    /*品名*/
    private String ima02;
    /*规格*/
    private String ima021;
    /*分解仓库*/
    private String tsf12;
    /*分解储位*/
    private String tsf13;
    /*分解批号*/
    private String tsf14;
    /*单位*/
    private String tsf04;
    /*分解数量*/
    private String tsf05;
    /**
     * 标记是否被扫描
     */
    private boolean ifScanedBox = false;

    public String getTse01() {  return tse01; }
    public void setTse01(String tse01){this.tse01=tse01;}

    public String getTse03() {  return tse03; }
    public void setTse03(String tse03){this.tse03=tse03;}

    public String getTse12() {  return tse12; }
    public void setTse12(String tse12){this.tse12=tse12;}

    public String getTse05() {  return tse05; }
    public void setTse05(String tse05){this.tse05=tse05;}

    public String getTsf02() {  return tsf02; }
    public void setTsf02(String tsf02){this.tsf02=tsf02;}

    public String getTsf03() {  return tsf03; }
    public void setTsf03(String tsf03){this.tsf03=tsf03;}

    public String getIma02() {
        return ima02;
    }
    public void setIma02(String ima02) {
        this.ima02 = ima02;
    }

    public String getIma021() {
        return ima021;
    }
    public void setIma021(String ima021) {
        this.ima021 = ima021;
    }

    public String getTsf12() {  return tsf12; }
    public void setTsf12(String tsf12){this.tsf12=tsf12;}

    public String getTsf13() {  return tsf13; }
    public void setTsf13(String tsf13){this.tsf13=tsf13;}

    public String getTsf14() {  return tsf14; }
    public void setTsf14(String tsf14){this.tsf14=tsf14;}

    public String getTsf04() {  return tsf04; }
    public void setTsf04(String tsf04){this.tsf04=tsf04;}

    public String getTsf05() {  return tsf05; }
    public void setTsf05(String tsf05){this.tsf05=tsf05;}

    public boolean isIfScanedBox() {
        return ifScanedBox;
    }
    public void setIfScanedBox(boolean ifScanedBox) {
        this.ifScanedBox = ifScanedBox;
    }

    public String toString() {
        return "CKbox [tse01=" + tse01 + ", tse03=" + tse03 + ", tse12="
                + tse12 + ", tse05=" + tse05 +", tsf02=" + tsf02 +", tsf03=" + tsf03 +", ima02=" + ima02 + ", ima021="
                + ima021 + ", tsf12=" +tsf12 + ",tsf13=" + tsf13 + ", tsf14="
                + tsf14 + ", tsf04=" + tsf04 + ", tsf05=" + tsf05  + "]";
}
    /**
     * 物料号排序
     *
     * @param cKbox
     * @return
     */
    @Override
    public int compareTo(@NonNull CKbox cKbox) {
        if (Long.parseLong(this.tsf03) - Long.parseLong(cKbox.getTsf03()) > 0) {
            return 1;
        } else {
            return -1;
        }
    }
}
