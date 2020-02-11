package com.dszj.manage.controller;

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
import com.dszj.manage.entity.Dict;
import com.dszj.manage.exception.BizException;
import com.dszj.manage.exception.BizExceptionEnum;
import com.dszj.manage.form.DictAddForm;
import com.dszj.manage.form.DictEditForm;
import com.dszj.manage.form.DictQueryForm;
import com.dszj.manage.service.DictService;
import com.dszj.manage.utils.BeanCopyUtil;
import com.dszj.thymeleaf.DictUtil;

/**
 * 数据字典控制器
 * @author yys
 */
@Controller
@RequestMapping("/dict")
@SuppressWarnings("rawtypes")
public class DictController extends BaseController {
	 @Autowired
	    private DictService dictService;

	    @GetMapping("/toList")
	    public String index(){
	        return "back/system/dict/dict-list";
	    }

	    @GetMapping("/toAdd")
	    public String toAdd(){
	        return "back/system/dict/dict-add";
	    }

		@GetMapping("/getPageList")
		@ResponseBody
		public ResultVO getPageList(@Valid DictQueryForm form, BindingResult bindingResult) {
			
			// 参数验证
			if (bindingResult.hasErrors()) {
				throw new BizException(BizExceptionEnum.PARAM_ERROR,
						bindingResult.getFieldError().getDefaultMessage());
			}
			// 获取用户列表
			Page<Dict> page = dictService.findPageList(form);
			
			return success(page);
		}

		
		@GetMapping("/toEdit")
		public ModelAndView toEdit(Integer id) {
			ModelAndView mv = new ModelAndView();
			mv.addObject("dict", dictService.findById(id));
			mv.setViewName("back/system/dict/dict-edit");
			return mv;
		}
		
		@GetMapping("/toDetail")
		public ModelAndView toDetail(Integer id) {
			ModelAndView mv = new ModelAndView();
			mv.addObject("dict", dictService.findById(id));
			mv.setViewName("back/system/dict/dict-detail");
			return mv;
		}
		@PostMapping("/add")
		@ResponseBody
		public ResultVO add(@Valid @RequestBody DictAddForm form,BindingResult bindingResult) {
			
			validateForm(bindingResult);
			Dict dict = BeanCopyUtil.convertBean(form, Dict.class);
			// 保存数据
			dictService.save(dict);
			DictUtil.clearAllCache();
			return success();
		}
		
		@PostMapping("/edit")
		@ResponseBody
		public ResultVO edit(@Valid @RequestBody DictEditForm form,BindingResult bindingResult) {
			validateForm(bindingResult);
			Dict dict = dictService.findById(form.getId());
			BeanCopyUtil.copyNotNullProperties(form, dict);
			// 保存数据
			dictService.update(dict);
			DictUtil.clearAllCache();
			return success();
		}
		
		
		@GetMapping("/delete")
		@ResponseBody
		public ResultVO delete(@RequestParam("ids") List<Integer> ids) {
			dictService.deleteByIdIn(ids);
			DictUtil.clearAllCache();
			return success();
		}


}
