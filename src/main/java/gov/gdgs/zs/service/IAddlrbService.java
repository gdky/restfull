package gov.gdgs.zs.service;

import java.util.Map;





public interface IAddlrbService {
	
	public Map<String,Object> addLrb ( Map <String,Object> obj);
	public void updateLrb(Map<String,Object> obj);	
	public Map<String,Object> addLrfpb ( Map <String,Object> obj);
	public void updateLrfpb(Map<String,Object> obj);
	public Map<String, Object> checkLrb(String jgid, String where);
	public Map<String, Object> getLrfpbJgxx(String jgid, String where);
	public Map<String, Object> checkLrfpb(String jgid, String where);

}

