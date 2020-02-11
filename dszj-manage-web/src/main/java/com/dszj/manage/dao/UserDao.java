package com.dszj.manage.dao;

import com.dszj.manage.base.BaseDao;
import com.dszj.manage.entity.User;

public interface UserDao extends BaseDao<User> {

    public User findByUsernameAndPassword(String username,String password);


}
