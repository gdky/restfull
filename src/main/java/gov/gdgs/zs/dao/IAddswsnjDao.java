package gov.gdgs.zs.dao;

import java.util.Map;

public interface IAddswsnjDao {
	
	public String addSwsnjb( Map <String,Object >obj) throws Exception;
	
	/**
	 * 更新事务所年检表
	 * @param obj
	 */
	public void updateSwsnjb(Map <String,Object >obj);
	
}
