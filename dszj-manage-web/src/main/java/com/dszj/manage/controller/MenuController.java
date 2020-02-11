package com.dszj.manage.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.dszj.manage.base.BaseController;
import com.dszj.manage.base.ResultVO;
import com.dszj.manage.entity.Menu;
import com.dszj.manage.enums.MenuIsLeafEnum;
import com.dszj.manage.enums.MenuTypeEnum;
import com.dszj.manage.exception.BizException;
import com.dszj.manage.exception.BizExceptionEnum;
import com.dszj.manage.form.MenuQueryForm;
import com.dszj.manage.form.MeunAddForm;
import com.dszj.manage.form.MeunEditForm;
import com.dszj.manage.service.MenuService;
import com.dszj.manage.utils.BeanCopyUtil;
import com.dszj.manage.vo.MenuSelectVO;
import com.dszj.manage.vo.MenuTreeVO;

/**
 * 菜单管理
 * @author yys
 */
@Controller
@RequestMapping("/menu")
@SuppressWarnings("rawtypes")
public class MenuController extends BaseController {
	@Autowired
    private MenuService menuService;
	
    @GetMapping("/toList")
    public String toList(){
        return "back/system/menu/menu-list";
    }
    
	@GetMapping("/getPageList")
	@ResponseBody
	public ResultVO getPageList(@Valid MenuQueryForm form, BindingResult bindingResult) {
		
		// 参数验证
		if (bindingResult.hasErrors()) {
			throw new BizException(BizExceptionEnum.PARAM_ERROR,
					bindingResult.getFieldError().getDefaultMessage());
		}
		// 获取用户列表
		Page<Menu> page = menuService.findPageList(form);
		
		return success(page);
	}
    
	 /**
	  * 菜单树（不包含资源）
	  * @return
	  * @author yys
	  */
	@GetMapping("/getMenuTree")
	@ResponseBody
	public ResultVO getMenuTree(Integer roleId) {
		
		List<Menu> menus =  menuService.findAllBySort();
		List<MenuTreeVO> allMenus = new ArrayList<>();
		if (roleId!=null) {
			List<Menu> roleMenus =  menuService.findByRoleId(roleId);
			for (Menu menu : menus) {
				for (Menu roleMenu : roleMenus) {
					//设置子节点为选中状态
					if (menu.getId().equals(roleMenu.getId()) && MenuIsLeafEnum.YES.getCode().equals(menu.getIsLeaf())) {
						menu.setChecked(true);
					}
				}
			}
		}
		
		for (Menu menu : menus) {
			MenuTreeVO menuTreeVO = new MenuTreeVO();
			menuTreeVO.setId(menu.getId());
			menuTreeVO.setParentId(menu.getParentId());
			menuTreeVO.setTitle(menu.getName());
			menuTreeVO.setType(menu.getType());
			menuTreeVO.setChecked(menu.isChecked());
			if (menu.getType().equals(MenuTypeEnum.MODE_MENU.getCode())||menu.getType().equals(MenuTypeEnum.ONE_MENU.getCode())) {
				menuTreeVO.setSpread(true);
			}
			
			if (roleId!=null && menu.getType().equals(MenuTypeEnum.TWO_MENU.getCode())) {
				menuTreeVO.setSpread(true);
			}
			
			allMenus.add(menuTreeVO);
		}
		
		//树形菜单根节点
		MenuTreeVO treeData = new MenuTreeVO();
		genMenuTree(allMenus, treeData);

		// 当前只获取第一个模块下的菜单列表
		List<MenuTreeVO> treeMenu = treeData.getChildren();
		return success(treeMenu);
	}
	
	/**
	 * 递归查找children
	 * @param allMenus
	 * @param treeData
	 * @author yys
	 */
	private void genMenuTree(List<MenuTreeVO> allMenus,MenuTreeVO treeData){
		
		List<MenuTreeVO> children = new ArrayList<>();
		
		for (MenuTreeVO menuTreeVO : allMenus) {
			
			if (menuTreeVO.getType().equals(MenuTypeEnum.RESOURCE.getCode())) {
				continue;
			}
			
			if (menuTreeVO.getType().equals(MenuTypeEnum.MODE_MENU.getCode())) {
				//菜单根节点
				if (treeData.getType()==null) {
					children.add(menuTreeVO);
				}
			}
			
			
			if (menuTreeVO.getType().equals(MenuTypeEnum.ONE_MENU.getCode())||menuTreeVO.getType().equals(MenuTypeEnum.TWO_MENU.getCode())||menuTreeVO.getType().equals(MenuTypeEnum.OPERATE.getCode())) {
				if (menuTreeVO.getParentId().equals(treeData.getId())) {
					children.add(menuTreeVO);
				}
			}
		}
		
		for (MenuTreeVO menu : children) {
			genMenuTree(allMenus, menu);
		}
		
		treeData.setChildren(children);
		
	}
	
	
	/**
	 * 查询菜单下拉列表
	 * @return
	 * @author yys
	 */
	@GetMapping("/getSelectParentTree")
	@ResponseBody
	public List<MenuSelectVO> getSelectParentTree() {
		
		List<Menu> menus =  menuService.findAllBySort();
		List<MenuSelectVO> allMenus = new ArrayList<>();
		for (Menu menu : menus) {
			MenuSelectVO menuSelectVO = new MenuSelectVO();
			menuSelectVO.setId(menu.getId());
			menuSelectVO.setParentId(menu.getParentId());
			menuSelectVO.setName(menu.getName());
			menuSelectVO.setType(menu.getType());
//			if (menu.getType().equals(MenuTypeEnum.ONE_MENU.getCode())||menu.getType().equals(MenuTypeEnum.TWO_MENU.getCode())) {
//				menuSelectVO.setOpen(true);
//			}
			allMenus.add(menuSelectVO);
		}
		
		//树形菜单根节点
		MenuSelectVO treeData = new MenuSelectVO();
		genSelectParentTree(allMenus, treeData);
		
		// 当前只获取第一个模块下的菜单列表
		List<MenuSelectVO> treeMenu = treeData.getChildren();
		
		return treeMenu;
	}
	
