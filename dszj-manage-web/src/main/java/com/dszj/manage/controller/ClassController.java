package com.dszj.manage.controller;


import com.dszj.manage.base.BaseController;
import com.dszj.manage.base.ResultVO;
import com.dszj.manage.entity.Class;
import com.dszj.manage.form.ClassAddForm;
import com.dszj.manage.form.ClassPageForm;
import com.dszj.manage.service.ClassService;
import com.dszj.manage.utils.BeanCopyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/class")
@Slf4j
@SuppressWarnings("rawtypes,unchecked")
public class ClassController extends BaseController {
    @Autowired
    private ClassService classService;

    @GetMapping("/toList")
    public String toList(){
        return "back/module/class/class-list";
    }

    /**
     * 分页查询班级列表
     * @param form 前台表单
     */
    @GetMapping("/queryClassPage")
    @ResponseBody
    public ResultVO queryClassPage(ClassPageForm form){
        Page<Class> classPage = classService.findPageList(form);
        ResultVO resultVO = new ResultVO(classPage);
        resultVO.setPageNum(classPage.getNumber());
        resultVO.setPageSize(classPage.getSize());
        resultVO.setTotal(Integer.valueOf(String.valueOf(classPage.getTotalElements())));
        resultVO.setData(classPage.getContent());
        return resultVO;
    }

    @GetMapping("/queryClassList")
    @ResponseBody
    public ResultVO queryClassList(){
        List<Class> classList = classService.findAll();
        return success(classList);
    }

    /**
     * 添加班级
     */
    @GetMapping("/toAdd")
    public String toAdd(){
        return "back/module/class/class-add";
    }

    @PostMapping("/add")
    @ResponseBody
    public ResultVO add(@Valid @RequestBody ClassAddForm form, BindingResult bindingResult) {
        validateForm(bindingResult);
        Class entity = BeanCopyUtil.convertBean(form,Class.class);
        // 保存数据
        classService.save(entity);
        return success();
    }
}
