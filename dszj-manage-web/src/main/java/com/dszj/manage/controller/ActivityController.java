package com.dszj.manage.controller;

import com.dszj.manage.auth.SessionKeyEnum;
import com.dszj.manage.auth.UserInfo;
import com.dszj.manage.base.BaseController;
import com.dszj.manage.base.ResultVO;
import com.dszj.manage.entity.Activity;
import com.dszj.manage.entity.Dept;
import com.dszj.manage.entity.Member;
import com.dszj.manage.entity.Record;
import com.dszj.manage.enums.ActivityTypeEnum;
import com.dszj.manage.exception.BizException;
import com.dszj.manage.exception.BizExceptionEnum;
import com.dszj.manage.form.ActivityAddForm;
import com.dszj.manage.form.ActivityEditForm;
import com.dszj.manage.form.ActivityPageForm;
import com.dszj.manage.form.DeptAddForm;
import com.dszj.manage.service.ActivityService;
import com.dszj.manage.service.MemberService;
import com.dszj.manage.service.RecordService;
import com.dszj.manage.utils.BeanCopyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;
import java.util.Optional;
import java.util.zip.ZipException;

@Controller
@RequestMapping("/activity")
@Slf4j
@SuppressWarnings("rawtypes,unchecked")
public class ActivityController extends BaseController {
    @Autowired
    private ActivityService activityService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private RecordService recordService;

    @GetMapping("/toList")
    public String toList(){
        return "back/module/activity/activity-list";
    }

    /**
     * 分页查询签到活动
     * @param form 前台表单
     */
    @GetMapping("/queryActivityPage")
    @ResponseBody
    public ResultVO queryActivityPage(ActivityPageForm form,HttpServletRequest request){
        UserInfo userInfo = (UserInfo) request.getSession().getAttribute(SessionKeyEnum.ACCOUNT.getCode());
        form.setCreatorId(userInfo.getId());
        Page<Activity> activityPage = activityService.findPageList(form);
        ResultVO resultVO = new ResultVO(activityPage);
        resultVO.setPageNum(activityPage.getNumber());
        resultVO.setPageSize(activityPage.getSize());
        resultVO.setTotal(Integer.valueOf(String.valueOf(activityPage.getTotalElements())));
        resultVO.setData(activityPage.getContent());
        return resultVO;
    }

    /**
     * 添加活动
     */
    @GetMapping("/toAdd")
    public String toAdd(){
        return "back/module/activity/activity-add";
    }

    @PostMapping("/add")
    @ResponseBody
    public ResultVO add(@Valid @RequestBody ActivityAddForm form, BindingResult bindingResult, HttpServletRequest request) {
        validateForm(bindingResult);
        Activity activity = BeanCopyUtil.convertBean(form,Activity.class);
        UserInfo userInfo = (UserInfo) request.getSession().getAttribute(SessionKeyEnum.ACCOUNT.getCode());
        activity.setCreatorId(userInfo.getId());
        // 保存数据
        activityService.save(activity);
        return success();
    }

    @GetMapping("/toEdit")
    public ModelAndView toEdit(Integer id){
        ModelAndView mv = new ModelAndView();
        mv.addObject("activity", activityService.findById(id));
        mv.setViewName("back/module/activity/activity-edit");
        return mv;
    }

    @PostMapping("/edit")
    @ResponseBody
    public ResultVO edit(@Valid @RequestBody ActivityEditForm form,BindingResult bindingResult){
        validateForm(bindingResult);
        Activity activity = activityService.findById(form.getId());
        if(null == activity){
            throw new BizException(BizExceptionEnum.PARAM_ERROR,"未查询到信息");
        }
        if(form.getStartTime().compareTo(form.getEndTime()) > 0){
            throw new BizException(BizExceptionEnum.PARAM_ERROR,"开始时间不能晚于结束时间");
        }
        BeanCopyUtil.copyNotNullProperties(form,activity);
        activityService.update(activity);
        return success();
    }

    @GetMapping("/toCode")
    public ModelAndView toCode(Integer id){
        ModelAndView mv = new ModelAndView();
        mv.addObject("id",id);
        mv.setViewName("back/module/activity/activity-code");
        return mv;
    }

    @GetMapping("/queryById")
    @ResponseBody
    public ResultVO queryById(@RequestParam("id") Integer id,@RequestParam("memberId")Integer memberId){
        if(null == id || null == memberId){
            throw new BizException(BizExceptionEnum.PARAM_ERROR);
        }
        //通过活动id查询活动信息
        Activity activity = activityService.findById(id);
        Optional.ofNullable(activity).orElseThrow(()->new BizException(BizExceptionEnum.PARAM_ERROR));
        //判断时间
        Date now = new Date();
        if((activity.getStartTime().compareTo(now) > 0) || activity.getEndTime().compareTo(now) < 0){
            throw new BizException(BizExceptionEnum.PARAM_ERROR,"当前未在签到时间范围");
        }
        //通过活动id和用户id查询是否已签到
        Record record = recordService.findByActivityIdAndMemberId(id,memberId);
        //签到成功 0 未绑定 1
        int flag = 0 ;
        //判断当前用户是否已绑定姓名或姓名学号
        Member member = memberService.findById(memberId);
        Optional.ofNullable(member).orElseThrow(()->new BizException(BizExceptionEnum.PARAM_ERROR));
        //判断用户姓名是否为空
        if(StringUtils.isEmpty(member.getName())){
            flag = 1;
        }else {
            if(null == record){
                record = new Record();
                record.setMemberId(memberId);
                record.setActivityId(id);
                record.setActivityName(activity.getName());
                recordService.save(record);
            }
        }
        return success(flag);
    }
}
