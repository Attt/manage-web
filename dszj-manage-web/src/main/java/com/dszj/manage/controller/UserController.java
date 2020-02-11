package com.dszj.manage.controller;

import java.util.List;

import javax.validation.Valid;

import org.apache.commons.codec.digest.DigestUtils;
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

import com.dszj.manage.auth.LoginAccount;
import com.dszj.manage.auth.UserInfo;
import com.dszj.manage.base.BaseController;
import com.dszj.manage.base.ResultVO;
import com.dszj.manage.entity.Dept;
import com.dszj.manage.entity.Role;
import com.dszj.manage.entity.User;
import com.dszj.manage.entity.UserRole;
import com.dszj.manage.exception.BizException;
import com.dszj.manage.exception.BizExceptionEnum;
import com.dszj.manage.form.EditPwdForm;
import com.dszj.manage.form.UserAddForm;
import com.dszj.manage.form.UserEditForm;
import com.dszj.manage.form.UserQueryForm;
import com.dszj.manage.service.DeptService;
import com.dszj.manage.service.RoleService;
import com.dszj.manage.service.UserRoleService;
import com.dszj.manage.service.UserService;

/**
 * 后台用户控制器
 * @author yys
 */
@Controller
@RequestMapping("/user")
@SuppressWarnings("rawtypes")
public class UserController extends BaseController {
	@Autowired
	private UserService userService;
	@Autowired
	private DeptService deptService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private UserRoleService userRoleService;
	
	@GetMapping("/toList")
	public String toList() {
		return "back/system/user/user-list";
	}
	
	
	@GetMapping("/getPageList")
	@ResponseBody
	public ResultVO getPageList(@Valid UserQueryForm form, BindingResult bindingResult) {
		// 参数验证
		if (bindingResult.hasErrors()) {
			throw new BizException(BizExceptionEnum.PARAM_ERROR,
					bindingResult.getFieldError().getDefaultMessage());
		}
		// 获取用户列表
		Page<User> page = userService.findPageList(form);
		List<Dept> deptList = deptService.findAll();
//		List<Role> roleList = roleService.findAll();
//		List<UserRole> userRoleList = userRoleService.findAll();
		for (Dept dept : deptList) {
			for (User user : page.getContent()) {
				if (dept.getId().equals(user.getDeptId())) {
					user.setDeptName(dept.getName());
				}
			}
			
//			for (Role role : roleList) {
//				
//			}
//			
//			for (UserRole userRole : userRoleList) {
//				
//			}
		}

		return success(page);
	}

    @GetMapping("/toAdd")
    public ModelAndView toAdd(){
    	ModelAndView mv = new ModelAndView();
    	mv.addObject("roles", roleService.findAll());
    	mv.setViewName("back/system/user/user-add");
        return mv;
    }
	
	@GetMapping("/toEdit")
	public ModelAndView toEdit(Integer id) {
		ModelAndView mv = new ModelAndView();
		
		List<Role> roles = roleService.findAll();
		List<UserRole> userRoles = userRoleService.findByUserId(id);
		for (UserRole userRole : userRoles) {
			for (Role role : roles) {
				if (userRole.getRoleId().equals(role.getId())) {
					role.setChecked(true);
				}
			}
		}
		
		mv.addObject("user", userService.findById(id));
		mv.addObject("roles", roleService.findAll());
		mv.setViewName("back/system/user/user-edit");
		return mv;
	}
	
	@GetMapping("/toDetail")
	public ModelAndView toDetail(Integer id) {
		ModelAndView mv = new ModelAndView();
		User user = userService.findById(id);
		Dept dept = deptService.findById(user.getDeptId());
		if (dept!=null) {
			user.setDeptName(dept.getName());
		}
		mv.addObject("user",user);
		mv.setViewName("back/system/user/user-detail");
		return mv;
	}
	
	@PostMapping("/add")
	@ResponseBody
	public ResultVO add(@Valid @RequestBody UserAddForm form,BindingResult bindingResult) {
		validateForm(bindingResult);
		// 保存数据
		form.setPassword(DigestUtils.md5Hex(form.getPassword().trim()));
		userService.save(form);
		return success();
	}
	
	@PostMapping("/edit")
	@ResponseBody
	public ResultVO edit(@Valid @RequestBody UserEditForm form,BindingResult bindingResult) {
		validateForm(bindingResult);
		userService.update(form);
		return success();
	}
	
	
	@GetMapping("/delete")
	@ResponseBody
	public ResultVO delete(@RequestParam("ids") List<Integer> ids) {
		userService.deleteByIdIn(ids);
		return success();
	}
	
	@GetMapping("/resetPassword")
	@ResponseBody
	public ResultVO resetPassword(@RequestParam("id") Integer id) {
		User user = userService.findById(id);
		user.setPassword(DigestUtils.md5Hex("000000"));
		return success();
	}

}
