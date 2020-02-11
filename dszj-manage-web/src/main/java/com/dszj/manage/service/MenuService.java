package com.dszj.manage.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dszj.manage.base.BaseService;
import com.dszj.manage.dao.MenuDao;
import com.dszj.manage.dao.RoleMenuDao;
import com.dszj.manage.dao.UserRoleDao;
import com.dszj.manage.entity.Menu;
import com.dszj.manage.entity.RoleMenu;
import com.dszj.manage.entity.UserRole;
import com.dszj.manage.enums.MenuIsLeafEnum;
import com.dszj.manage.enums.MenuTypeEnum;
import com.dszj.manage.exception.BizException;
import com.dszj.manage.exception.BizExceptionEnum;

@Service
public class MenuService extends BaseService<Menu> {

    @Autowired
    private MenuDao menuDao;
    @Autowired
    private RoleMenuDao roleMenuDao;
    @Autowired
    private UserRoleDao userRoleDao;
    
    
    
	@Override
	@Transactional
	public Menu save(Menu entity) {
		Menu parentMenu =  this.findById(entity.getParentId());
		if (parentMenu.getType() >= entity.getType()) {
			throw new BizException(BizExceptionEnum.PARAM_ERROR,"菜单类型范围必须小于父级菜单类型范围");
		}
		
		if (parentMenu.getType().equals(MenuTypeEnum.MODE_MENU.getCode()) && entity.getType().equals(MenuTypeEnum.TWO_MENU.getCode())) {
			throw new BizException(BizExceptionEnum.PARAM_ERROR,"模块菜单下只能新增一级菜单或操作资源");
		}
		
		if (parentMenu.getType().equals(MenuTypeEnum.ONE_MENU.getCode()) && !entity.getType().equals(MenuTypeEnum.TWO_MENU.getCode())) {
			throw new BizException(BizExceptionEnum.PARAM_ERROR,"一级菜单下只能新增二级菜单");
		}
	
		return super.save(entity);
	}

	@Override
	@Transactional
	public Menu update(Menu entity) {
		
		//菜单类型范围验证
		
		if (entity.getParentId()==0 && (!MenuTypeEnum.MODE_MENU.getCode().equals(entity.getType()))) {
			throw new BizException(BizExceptionEnum.PARAM_ERROR, "系统菜单属于顶级菜单，菜单类型不允许编辑");
		}
		
		if (entity.getParentId()!=0) {
			Menu parentMenu =  this.findById(entity.getParentId());
			if (parentMenu.getType() >= entity.getType()) {
				throw new BizException(BizExceptionEnum.PARAM_ERROR,"菜单类型范围必须小于父级菜单类型范围");
			}
			
			if (parentMenu.getType().equals(MenuTypeEnum.MODE_MENU.getCode()) && entity.getType().equals(MenuTypeEnum.TWO_MENU.getCode())) {
				throw new BizException(BizExceptionEnum.PARAM_ERROR,"模块菜单下只能新增一级菜单或操作资源");
			}
			
			if (parentMenu.getType().equals(MenuTypeEnum.ONE_MENU.getCode()) && !entity.getType().equals(MenuTypeEnum.TWO_MENU.getCode())) {
				throw new BizException(BizExceptionEnum.PARAM_ERROR,"一级菜单下只能是二级菜单");
			}
		}
		
		//更新所有菜单叶子节点状态
		List<Menu> allMenus = this.findAll();
		for (Menu menu : allMenus) {
			Integer isLeaf = MenuIsLeafEnum.YES.getCode();
			for (Menu childMenu : allMenus) {
				//该菜单不是叶子节点
				if (menu.getId().equals(childMenu.getParentId()) && !(menu.getType().equals(MenuTypeEnum.RESOURCE.getCode())) && !(childMenu.getType().equals(MenuTypeEnum.RESOURCE.getCode())) ) {
					isLeaf = MenuIsLeafEnum.NO.getCode();
					break;
				}
			}
			menu.setIsLeaf(isLeaf);
		}
		
		this.save(allMenus);
		return super.update(entity);
	}


	public List<Menu> findByRoleId(Integer roleId) {
		List<RoleMenu> roleMenuList = roleMenuDao.findByRoleId(roleId);
		List<Integer> menuIds = new ArrayList<>();
		roleMenuList.forEach(roleMenu -> {
			menuIds.add(roleMenu.getMenuId());
		});
		List<Menu> menuList = menuDao.findByIdIn(menuIds);
		return menuList;

	}
	
	public List<Menu> findByUserId(Integer userId) {
		List<UserRole> userRoleList = userRoleDao.findByUserId(userId);
		List<Integer> roleIds = new ArrayList<>();
		for (UserRole userRole : userRoleList) {
			roleIds.add(userRole.getRoleId());
		}
		List<RoleMenu> roleMenuList = roleMenuDao.findByRoleIdIn(roleIds);
		List<Integer> menuIds = new ArrayList<>();
		for (RoleMenu roleMenu : roleMenuList) {
			menuIds.add(roleMenu.getMenuId());
		}
		List<Menu> menuList = menuDao.findByIdInOrderByTypeAscOrderNumAsc(menuIds);
		return menuList;

	}
	

    @Override
    @Transactional
	public void deleteByIdIn(List<Integer> ids) {
    	//子节点一并删除
		List<Menu> parentList = menuDao.findByParentIdIn(ids);
		for (Menu menu : parentList) {
			ids.add(menu.getId());
		}
		super.deleteByIdIn(ids);
	}

	public List<Menu> findAllBySort() {
        Sort sort = new Sort(Sort.Direction.ASC, "type", "orderNum");
        return menuDao.findAll(sort);
    }

}
