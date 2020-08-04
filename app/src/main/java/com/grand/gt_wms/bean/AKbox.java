package com.grand.gt_wms.bean;
/*
此值对象为调拨单身用
 */
import android.support.annotation.NonNull;

public class AKbox implements Comparable<AKbox> {
    /*DI单号*/
    private String imm01;
    /*项次*/
    private String imn02;
    /*料号*/
    private String imn03;
    /*品名*/
    private String ima02;
    /*规格*/
    private String ima021;
    /*拨出仓库*/
    private String imn04;
    /*拨出储位*/
    private String imn05;
    /*拨出批号*/
    private String imn06;
    /*拨入仓库*/
    private String imn15;
    /*拨入储位*/
    private String imn16;
    /*拨入批号*/
    private String imn17;
    /*拨出数量*/
    private String imn10;
    /*建议条码*/
    private String rvbs04;
    /*建议条码数量*/
    private String rvbs06;
    /**
     * 标记是否被扫描
     */
    private boolean ifScanedBox = false;

    public String getImm01() {
        return imm01;
    }

    public void setImm01(String imm01) {
        this.imm01 = imm01;
    }

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

    public String getImn02() {
        return imn02;
    }

    public void setImn02(String imn02) {
        this.imn02 = imn02;
    }

    public String getImn03() {
        return imn03;
    }

    public void setImn03(String imn03) {
        this.imn03 = imn03;
    }

    public String getImn04() {
        return imn04;
    }

    public void setImn04(String imn04) {
        this.imn04 = imn04;
    }

    public String getImn05() {
        return imn05;
    }

    public void setImn05(String imn05) {
        this.imn05 = imn05;
    }

    public String getImn06() {
        return imn06;
    }

    public void setImn06(String imn06) {
        this.imn06 = imn06;
    }

    public String getImn10() {
        return imn10;
    }

    public void setImn10(String imn10) {
        this.imn10 = imn10;
    }

    public String getImn15() {
        return imn15;
    }

    public void setImn15(String imn15) {
        this.imn15 = imn15;
    }

    public String getImn16() {
        return imn16;
    }

    public void setImn16(String imn16) {
        this.imn16 = imn16;
    }

    public String getImn17() {
        return imn17;
    }

    public void setImn17(String imn17) {
        this.imn17 = imn17;
    }

    public String getRvbs04() {
        return rvbs04;
    }

    public void setRvbs04(String rvbs04) {
        this.rvbs04 = rvbs04;
    }

    public String getRvbs06() {
        return rvbs06;
    }

    public void setRvbs06(String rvbs06) {
        this.rvbs06 = rvbs06;
    }

    public boolean isIfScanedBox() {
        return ifScanedBox;
    }

    public void setIfScanedBox(boolean ifScanedBox) {
        this.ifScanedBox = ifScanedBox;
    }


    @Override //字符串拼接
    public String toString() {
        return "AKbox [imm01=" + imm01 + ", imn02=" + imn02 + ", imn03="
                + imn03 + ", ima02=" + ima02 + ", ima021="
                + ima021 + ", imn04=" + imn04 + ",imn05=" + imn05 + ", imn06="
                + imn06 + ", imn15=" + imn15 + ", imn16=" + imn16 + ", imn17=" + imn17 + ", imn10=" + imn10 + ", rvbs04=" + rvbs04
                + ", rvbs06=" + rvbs06 + "]";
    }

    /**
     * 物料号排序
     *
     * @param aKbox
     * @return
     */
    @Override
    public int compareTo(@NonNull AKbox aKbox) {
        if (Long.parseLong(this.imn03) - Long.parseLong(aKbox.getImn03()) > 0) {
            return 1;
        } else {
            return -1;
        }
    }
}
