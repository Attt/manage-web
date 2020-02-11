package com.dszj.manage.controller;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.dszj.manage.auth.BackAuth;
import com.dszj.manage.auth.LoginAccount;
import com.dszj.manage.auth.PublicAuth;
import com.dszj.manage.auth.UserInfo;
import com.dszj.manage.base.BaseController;
import com.dszj.manage.base.ResultVO;
import com.dszj.manage.entity.Menu;
import com.dszj.manage.entity.User;
import com.dszj.manage.enums.FilePathEnum;
import com.dszj.manage.enums.MenuTypeEnum;
import com.dszj.manage.exception.BizException;
import com.dszj.manage.exception.BizExceptionEnum;
import com.dszj.manage.form.EditPwdForm;
import com.dszj.manage.form.UserInfoForm;
import com.dszj.manage.service.MenuService;
import com.dszj.manage.service.UserService;
import com.dszj.manage.utils.BeanCopyUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 后台页面主框架控制器
 * @author yys
 */
@Controller
// @RequestMapping("/main")
@Slf4j
@SuppressWarnings("rawtypes")
public class MainController extends BaseController {
	
	@Autowired
	private UserService userService;

	@Autowired
	private MenuService menuService;

	/**
	 * 后台主体内容
	 */
	@GetMapping(value = "/")
	@BackAuth
	public ModelAndView main(@LoginAccount UserInfo userInfo) {
		
		ModelAndView mv = new ModelAndView();
		List<Menu> allMenus = null;
		if (userInfo.getId()==1) {
			allMenus = menuService.findAllBySort();
		}else{
			allMenus = menuService.findByUserId(userInfo.getId());
		}
		
		Menu treeData = new Menu();
		genTreeData(allMenus, treeData);
		
		//当前只获取第一个模块下的菜单列表
		List<Menu> treeMenu = null;
		if (treeData.getChildren()!=null && treeData.getChildren().size()>0) {
			treeMenu = treeData.getChildren().get(0).getChildren();
		}
		User user = userService.findById(userInfo.getId());
		mv.addObject("user", user);
		mv.addObject("treeMenu", treeMenu);
		mv.setViewName("back/main");
		return mv;
	}
	
	private void genTreeData(List<Menu> allMenus,Menu treeData){
		
		List<Menu> children = new ArrayList<>();
		
		for (Menu menu : allMenus) {
			
			if (menu.getType().equals(MenuTypeEnum.OPERATE.getCode())||menu.getType().equals(MenuTypeEnum.RESOURCE.getCode())) {
				continue;
			}
			
			if (menu.getType().equals(MenuTypeEnum.MODE_MENU.getCode())) {
				//菜单根节点
				if (treeData.getType()==null) {
					children.add(menu);
				}
			}
			
			
			if (menu.getType().equals(MenuTypeEnum.ONE_MENU.getCode())||menu.getType().equals(MenuTypeEnum.TWO_MENU.getCode())) {
				//菜单根节点
				if (menu.getParentId().equals(treeData.getId())) {
					children.add(menu);
				}
			}
		}
		
		for (Menu menu : children) {
			genTreeData(allMenus, menu);
		}
		
		treeData.setChildren(children);
		
	}

	/**
	 * 主页
	 */
	@GetMapping("/index")
	public String index() {
		return "back/system/main/index";
	}

	/**
	 * 跳转到个人信息页面
	 */
	@GetMapping("/toUserInfo")
	@PublicAuth
	public ModelAndView toUserInfo(ModelAndView mv,@LoginAccount UserInfo userInfo) {
		User user = userService.findById(userInfo.getId());
		mv.addObject("user", user);
		mv.setViewName("back/system/main/userInfo");
		return mv;
	}

	/**
	 * 修改密码页面
	 */
	@GetMapping("/toEditPassword")
	@PublicAuth
	public String toEditPassword() {
		return "back/system/main/editPwd";
	}
	
	@PostMapping("/editPwd")
	@PublicAuth
	@ResponseBody
	public ResultVO editPwd(@Valid @RequestBody EditPwdForm editPwdForm,@LoginAccount UserInfo userInfo,BindingResult bindingResult) {
		
		validateForm(bindingResult);
		
		if (!editPwdForm.getNewPassword().equals(editPwdForm.getConfirmPassword())) {
			throw new BizException(BizExceptionEnum.PARAM_ERROR, "两次输入密码不一致");
		}
		User user = userService.findById(userInfo.getId());
		if (!user.getPassword().equals(DigestUtils.md5Hex(editPwdForm.getOrginalPassword().trim()))) {
			throw new BizException(BizExceptionEnum.PARAM_ERROR, "原密码输入错误");
		}
		user.setPassword(DigestUtils.md5Hex(editPwdForm.getNewPassword().trim()));
		userService.save(user);
		return success();
	}
	
	/**
	 * 修改用户头像
	 */
	@PostMapping("/uploadHeadPic")
	@ResponseBody
	@PublicAuth
	public ResultVO uploadHeadPic(@RequestParam("headPic") MultipartFile headPic,@LoginAccount UserInfo userInfo) throws FileNotFoundException {
		try {
            String fileName = UUID.randomUUID().toString()+headPic.getOriginalFilename().substring(headPic.getOriginalFilename().lastIndexOf("."));
            InputStream inputStream = headPic.getInputStream();
            Path directory = Paths.get(FilePathEnum.HEAD_PIC.getValue());
            if(!Files.exists(directory)){
                Files.createDirectories(directory);
            }
            Files.copy(inputStream, directory.resolve(fileName));
//			Thumbnails.of(headPic.getInputStream()).scale(0.8f).toFile(diskFile);
			User user = userService.findById(userInfo.getId());
			user.setHeadPic(FilePathEnum.HEAD_PIC.getPath(fileName));
			userService.save(user);

        } catch (Exception e) {
            log.error("文件上传失败", e);
        	throw new BizException(BizExceptionEnum.SYS_ERROR);
        }
		return success();
	}
	

	@PostMapping("/editUserInfo")
	@ResponseBody
	@PublicAuth
	public ResultVO editUserInfo(@Valid @RequestBody UserInfoForm form,BindingResult bindingResult,@LoginAccount UserInfo userInfo) {
		validateForm(bindingResult);
		User user = userService.findById(userInfo.getId());
		BeanCopyUtil.copyNotNullProperties(form, user);
		userService.update(user);
		return success();
	}

}
