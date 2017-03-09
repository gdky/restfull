package com.gdky.restfull.service;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gdky.restfull.dao.UploadDao;

@Service
@Transactional
public class UploadService {

	@Resource
	private UploadDao uploadDao;
	
	public void insertfile(Map<String,Object> fj) throws Exception{
		if(uploadDao.checkFileUrl((String) fj.get("uploadUrl"))){
			throw new Exception("文件已存在，请勿重复添加");
		}
		uploadDao.insertfile(fj);
	}
	
	public void delfile(String url){
		uploadDao.delfile(url);
	}
	public String getFileName(String url) throws Exception{
		url = java.net.URLDecoder.decode(url, "UTF-8");
		return uploadDao.getFileName(url);
	}
	
}
