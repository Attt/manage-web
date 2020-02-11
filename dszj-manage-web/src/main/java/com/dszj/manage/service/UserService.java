package com.dszj.manage.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dszj.manage.base.BaseService;
import com.dszj.manage.dao.UserDao;
import com.dszj.manage.dao.UserRoleDao;
import com.dszj.manage.entity.User;
import com.dszj.manage.entity.UserRole;
import com.dszj.manage.enums.GloableParamEnum;
import com.dszj.manage.form.UserAddForm;
import com.dszj.manage.form.UserEditForm;
import com.dszj.manage.utils.BeanCopyUtil;

@Service
public class UserService extends BaseService<User>{

    @Autowired
    private UserDao userDao;
    @Autowired
    private UserRoleDao userRoleDao;
    
    public User findByUsernameAndPassword(String username,String password) {
        return userDao.findByUsernameAndPassword(username,password);
    }
    
    @Transactional
	public User save(UserAddForm form) {
		User user = userDao.save(BeanCopyUtil.convertBean(form, User.class));
		user.setHeadPic(GloableParamEnum.DEFAULT_HEAD_PIC.getValue());
		List<Integer> roleIds = form.getRoleIds();
		List<UserRole> userRoles = new ArrayList<>();
		for (Integer roleId : roleIds) {
			UserRole userRole = new UserRole();
			userRole.setRoleId(roleId);
			userRole.setUserId(user.getId());
			userRoles.add(userRole);
		}
		userRoleDao.saveAll(userRoles);
		return user;
	}
    
	@Transactional
	public User update(UserEditForm form) {
		User user = this.findById(form.getId());
		BeanCopyUtil.copyNotNullProperties(form, user);
		List<Integer> roleIds = form.getRoleIds();
		if (roleIds != null && roleIds.size() > 0) {
			List<UserRole> oldRoles = userRoleDao.findByUserId(form.getId());
			List<UserRole> userRoles = new ArrayList<>();
			for (Integer roleId : roleIds) {
				UserRole userRole = new UserRole();
				userRole.setRoleId(roleId);
				userRole.setUserId(user.getId());
				userRoles.add(userRole);
			}
			userRoleDao.saveAll(userRoles);
			userRoleDao.deleteInBatch(oldRoles);
		}

		userDao.save(user);
		return user;
	}
    
    

}
