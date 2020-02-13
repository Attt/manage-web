package com.dszj.manage.controller;

import com.dszj.manage.base.BaseController;
import com.dszj.manage.base.ResultVO;
import com.dszj.manage.entity.Activity;
import com.dszj.manage.entity.Student;
import com.dszj.manage.exception.BizException;
import com.dszj.manage.exception.BizExceptionEnum;
import com.dszj.manage.form.StudentAddForm;
import com.dszj.manage.form.StudentEditForm;
import com.dszj.manage.service.ClassService;
import com.dszj.manage.service.StudentService;
import com.dszj.manage.utils.BeanCopyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
@RequestMapping("/student")
@Slf4j
@SuppressWarnings("rawtypes,unchecked")
public class StudentController extends BaseController {
    @Autowired
    private StudentService studentService;
    @Autowired
    private ClassService classService;

    /**
     * 添加学生信息
     */
    @GetMapping("/toAdd")
    public String toAdd(){
        return "back/module/student/student-add";
    }

    @PostMapping("/add")
    @ResponseBody
    public ResultVO add(@Valid @RequestBody StudentAddForm form, BindingResult bindingResult) {
        validateForm(bindingResult);
        Student student = BeanCopyUtil.convertBean(form,Student.class);
        // 保存数据
        studentService.save(student);
        return success();
    }

    @GetMapping("/toEdit")
    public ModelAndView toEdit(Integer id){
        ModelAndView mv = new ModelAndView();
        mv.addObject("student", studentService.findById(id));
        mv.setViewName("back/module/student/student-edit");
        return mv;
    }

    @PostMapping("/edit")
    @ResponseBody
    public ResultVO edit(@Valid @RequestBody StudentEditForm form, BindingResult bindingResult){
        validateForm(bindingResult);
        Student student = studentService.findById(form.getId());
        if(null == student){
            throw new BizException(BizExceptionEnum.PARAM_ERROR,"未查询到信息");
        }
        BeanCopyUtil.copyNotNullProperties(form,student);
        studentService.update(student);
        return success();
    }
}
