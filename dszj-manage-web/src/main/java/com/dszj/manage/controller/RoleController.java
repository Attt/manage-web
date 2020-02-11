package com.dszj.manage.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
import com.dszj.manage.entity.Role;
import com.dszj.manage.exception.BizException;
import com.dszj.manage.exception.BizExceptionEnum;
import com.dszj.manage.form.AuthForm;
import com.dszj.manage.form.RoleAddForm;
import com.dszj.manage.form.RoleEditForm;
import com.dszj.manage.form.RoleQueryForm;
import com.dszj.manage.service.MenuService;
import com.dszj.manage.service.RoleService;
import com.dszj.manage.utils.BeanCopyUtil;
import com.dszj.manage.vo.RoleSelectVO;

/**
 * 角色控制器
 * @author yys
 */
@Controller
@RequestMapping("/role")
@SuppressWarnings("rawtypes")
public class RoleController extends BaseController {
	 @Autowired
	    private RoleService roleService;

	    @Autowired
	    private MenuService menuService;

	    @GetMapping("/toList")
	    public String toList(ModelAndView mv,RoleQueryForm form){
	        return "back/system/role/role-list";
	    }
	    
		@GetMapping("/getPageList")
		@ResponseBody
		public ResultVO getPageList(@Valid RoleQueryForm form, BindingResult bindingResult) {
			
			// 参数验证
			if (bindingResult.hasErrors()) {
				throw new BizException(BizExceptionEnum.PARAM_ERROR,
						bindingResult.getFieldError().getDefaultMessage());
			}
			// 获取用户列表
			Page<Role> page = roleService.findPageList(form);
			
			return success(page);
		}

	    @GetMapping("/toAdd")
	    public String toAdd(){
	        return "back/system/role/role-add";
	    }

	    @GetMapping("toEdit")
	    public ModelAndView toEdit(Integer id){
	    	ModelAndView mv = new ModelAndView();
	    	mv.addObject("role", roleService.findById(id));
	    	mv.setViewName("back/system/role/role-edit");
	        return mv;
	    }

	    
		@PostMapping("/add")
		@ResponseBody
		public ResultVO add(@Valid @RequestBody RoleAddForm form,BindingResult bindingResult) {
			validateForm(bindingResult);
			Role role = BeanCopyUtil.convertBean(form, Role.class);
			// 保存数据
			roleService.save(role);
			return success();
		}
		
		@PostMapping("/edit")
		@ResponseBody
		public ResultVO edit(@Valid @RequestBody RoleEditForm form,BindingResult bindingResult) {
			validateForm(bindingResult);
			Role role = roleService.findById(form.getId());
			BeanCopyUtil.copyNotNullProperties(form, role);
			// 保存数据
			roleService.update(role);
			return success();
		}

	    @GetMapping("/toAuth")
	    public ModelAndView toAuth(Integer roleId){
	    	ModelAndView mv = new ModelAndView();
	    	mv.addObject("roleId", roleId);
	    	mv.setViewName("back/system/role/role-auth");
	        return mv;
	    }

	    /**
	     * 保存授权信息
	     */
	    @PostMapping("/auth")
	    @ResponseBody
	    public ResultVO auth(@RequestBody @Valid AuthForm form, BindingResult bindingResult){
	    	 // 参数验证
	    	validateForm(bindingResult);
	        roleService.auth(form.getRoleId(),form.getMenuIds());
	        return success();
	    }

	    /**
	     * 跳转到详细页面
	     */
	    @GetMapping("toDetail")
	    public ModelAndView toDetail(Integer id){
	    	ModelAndView mv = new ModelAndView();
	    	mv.addObject("role", roleService.findById(id));
	    	mv.setViewName("back/system/role/role-detail");
	        return mv;
	    }

	    /**
	     * 跳转到拥有该角色的用户列表页面
	     */
	    @GetMapping("toRoleUserList")
	    public ModelAndView toRoleUserList(Integer id){
	    	ModelAndView mv = new ModelAndView();
	    	mv.addObject("userList", roleService.getUserListByRoleId(id));
			mv.setViewName("back/system/role/role-user-list");
			return mv;
	    }

	    
		@GetMapping("/delete")
		@ResponseBody
		public ResultVO delete(@RequestParam("ids") List<Integer> ids) {
			roleService.deleteByIdIn(ids);
			return success();
		}
		
		
		/**
		 * 查询角色下拉列表
		 * @return
		 * @author yys
		 */
		@GetMapping("/getRoleList")
		@ResponseBody
		public List<RoleSelectVO> getRoleList() {
			
			List<Role> roles = roleService.findAll();
			List<RoleSelectVO> selectList = new ArrayList<>();
			for (Role role : roles) {
				RoleSelectVO roleSelectVO = new RoleSelectVO();
				roleSelectVO.setId(role.getId());
				roleSelectVO.setName(role.getTitle());
				selectList.add(roleSelectVO);
			}
			return selectList;
		}

}
