package gov.gdgs.zs.dao;

import java.util.HashMap;
import java.util.Map;

public interface ILrbDao {

	public String addLrb(Map<String, Object> obj);

	public void updateLrb(Map<String, Object> obj);

	public String addLrfpb(Map<String, Object> obj);

	public void updateLrfpb(Map<String, Object> obj);

	public Map<String, Object> checkLrb(String jgid, HashMap<String, Object> map);

	public Map<String, Object> getLrfpbJgxx(String jgid,
			HashMap<String, Object> map);

	public Map<String, Object> checkLrfpb(String jgid,
			HashMap<String, Object> map);
}
