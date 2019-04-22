package cn.xvkang.service;





import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.Resource;

import cn.xvkang.entity.TXms;
import cn.xvkang.utils.page.Page;


public interface TxmsService {

	public Page<TXms> table(String name,Integer pageNum, Integer pageSize);

	public Map<String,Object> importExcel(String xmid, String deleteOrigin, InputStream inputStream);
	
	public List< String> getSchoolsByXmid(String xmid);
	
	public List< String> getClassesByXmidAndSchool(String xmid,String school);

	public Resource exportBjRank(String xmid, String school, String bj);

	public Resource exportSubPercent(String xmid, String school);



}
