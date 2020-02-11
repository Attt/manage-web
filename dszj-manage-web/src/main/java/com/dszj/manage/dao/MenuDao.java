package com.dszj.manage.dao;

import java.util.List;

import com.dszj.manage.base.BaseDao;
import com.dszj.manage.entity.Menu;

public interface MenuDao extends BaseDao<Menu>{
	
	List<Menu> findByParentIdIn(List<Integer> parentIds);
	
	List<Menu> findByParentId(Integer parentId);
	
	List<Menu> findByParentIdInAndType(List<Integer> parentIds,Integer type);
	
	List<Menu> findByIdInOrderByTypeAscOrderNumAsc(List<Integer> parentIds);
}
