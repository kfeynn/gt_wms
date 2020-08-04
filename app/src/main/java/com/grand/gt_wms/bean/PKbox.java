/**
  * Copyright 2018 bejson.com 
  */
package com.grand.gt_wms.bean;

/**
 * Auto-generated: 2018-10-23 10:18:42
 *
 */
public class PKbox implements Comparable<PKbox>{
	/*发料单号*/
    private String sfp01;
	/*项次*/
    private String sfs02;
	/*工单单号*/
    private String sfs03;
	/*料号*/
    private String sfs04;
	/*品名*/
    private String ima02;
	/*规格*/
    private String ima021;
	/*发料数量*/
    private String sfs05;
	/*仓库*/
    private String sfs07;
	/*储位*/
    private String sfs08;
	/*批号*/
    private String sfs09;
	/*发料条码*/
    private String rvbs04;
	/*发料条码数量*/
    private String rvbs06;
	/**标记是否被扫描*/
	private boolean ifScanedBox = false;

	//杂发料单属性
	/*发料单号*/
	private String inb01;
	/*项次*/
	private String inb03;
	/*料号*/
	private String inb04;
	/*发料数量*/
	private String inb09;
	/*仓库*/
	private String inb05;
	/*储位*/
	private String inb06;
	/*批号*/
	private String inb07;

	public String getSfp01() {
		return sfp01;
	}
	public void setSfp01(String sfp01) {
		this.sfp01 = sfp01;
	}
	public String getSfs02() {
		return sfs02;
	}
	public void setSfs02(String sfs02) {
		this.sfs02 = sfs02;
	}
	public String getSfs03() {
		return sfs03;
	}
	public void setSfs03(String sfs03) {
		this.sfs03 = sfs03;
	}
	public String getSfs04() {
		return sfs04;
	}
	public void setSfs04(String sfs04) {
		this.sfs04 = sfs04;
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
	public String getSfs05() {
		return sfs05;
	}
	public void setSfs05(String sfs05) {
		this.sfs05 = sfs05;
	}
	public String getSfs07() {
		return sfs07;
	}
	public void setSfs07(String sfs07) {
		this.sfs07 = sfs07;
	}
	public String getSfs08() {
		return sfs08;
	}
	public void setSfs08(String sfs08) {
		this.sfs08 = sfs08;
	}
	public String getSfs09() {
		return sfs09;
	}
	public void setSfs09(String sfs09) {
		this.sfs09 = sfs09;
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

	public String getInb01() {
		return inb01;
	}

	public void setInb01(String inb01) {
		this.inb01 = inb01;
	}

	public String getInb03() {
		return inb03;
	}

	public void setInb03(String inb03) {
		this.inb03 = inb03;
	}

	public String getInb04() {
		return inb04;
	}

	public void setInb04(String inb04) {
		this.inb04 = inb04;
	}

	public String getInb09() {
		return inb09;
	}

	public void setInb09(String inb09) {
		this.inb09 = inb09;
	}

	public String getInb05() {
		return inb05;
	}

	public void setInb05(String inb05) {
		this.inb05 = inb05;
	}

	public String getInb06() {
		return inb06;
	}

	public void setInb06(String inb06) {
		this.inb06 = inb06;
	}

	public String getInb07() {
		return inb07;
	}

	public void setInb07(String inb07) {
		this.inb07 = inb07;
	}

	@Override
	public String toString() {
		return "PKbox [sfp01=" + sfp01 + ", sfs02=" + sfs02 + ", sfs03="
				+ sfs03 + ", sfs04=" + sfs04 + ", ima02=" + ima02 + ", ima021="
				+ ima021 + ", sfs05=" + sfs05 + ", sfs07=" + sfs07 + ", sfs08="
				+ sfs08 + ", sfs09=" + sfs09 + ", rvbs04=" + rvbs04
				+ ", rvbs06=" + rvbs06 + "]";
	}

	/**
	 * 物料号排序
	 * @param pKbox
	 * @return
	 */
	@Override
	public int compareTo(PKbox pKbox) {
		if(Long.parseLong(this.sfs04) - Long.parseLong(pKbox.getSfs04())>0){
			return 1;
		}else {
			return -1;
		}
	}
}