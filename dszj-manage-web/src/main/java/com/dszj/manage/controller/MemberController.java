package com.dszj.manage.controller;

import com.dszj.manage.api.WeChatLoginApi;
import com.dszj.manage.auth.PublicAuth;
import com.dszj.manage.base.BaseController;
import com.dszj.manage.base.ResultVO;
import com.dszj.manage.dao.MemberDao;
import com.dszj.manage.dto.WeChatUserInfoDTO;
import com.dszj.manage.entity.Activity;
import com.dszj.manage.entity.Member;
import com.dszj.manage.entity.Record;
import com.dszj.manage.entity.Student;
import com.dszj.manage.enums.ActivityTypeEnum;
import com.dszj.manage.exception.BizException;
import com.dszj.manage.exception.BizExceptionEnum;
import com.dszj.manage.form.BindInfoForm;
import com.dszj.manage.form.MemberPageForm;
import com.dszj.manage.form.WeChatLoginForm;
import com.dszj.manage.service.ActivityService;
import com.dszj.manage.service.MemberService;
import com.dszj.manage.service.StudentService;
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

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.validation.Valid;

/**
 * @author: zhangsy
 * @Date: 2019/10/8
 * @Description:
 */
@Controller
@RequestMapping("/member")
@SuppressWarnings("rawtypes")
public class MemberController extends BaseController {
    @Autowired
    private MemberService memberService;
    @Autowired
	private WeChatLoginApi weChatLoginApi;
    @Autowired
	private ActivityService activityService;
    @Autowired
	private StudentService studentService;

	/**
	 * 微信登录
	 */
	@PostMapping("/loginByWeChat")
	public ResultVO loginByWeChat(@RequestBody WeChatLoginForm form, BindingResult bindingResult) {
		// 参数验证
		validateForm(bindingResult);
		String code = form.getCode();

		// 获取微信用户信息
		WeChatUserInfoDTO userInfoDTO = weChatLoginApi.getUserInfo(code);
		if (!userInfoDTO.isSuccess()) {
			throw new BizException(BizExceptionEnum.SYS_ERROR, userInfoDTO.getErrMsg());
		}

		// 查询用户是否已注册
		String openId = userInfoDTO.getOpenId();
		Member member = memberService.findByOpenId(openId);
		if (member == null) {
			// 新增用户信息
			member = new Member();
			//过滤表情
			member.setOpenId(openId);
			member.setHeaderPic(userInfoDTO.getHeadImgUrl());
			member = memberService.save(member);
		}
		return success(member.getId());
	}

	/**
	 * 绑定信息
	 * @param form
	 * @param bindingResult
	 * @return
	 */
	@PostMapping("/bindInfo")
	@ResponseBody
	public ResultVO bindInfo(@RequestBody BindInfoForm form,BindingResult bindingResult){
		validateForm(bindingResult);
		//查询活动信息
		Activity activity = activityService.findById(form.getId());
		Optional.ofNullable(activity).orElseThrow(()->new BizException(BizExceptionEnum.PARAM_ERROR));
		//查询用户信息
		Member member = memberService.findById(form.getMemberId());
		Optional.ofNullable(member).orElseThrow(()->new BizException(BizExceptionEnum.PARAM_ERROR));

		//判断活动类型
		if(activity.getType().equals(ActivityTypeEnum.SOURCE.getCode())){
			if(StringUtils.isEmpty(form.getStudentNo())){
				throw new BizException(BizExceptionEnum.PARAM_ERROR,"学号未填写");
			}
			//通过学号查询学生信息
			Student student = studentService.findByStudentNo(form.getStudentNo());
			Optional.ofNullable(student).orElseThrow(()->new BizException(BizExceptionEnum.PARAM_ERROR,"未查询到学生信息"));
			member.setStudentId(student.getId());
		}
		member.setName(form.getName());
		//创建签到记录
		Record record = new Record();
		record.setActivityName(activity.getName());
		record.setActivityId(activity.getId());
		record.setMemberId(form.getMemberId());
		memberService.saveInfo(member,record);
		return success();
	}
}
