package com.gdky.restfull.dao;

import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class UploadDao extends BaseJdbcDao {
	
	
	public boolean checkFileUrl(String url){
		return this.jdbcTemplate.queryForList("select URL from fw_filename_url where URL='"+url.replace("\"", "")+"' and YXBZ='1' ").size()>0;
	}
	public void insertfile(Map<String,Object> fj){
		this.jdbcTemplate.update("insert into fw_filename_url (FILENAME,URL,LRRQ,YXBZ) values(?,?,now(),'1')",new Object[]{fj.get("fileName"),fj.get("uploadUrl")});
	}
	public void delfile(String old){
		this.jdbcTemplate.update("update fw_filename_url set YXBZ='0' where URL='"+old.replace("\"", "")+"' ");
	}
	public String getFileName(String url){
	  return this.jdbcTemplate.queryForObject("select t.filename from fw_filename_url t where url=? and yxbz='1' limit 1",new Object[]{url}, String.class);
	}

}
