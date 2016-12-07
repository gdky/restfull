package com.gdky.restfull.dao;

import java.util.List;
import java.util.Map;

public class MessageDao extends BaseJdbcDao{
	
	public List<Map<String,Object>> getSendBox (){
		StringBuffer sb = new StringBuffer();
		sb.append(" select mt.* ");
		sb.append(" from  fw_msg_text mt,fw_msg_log ml ");
		sb.append(" where ml.TEXTID = mt.ID ");
		sb.append(" and ml.SENDID ");
		sb.append(" and mt.TITLE like ");
		
		return null;
	}
}
