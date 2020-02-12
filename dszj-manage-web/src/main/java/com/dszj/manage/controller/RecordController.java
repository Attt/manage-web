package com.dszj.manage.controller;

import com.dszj.manage.base.BaseController;
import com.dszj.manage.base.ResultVO;
import com.dszj.manage.entity.Record;
import com.dszj.manage.form.RecordPageForm;
import com.dszj.manage.service.RecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/record")
@Slf4j
@SuppressWarnings("rawtypes,unchecked")
public class RecordController extends BaseController {
    @Autowired
    private RecordService recordService;

    @GetMapping("/toList")
    public String toList(){
        return "back/module/record/record-list";
    }

    /**
     * 分页查询签到记录
     * @param form 前台表单
     */
    @GetMapping("/queryRecordPage")
    @ResponseBody
    public ResultVO queryRecordPage(RecordPageForm form){
        Page<Record> recordPage = recordService.findPageList(form);
        ResultVO resultVO = new ResultVO(recordPage);
        resultVO.setPageNum(recordPage.getNumber());
        resultVO.setPageSize(recordPage.getSize());
        resultVO.setTotal(Integer.valueOf(String.valueOf(recordPage.getTotalElements())));
        resultVO.setData(recordPage.getContent());
        return resultVO;
    }


}

