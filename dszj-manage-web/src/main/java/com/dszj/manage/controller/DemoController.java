package com.dszj.manage.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dszj.manage.base.BaseController;
import com.dszj.manage.base.ResultVO;
import com.dszj.manage.entity.Demo;
import com.dszj.manage.exception.BizException;
import com.dszj.manage.exception.BizExceptionEnum;
import com.dszj.manage.form.DemoAddForm;
import com.dszj.manage.form.DemoQueryForm;
import com.dszj.manage.service.DemoService;
import com.dszj.manage.utils.BeanCopyUtil;

/**
 * 测试demo
 * @author yangyuesong
 *
 */
@Controller
@RequestMapping("/demo")
@SuppressWarnings("rawtypes")
public class DemoController extends BaseController {
	@Autowired
	private DemoService demoService;
	
	@GetMapping("/getPageList")
	@ResponseBody
	public ResultVO getPageList(@Valid DemoQueryForm form, BindingResult bindingResult) {
		
		// 参数验证
		if (bindingResult.hasErrors()) {
			throw new BizException(BizExceptionEnum.PARAM_ERROR,
					bindingResult.getFieldError().getDefaultMessage());
		}
		Page<Demo> page = demoService.findPageList(form);
		
		return success(page);
	}
	
	@PostMapping("/add")
	@ResponseBody
	public ResultVO add(@RequestBody @Valid DemoAddForm form, BindingResult bindingResult) {
		
		// 参数验证
		if (bindingResult.hasErrors()) {
			throw new BizException(BizExceptionEnum.PARAM_ERROR,
					bindingResult.getFieldError().getDefaultMessage());
		}
		Demo demo = BeanCopyUtil.convertBean(form, Demo.class);
		return success(demoService.save(demo));
	}

}
