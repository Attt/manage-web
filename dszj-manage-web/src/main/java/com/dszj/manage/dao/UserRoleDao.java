package com.dszj.manage.dao;

import java.util.List;

import com.dszj.manage.base.BaseDao;
import com.dszj.manage.entity.UserRole;

public interface UserRoleDao extends BaseDao<UserRole> {

     List<UserRole> findByRoleId(Integer roleId);
     List<UserRole> findByUserId(Integer userId);

}
