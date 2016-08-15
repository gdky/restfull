package gov.gdgs.zs.service;

import java.util.HashMap;
import java.util.Map;

import gov.gdgs.zs.dao.HfglDao;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@Transactional(rollbackFor=Exception.class)
public class HfglService {
	@Resource
	private HfglDao hfglDao;
	
	public Map<String, Object> hyhfjnqk(int pn, int ps, String where) throws Exception {
		HashMap<String, Object> map = new HashMap<String, Object>();
		if (where != null) {
			try {
				where = java.net.URLDecoder.decode(where, "UTF-8");
				ObjectMapper mapper = new ObjectMapper();
				map = mapper.readValue(where,
						new TypeReference<Map<String, Object>>() {
						});
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return hfglDao.hyhfjnqk(pn, ps, map);
	}
	public Map<String, Object> fpdy(int pn, int ps, String where) throws Exception {
		HashMap<String, Object> map = new HashMap<String, Object>();
		if (where != null) {
			try {
				where = java.net.URLDecoder.decode(where, "UTF-8");
				ObjectMapper mapper = new ObjectMapper();
				map = mapper.readValue(where,
						new TypeReference<Map<String, Object>>() {
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return hfglDao.fpdy(pn, ps, map);
	}
	public Map<String, Object> fzyhfjn(int pn, int ps, String where) throws Exception {
		HashMap<String, Object> map = new HashMap<String, Object>();
		if (where != null) {
			try {
				where = java.net.URLDecoder.decode(where, "UTF-8");
				ObjectMapper mapper = new ObjectMapper();
				map = mapper.readValue(where,
						new TypeReference<Map<String, Object>>() {
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return hfglDao.fzyhfjn(pn, ps, map);
	}
	public Map<String, Object> fytj( String where) throws Exception {
		HashMap<String, Object> map = new HashMap<String, Object>();
		if (where != null) {
			try {
				where = java.net.URLDecoder.decode(where, "UTF-8");
				ObjectMapper mapper = new ObjectMapper();
				map = mapper.readValue(where,
						new TypeReference<Map<String, Object>>() {
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return hfglDao.fytj( map);
	}
	
	public Object upLoadJFSC(MultipartFile file,String uid) throws Exception {
		return hfglDao.upLoadJFSC(file,uid);
	}
	public Object upLoadfzy(MultipartFile file,String uid) throws Exception {
		return hfglDao.upLoadfzy(file,uid);
	}
	
	public Object ttgefp(String jlid,Map<String, Object> fptj,String name) throws Exception {
		return hfglDao.ttgefp(jlid,fptj,name);
	}
	public Object fpjexg(String jlid,Map<String, Object> fptj,String name) throws Exception {
		return hfglDao.fpjexg(jlid,fptj,name);
	}
	public Object fzyxg(String jlid,Map<String, Object> fptj,String name) throws Exception {
		return hfglDao.fzyxg(jlid,fptj,name);
	}
	public Object fzyDel(String jlid,String name) throws Exception {
		return hfglDao.fzyDel(jlid,name);
	}
	public Object fzytj(Map<String, Object> fptj,String name) throws Exception {
		return hfglDao.fzytj(fptj,name);
	}
	public Object fpdylj(Object fptj) throws Exception {
		return hfglDao.fpdylj(fptj);
	}

}
