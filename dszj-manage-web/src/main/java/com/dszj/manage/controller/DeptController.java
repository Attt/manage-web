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
import com.dszj.manage.entity.Dept;
import com.dszj.manage.exception.BizException;
import com.dszj.manage.exception.BizExceptionEnum;
import com.dszj.manage.form.DeptAddForm;
import com.dszj.manage.form.DeptQueryForm;
import com.dszj.manage.form.DeptEditForm;
import com.dszj.manage.service.DeptService;
import com.dszj.manage.utils.BeanCopyUtil;
import com.dszj.manage.vo.DeptSelectVO;
import com.dszj.manage.vo.DeptTreeVO;

/**
 * 部门控制器
 * @author yys
 */
@Controller
@RequestMapping("/dept")
@SuppressWarnings("rawtypes")
public class DeptController extends BaseController {
	 @Autowired
	    private DeptService deptService;

	    @GetMapping("/toList")
	    public String toList(){
	        return "back/system/dept/dept-list";
	    }
		@GetMapping("/getPageList")
		@ResponseBody
		public ResultVO getPageList(@Valid DeptQueryForm form, BindingResult bindingResult) {
			
			// 参数验证
			if (bindingResult.hasErrors()) {
				throw new BizException(BizExceptionEnum.PARAM_ERROR,
						Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
			}
			// 获取用户列表
			Page<Dept> page = deptService.findPageList(form);
			
			return success(page);
		}
	    
		/**
		 * 查询部门结构树
		 * @return
		 * @author yys
		 */
		@GetMapping("/getDeptTree")
		@ResponseBody
		public ResultVO getDeptTree() {
			
			List<Dept> deptList = deptService.findAll();
			List<DeptTreeVO> treeList = new ArrayList<>();
			deptList.forEach(dept -> {
				if (dept.getParentId()==0) {
					DeptTreeVO deptTreeVO = new DeptTreeVO();
					deptTreeVO.setId(dept.getId());
					deptTreeVO.setTitle(dept.getName());
					deptTreeVO.setSpread(true);
					
					List<DeptTreeVO> subTreeList = new ArrayList<>();
					deptList.forEach(subDept -> {
						if (subDept.getParentId()==dept.getId()) {
							
							DeptTreeVO subDeptTreeVO = new DeptTreeVO();
							subDeptTreeVO.setId(subDept.getId());
							subDeptTreeVO.setTitle(subDept.getName());
							subTreeList.add(subDeptTreeVO);
						}
			        });
					
					deptTreeVO.setChildren(subTreeList);
					treeList.add(deptTreeVO);
				}
				
	        });
			
			return success(treeList);
		}
		
		/**
		 * 查询部门列表
		 * @return
		 * @author yys
		 */
		@GetMapping("/getDeptList")
		@ResponseBody
		public List<DeptSelectVO> getDeptList() {
			
			List<Dept> deptList = deptService.findAll();
			List<DeptSelectVO> treeList = new ArrayList<>();
			deptList.forEach(dept -> {
				if (dept.getParentId()==1) {
					DeptSelectVO deptSelectVO = new DeptSelectVO();
					deptSelectVO.setId(dept.getId());
					deptSelectVO.setName(dept.getName());
					deptSelectVO.setParent(true);
					deptSelectVO.setOpen(true);
					treeList.add(deptSelectVO);
				}
	        });
			
			return treeList;
		}
		
		
	    @GetMapping("/toAdd")
	    public String toAdd(){
	        return "back/system/dept/dept-add";
	    }

	    @GetMapping("toEdit")
	    public ModelAndView toEdit(Integer id){
	    	ModelAndView mv = new ModelAndView();
	    	mv.addObject("dept", deptService.findById(id));
	    	mv.setViewName("back/system/dept/dept-edit");
	        return mv;
	    }
	    
	    @PostMapping("/add")
		@ResponseBody
		public ResultVO add(@Valid @RequestBody DeptAddForm form,BindingResult bindingResult) {
	    	validateForm(bindingResult);
			Dept dept = BeanCopyUtil.convertBean(form, Dept.class);
			// 保存数据
			deptService.save(dept);
			return success();
		}
		
		
		@PostMapping("/edit")
		@ResponseBody
		public ResultVO edit(@Valid @RequestBody DeptEditForm form,BindingResult bindingResult) {
			validateForm(bindingResult);
			Dept Dept = deptService.findById(form.getId());
			BeanCopyUtil.copyNotNullProperties(form, Dept);
			// 保存数据
			deptService.update(Dept);
			return success();
		}
		
	    @GetMapping("toDetail")
	    public ModelAndView toDetail(Integer id){
	    	ModelAndView mv = new ModelAndView();
	    	mv.addObject("dept", deptService.findById(id));
	    	mv.setViewName("back/system/dept/dept-detail");
	        return mv;
	    }
	    
		@GetMapping("/delete")
		@ResponseBody
		public ResultVO delete(@RequestParam("ids") List<Integer> ids) {
			deptService.deleteByIdIn(ids);
			return success();
		}

}
