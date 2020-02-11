package com.dszj.manage.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dszj.manage.base.BaseService;
import com.dszj.manage.dao.MenuDao;
import com.dszj.manage.dao.RoleDao;
import com.dszj.manage.dao.RoleMenuDao;
import com.dszj.manage.dao.UserDao;
import com.dszj.manage.dao.UserRoleDao;
import com.dszj.manage.entity.Menu;
import com.dszj.manage.entity.Role;
import com.dszj.manage.entity.RoleMenu;
import com.dszj.manage.entity.User;
import com.dszj.manage.entity.UserRole;
import com.dszj.manage.enums.MenuTypeEnum;

@Service
public class RoleService extends BaseService<Role> {

    @Autowired
    private MenuDao menuDao;
    @Autowired
    private UserRoleDao userRoleDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleMenuDao roleMenuDao;
    @Autowired
    private RoleDao roleDao;

    
    @Transactional
    public void auth(Integer roleId,List<Integer> menuIds) {
    	
    	//追加操作下的接口资源
    	List<Menu> resources =  menuDao.findByParentIdInAndType(menuIds,MenuTypeEnum.RESOURCE.getCode());
    	for (Menu menu : resources) {
    		menuIds.add(menu.getId());
		}
    	List<RoleMenu> oldRoleMenu = roleMenuDao.findByRoleId(roleId);
    	List<RoleMenu> newRoleMenu =  new ArrayList<>();
    	menuIds.forEach(menuId->{
    		RoleMenu roleMenu =new RoleMenu();
    		roleMenu.setRoleId(roleId);
    		roleMenu.setMenuId(menuId);
    		newRoleMenu.add(roleMenu);
    	});
    	
    	roleMenuDao.deleteAll(oldRoleMenu);
    	roleMenuDao.saveAll(newRoleMenu);
    }
    
    public List<User> getUserListByRoleId(Integer roleId) {
    	List<UserRole> userRoleList =  userRoleDao.findByRoleId(roleId);
    	List<Integer> userIds =  new ArrayList<>();
    	userRoleList.forEach(userRole -> userIds.add(userRole.getUserId()));
    	return userDao.findByIdIn(userIds);
    }
    
    public List<Role> findByUserId(Integer userId) {
    	List<UserRole> userRoleList =  userRoleDao.findByUserId(userId);
    	List<Integer> roleIds =  new ArrayList<>();
    	userRoleList.forEach(userRole -> roleIds.add(userRole.getRoleId()));
    	return roleDao.findByIdIn(roleIds);
    }
    

}
