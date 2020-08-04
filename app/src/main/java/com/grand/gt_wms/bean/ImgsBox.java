/**
  * Copyright 2018 bejson.com 
  */
package com.grand.gt_wms.bean;

/**条码库存实体类
 * 
 * @author Administrator
 *
 */
public class ImgsBox {
	/*料号*/
    private String imgs01;
	/*品名*/
    private String ima02;
	/*规格*/
    private String ima021;
	/*仓库*/
    private String imgs02;
	/*储位*/
    private String imgs05;
	/*条码*/
    private String imgs06;
	/*库存单位*/
    private String imgs07;
	/*数量*/
    private String imgs08;
	/*制造日期*/
    private String imgs09;
	/*归属运营中心*/
    private String imgsplant;
	/*拨入库位*/
	private String imn15="";
	/*拨入储位*/
	private String imn16="";
	/**
	 * 标记是否被扫描
	 */
	private boolean ifScanedBox = false;

	public String getImgs01() {
		return imgs01;
	}
	public void setImgs01(String imgs01) {
		this.imgs01 = imgs01;
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
	public String getImgs02() {
		return imgs02;
	}
	public void setImgs02(String imgs02) {
		this.imgs02 = imgs02;
	}
	public String getImgs05() {
		return imgs05;
	}
	public void setImgs05(String imgs05) {
		this.imgs05 = imgs05;
	}
	public String getImgs06() {
		return imgs06;
	}
	public void setImgs06(String imgs06) {
		this.imgs06 = imgs06;
	}
	public String getImgs07() {
		return imgs07;
	}
	public void setImgs07(String imgs07) {
		this.imgs07 = imgs07;
	}
	public String getImgs08() {
		return imgs08;
	}
	public void setImgs08(String imgs08) {
		this.imgs08 = imgs08;
	}
	public String getImgs09() {
		return imgs09;
	}
	public void setImgs09(String imgs09) {
		this.imgs09 = imgs09;
	}
	public String getImgsplant() {
		return imgsplant;
	}
	public void setImgsplant(String imgsplant) {
		this.imgsplant = imgsplant;
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

	public boolean isIfScanedBox() {
		return ifScanedBox;
	}

	public void setIfScanedBox(boolean ifScanedBox) {
		this.ifScanedBox = ifScanedBox;
	}
}