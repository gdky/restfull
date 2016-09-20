package gov.gdgs.zs.entity;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 税务师资质锁定记录
 * @author ming
 *
 */
public class SdjlZYSWS implements Serializable {

	private static final long serialVersionUID = 946074415689826397L;
	private Integer id;
	private Integer zyswsId;
	private String sdyy;
	private String sdr;
	private String sdrRole;
	private Timestamp sdTime;
	private String jsr;
	private String jsrRole;
	private Timestamp jsTime;
	private Boolean yxbz;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getZyswsId() {
		return zyswsId;
	}
	public void setZyswsId(Integer zyswsId) {
		this.zyswsId = zyswsId;
	}
	public String getSdyy() {
		return sdyy;
	}
	public void setSdyy(String sdyy) {
		this.sdyy = sdyy;
	}
	public String getSdr() {
		return sdr;
	}
	public void setSdr(String sdr) {
		this.sdr = sdr;
	}
	public String getSdrRole() {
		return sdrRole;
	}
	public void setSdrRole(String sdrRole) {
		this.sdrRole = sdrRole;
	}
	public Timestamp getSdTime() {
		return sdTime;
	}
	public void setSdTime(Timestamp sdTime) {
		this.sdTime = sdTime;
	}
	public String getJsr() {
		return jsr;
	}
	public void setJsr(String jsr) {
		this.jsr = jsr;
	}
	public String getJsrRole() {
		return jsrRole;
	}
	public void setJsrRole(String jsrRole) {
		this.jsrRole = jsrRole;
	}
	public Timestamp getJsTime() {
		return jsTime;
	}
	public void setJsTime(Timestamp jsTime) {
		this.jsTime = jsTime;
	}
	public Boolean getYxbz() {
		return yxbz;
	}
	public void setYxbz(Boolean yxbz) {
		this.yxbz = yxbz;
	}
	
}
