package com.grand.gt_wms.bean;

/**仓库退料单列表实体类
 * 
 * @author Administrator
 *
 */
public class WhrBox {
	/*仓退单号*/
    private String rvv01;
	/*项次*/
    private String rvv02;
	/*收货单号*/
    private String rvv04;
	/*收货项次*/
    private String rvv05;
	/*料号*/
    private String rvv31;
	/*品名*/
    private String ima02;
	/*规格*/
    private String ima021;
	/*仓退数量*/
    private String rvv17;
	/*计价单位*/
    private String rvv86;
	/*计价数量*/
    private String rvv87;
	/*仓库*/
    private String rvv32;
	/*储位*/
    private String rvv33;
	/*批号*/
    private String rvv34;
	/*仓退条码*/
    private String rvbs04;
	/*仓退条码数量*/
    private String rvbs06;
	/**标记是否被扫描*/
	private boolean ifScanedBox = false;

	public String getRvv01() {
		return rvv01;
	}
	public void setRvv01(String rvv01) {
		this.rvv01 = rvv01;
	}
	public String getRvv02() {
		return rvv02;
	}
	public void setRvv02(String rvv02) {
		this.rvv02 = rvv02;
	}
	public String getRvv04() {
		return rvv04;
	}
	public void setRvv04(String rvv04) {
		this.rvv04 = rvv04;
	}
	public String getRvv05() {
		return rvv05;
	}
	public void setRvv05(String rvv05) {
		this.rvv05 = rvv05;
	}
	public String getRvv31() {
		return rvv31;
	}
	public void setRvv31(String rvv31) {
		this.rvv31 = rvv31;
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
	public String getRvv17() {
		return rvv17;
	}
	public void setRvv17(String rvv17) {
		this.rvv17 = rvv17;
	}
	public String getRvv86() {
		return rvv86;
	}
	public void setRvv86(String rvv86) {
		this.rvv86 = rvv86;
	}
	public String getRvv87() {
		return rvv87;
	}
	public void setRvv87(String rvv87) {
		this.rvv87 = rvv87;
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
	public String getRvv34() {
		return rvv34;
	}
	public void setRvv34(String rvv34) {
		this.rvv34 = rvv34;
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
}
