package com.grand.gt_wms.bean;

public class ImptAndEmptItem implements Comparable<ImptAndEmptItem>{
	/*料号*/
    private String tlf01;
	/*仓库*/
    private String tlf902;
	/*入出库码*/
    private String tlf907;
	/*异动数量*/
    private String tlf10;
	/*扣账时间*/
    private String tlf06;
	/*操作人员工号*/
    private String tlf09;
	/*操作人员姓名*/
    private String gen02;
	/*品名*/
    private String ima02;
	/*规格*/
    private String ima021;
	/*当前库存*/
	private String img10;
	public String getTlf01() {
		return tlf01;
	}
	public void setTlf01(String tlf01) {
		this.tlf01 = tlf01;
	}
	public String getTlf902() {
		return tlf902;
	}
	public void setTlf902(String tlf902) {
		this.tlf902 = tlf902;
	}
	public String getTlf907() {
		return tlf907;
	}
	public void setTlf907(String tlf907) {
		this.tlf907 = tlf907;
	}
	public String getTlf10() {
		return tlf10;
	}
	public void setTlf10(String tlf10) {
		this.tlf10 = tlf10;
	}
	public String getTlf06() {
		return tlf06;
	}
	public void setTlf06(String tlf06) {
		this.tlf06 = tlf06;
	}
	public String getTlf09() {
		return tlf09;
	}
	public void setTlf09(String tlf09) {
		this.tlf09 = tlf09;
	}
	public String getGen02() {
		return gen02;
	}
	public void setGen02(String gen02) {
		this.gen02 = gen02;
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
	public String getImg10() {return img10;}
	public void setImg10(String img10) {this.img10 = img10;}

	/**
	 * 物料号排序
	 * @param item
	 * @return
	 */
	@Override
	public int compareTo(ImptAndEmptItem item) {
		long year1 = 0;
		long month1 = 0;
		long day1 = 0;
		long year2 = 0;
		long month2 = 0;
		long day2 = 0;
		year1 = Long.parseLong(this.getTlf06().substring(0,4));
		month1 = Long.parseLong(this.getTlf06().substring(5,7));
		day1 = Long.parseLong(this.getTlf06().substring(8,10));
		year2 = Long.parseLong(item.getTlf06().substring(0,4));
		month2 = Long.parseLong(item.getTlf06().substring(5,7));
		day2 = Long.parseLong(item.getTlf06().substring(8,10));
		if(year1-year2>0){
			return -1;
		}else if(year1-year2==0){
			if(month1-month2>0){
				return -1;
			}else if(month1-month2==0){
				if(day1-day2>0){
					return -1;
				}else {
					return 1;
				}
			}else {
				return 1;
			}
		}else {
			return 1;
		}
	}

}
