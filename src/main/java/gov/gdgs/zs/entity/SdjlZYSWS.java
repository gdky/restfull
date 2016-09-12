package gov.gdgs.zs.entity;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 机构锁定记录
 * @author ming
 *
 */
public class SdjlZYSWS implements Serializable {

	private static final long serialVersionUID = 946074415689826397L;
	private Integer id;
	private Integer zyswsId;
	private String sdyy;
	private Integer sdrId;
	private Integer sdrRole;
	private Timestamp sdTime;
	private Integer jsrId;
	private Integer jsrRole;
	private Timestamp jsTime;
	private Boolean yxbz;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getSdyy() {
		return sdyy;
	}
	public void setSdyy(String sdyy) {
		this.sdyy = sdyy;
	}
	public Integer getSdrId() {
		return sdrId;
	}
	public void setSdrId(Integer sdrId) {
		this.sdrId = sdrId;
	}
	public Integer getSdrRole() {
		return sdrRole;
	}
	public void setSdrRole(Integer sdrRole) {
		this.sdrRole = sdrRole;
	}
	public Timestamp getSdTime() {
		return sdTime;
	}
	public void setSdTime(Timestamp sdTime) {
		this.sdTime = sdTime;
	}
	public Integer getJsrId() {
		return jsrId;
	}
	public void setJsrId(Integer jsrId) {
		this.jsrId = jsrId;
	}
	public Integer getJsrRole() {
		return jsrRole;
	}
	public void setJsrRole(Integer jsrRole) {
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
	public Integer getZyswsId() {
		return zyswsId;
	}
	public void setZyswsId(Integer zyswsId) {
		this.zyswsId = zyswsId;
	}
}
