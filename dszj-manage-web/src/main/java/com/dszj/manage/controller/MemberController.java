package com.dszj.manage.controller;

import com.dszj.manage.base.BaseController;
import com.dszj.manage.base.ResultVO;
import com.dszj.manage.dao.MemberDao;
import com.dszj.manage.entity.Member;
import com.dszj.manage.form.MemberPageForm;
import com.dszj.manage.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

import javax.validation.Valid;

/**
 * @author: zhangsy
 * @Date: 2019/10/8
 * @Description:
 */
@Controller
@RequestMapping("/member")
@SuppressWarnings("rawtypes,unchecked")
public class MemberController extends BaseController {
    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberDao memberdao;

    @GetMapping("/toList")
    public String toList() {
        return "back/system/member/member-list";
    }

    @GetMapping("/queryMemberPage")
    @ResponseBody
    public ResultVO queryMemberPage(@Valid MemberPageForm form){
    	form.setPageSize(1000);
        Page<Member> page = memberService.findPageList(form);
        ResultVO resultVO = new ResultVO<>(page);
        
        resultVO.setTraceId(String.valueOf(memberdao.couontNickname()));
        resultVO.setErrDesc(String.valueOf(memberdao.couontHeaderpic()));
        resultVO.setPageNum(page.getNumber());
		resultVO.setPageSize(page.getSize());
		resultVO.setTotal(Integer.valueOf(String.valueOf(page.getTotalElements())));
		
		for (Member member : page.getContent()) {
				if ("1".equals(member.getNameStatus())) {
					member.setNickname("");
				}
				
				if ("1".equals(member.getPicStatus())) {
					member.setHeaderPic("");
				}
		}
		
		resultVO.setData(page.getContent());
        return resultVO;
    }
    
	@GetMapping("/headerpic")
	@ResponseBody
	public ResultVO headerpic(@RequestParam("ids") List<Integer> ids) {
		memberService.batchUpdate(false,ids);
		return success();
	}
	
	@GetMapping("/nickname")
	@ResponseBody
	public ResultVO nickname(@RequestParam("ids") List<Integer> ids) {
		memberService.batchUpdate(true,ids);
		return success();
	}
}
