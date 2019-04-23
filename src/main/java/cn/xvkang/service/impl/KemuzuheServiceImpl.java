package cn.xvkang.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.map.LinkedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.xvkang.dao.KemuzuheMapper;
import cn.xvkang.entity.Kemuzuhe;
import cn.xvkang.entity.KemuzuheExample;
import cn.xvkang.service.KemuzuheService;

@Service
public class KemuzuheServiceImpl implements KemuzuheService {
	@Autowired
	private KemuzuheMapper kemuzuheMapper;
	

	@Override
	public void deleteById(Integer id) {
		kemuzuheMapper.deleteByPrimaryKey(id);

	}

	@Override
	public void add(Kemuzuhe p) {
		kemuzuheMapper.insert(p);

	}

	@Override
	public Kemuzuhe findById(Integer id) {
		return kemuzuheMapper.selectByPrimaryKey(id);
	}

	@Override
	public Map<String, List<Kemuzuhe>> findAllByCategory() {
		Map<String, List<Kemuzuhe>> result = new LinkedMap<String, List<Kemuzuhe>>();
		KemuzuheExample categoryExample = new KemuzuheExample();
		categoryExample.createCriteria().andPidEqualTo(0);
		categoryExample.setOrderByClause(" sort asc ");
		List<Kemuzuhe> category = kemuzuheMapper.selectByExample(categoryExample);
		for (Kemuzuhe kemuzuhe : category) {
			List<Kemuzuhe> list = result.get(kemuzuhe.getName());
			if (list == null) {
				list = new ArrayList<>();
				result.put(kemuzuhe.getName(), list);
			}
			Integer id = kemuzuhe.getId();
			KemuzuheExample childExample = new KemuzuheExample();
			childExample.createCriteria().andPidEqualTo(id);
			childExample.setOrderByClause(" sort asc ");
			List<Kemuzuhe> child = kemuzuheMapper.selectByExample(childExample);
			list.addAll(child);
		}
		return result;
	}

	@Override
	public Kemuzuhe findByCode(String code) {
		KemuzuheExample example = new KemuzuheExample();
		example.createCriteria().andCodeEqualTo(code);
		List<Kemuzuhe> category = kemuzuheMapper.selectByExample(example);
		if(category.size()==1) {
			return category.get(0);
		}
		return null;
	}

}
