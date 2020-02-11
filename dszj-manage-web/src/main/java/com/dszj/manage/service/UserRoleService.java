package com.dszj.manage.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dszj.manage.base.BaseService;
import com.dszj.manage.dao.UserRoleDao;
import com.dszj.manage.entity.UserRole;

@Service
public class UserRoleService extends BaseService<UserRole> {
    @Autowired
    private UserRoleDao userRoleDao;
    
    
    public List<UserRole> findByUserId(Integer userId){
    	return userRoleDao.findByUserId(userId);
    }

}