	/**
	 * 递归查询菜单下拉列表children
	 * @param allMenus
	 * @param treeData
	 * @author yys
	 */
	private void genSelectParentTree(List<MenuSelectVO> allMenus,MenuSelectVO treeData){
		
		List<MenuSelectVO> children = new ArrayList<>();
		
		for (MenuSelectVO menuSelectVO : allMenus) {
			
			if (menuSelectVO.getType().equals(MenuTypeEnum.RESOURCE.getCode())) {
				continue;
			}
			
			if (menuSelectVO.getType().equals(MenuTypeEnum.MODE_MENU.getCode())) {
				//菜单根节点
				if (treeData.getType()==null) {
					children.add(menuSelectVO);
				}
			}
			
			
			if (menuSelectVO.getType().equals(MenuTypeEnum.ONE_MENU.getCode())||menuSelectVO.getType().equals(MenuTypeEnum.TWO_MENU.getCode())||menuSelectVO.getType().equals(MenuTypeEnum.OPERATE.getCode())) {
				if (menuSelectVO.getParentId().equals(treeData.getId())) {
					children.add(menuSelectVO);
				}
			}
		}
		
		for (MenuSelectVO menu : children) {
			genSelectParentTree(allMenus, menu);
		}
		
		if (children.size()>0) {
			treeData.setChildren(children);
			treeData.setParent(true);
			//不显示操作
			if (MenuTypeEnum.TWO_MENU.getCode().equals(treeData.getType())) {
				treeData.setOpen(false);
			}else{
				treeData.setOpen(true);
			}
			
		}else{
			treeData.setOpen(false);
			treeData.setParent(false);
		}
		
		
		
	}
	
	
    @GetMapping("/toAdd")
    public String toAdd(){
        return "back/system/menu/menu-add";
    }

    @GetMapping("toEdit")
    public ModelAndView toEdit(Integer id){
    	ModelAndView mv = new ModelAndView();
    	mv.addObject("menu", menuService.findById(id));
    	mv.setViewName("back/system/menu/menu-edit");
        return mv;
    }
    
    
    @PostMapping("/add")
	@ResponseBody
	public ResultVO add(@Valid @RequestBody MeunAddForm form,BindingResult bindingResult) {
    	
    	validateForm(bindingResult);
    	
    	if ((MenuTypeEnum.MODE_MENU.getCode().equals(form.getType()))) {
			throw new BizException(BizExceptionEnum.PARAM_ERROR, "当前只允许新增一个模块菜单");
		}
    	
    	Menu menu = BeanCopyUtil.convertBean(form, Menu.class);
		// 保存数据
		menuService.save(menu);
		return success();
	}
	
	@PostMapping("/edit")
	@ResponseBody
	public ResultVO edit(@Valid @RequestBody MeunEditForm form,BindingResult bindingResult) {
		
		validateForm(bindingResult);
		
		if (form.getId().equals(form.getParentId())) {
			throw new BizException(BizExceptionEnum.PARAM_ERROR, "父级菜单不能选择自己");
		}
		Menu menu = menuService.findById(form.getId());
		BeanCopyUtil.copyNotNullProperties(form, menu);
		// 保存数据
		menuService.update(menu);
		return success();
	}
	
    @GetMapping("toDetail")
    public ModelAndView toDetail(Integer id){
    	ModelAndView mv = new ModelAndView();
    	mv.addObject("menu", menuService.findById(id));
    	mv.setViewName("back/system/menu/menu-detail");
        return mv;
    }
    
	@GetMapping("/delete")
	@ResponseBody
	public ResultVO delete(@RequestParam("ids") List<Integer> ids) {
		if (ids.contains(1)) {
			throw new BizException(BizExceptionEnum.PARAM_ERROR, "被删除项中不能包含系统菜单");
		}
		menuService.deleteByIdIn(ids);
		return success();
	}
}
