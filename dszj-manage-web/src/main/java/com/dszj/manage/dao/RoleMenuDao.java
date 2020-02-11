package com.dszj.manage.dao;

import java.util.List;

import com.dszj.manage.base.BaseDao;
import com.dszj.manage.entity.RoleMenu;

public interface RoleMenuDao extends BaseDao<RoleMenu> {
	
	List<RoleMenu> findByRoleId(Integer roleId);
	List<RoleMenu> findByRoleIdIn(List<Integer> roleIds);
}
