package cn.xvkang.service;

import java.util.List;
import java.util.Map;

import cn.xvkang.entity.Kemuzuhe;

public interface KemuzuheService {
	
	public void add(Kemuzuhe p);

	public Kemuzuhe findById(Integer id);
	public Kemuzuhe findByCode(String code);


	public void deleteById(Integer id);
	public Map<String,List<Kemuzuhe>> findAllByCategory();

	
}
