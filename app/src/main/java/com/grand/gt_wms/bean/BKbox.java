package com.grand.gt_wms.bean;

import android.support.annotation.NonNull;

/*
此值对象为报废接口单身所用
 */
public class BKbox  implements Comparable<BKbox>{

    /*报废单*/
    private String sfk01;
    /*项次*/
    private String sfl02;
    /*料号*/
    private String sfl03;
    /*品名*/
    private String ima02;
    /*规格*/
    private String ima021;
    /*应发数量*/
    private String sfl05;
    /*已发数量*/
    private String sfl06;
    /*已报废数量*/
    private String sfl063;
    /*本次报废套数*/
    private String sflud10;
    /*本次报废数量*/
    private String sfl07;
    /**
     * 标记是否被扫描
     */
    private boolean ifScanedBox = false;

    public String getSfk01() { return sfk01; }
    public void setSfk01(String sfk01) { this.sfk01 = sfk01; }

    public String getSfl02() { return sfl02; }
    public void setSfl02(String sfl02) { this.sfl02 = sfl02; }

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

    public String getSfl03() { return sfl03; }
    public void setSfl03(String sfl03) { this.sfl03 = sfl03; }

    public String getSfl05() { return sfl05; }
    public void setSfl05(String sfl05) { this.sfl05 = sfl05; }

    public String getSfl06() { return sfl06; }
    public void setSfl06(String sfl06) { this.sfl06 = sfl06; }

    public String getSfl063() { return sfl063; }
    public void setSfl063(String sfl063) { this.sfl063 = sfl063; }

    public String getSfl07() { return sfl07; }
    public void setSfl07(String sfl07) { this.sfl07 = sfl07; }

    public String getSflud10() { return sflud10; }
    public void setSflud10(String sflud10) { this.sflud10 = sflud10; }
    public boolean isIfScanedBox() {
        return ifScanedBox;
    }

    public void setIfScanedBox(boolean ifScanedBox) {
        this.ifScanedBox = ifScanedBox;
    }

    @Override //字符串拼接
    public String toString() {
        return "BKbox [sfk01=" + sfk01 + ", sfl02=" + sfl02 + ", sfl03="
                + sfl03 + ", ima02=" + ima02 + ", ima021="
                + ima021 + ", sfl05=" + sfl05 + ",sfl06=" + sfl06 + ", sfl063="
                + sfl063 + ", sflud10=" + sflud10 + ", sfl07=" + sfl07  + "]";

    }
    /**
     * 物料号排序
     *
     * @param bKbox
     * @return
     */
    @Override
    public int compareTo(@NonNull BKbox bKbox) {
        if (Long.parseLong(this.sfl03) - Long.parseLong(bKbox.getSfl03()) > 0) {
            return 1;
        } else {
            return -1;
        }
    }
}
