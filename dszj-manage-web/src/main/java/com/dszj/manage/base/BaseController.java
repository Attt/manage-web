package com.dszj.manage.base;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import com.dszj.manage.exception.BizException;
import com.dszj.manage.exception.BizExceptionEnum;
import com.dszj.manage.utils.BeanCopyUtil;

/**
 * 控制器基类
 * 
 * @author yys
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class BaseController {
	/**
	 * 无业务数据成功返回
	 * 
	 * @return
	 */
	public ResultVO success() {
		ResultVO resultVO = new ResultVO();
		resultVO.setData(null);
		return resultVO;
	}

	/**
	 * 有业务数据成功返回
	 * 
	 * @param data
	 * @return
	 */
	public ResultVO success(Object data) {
		ResultVO resultVO = new ResultVO();
		resultVO.setData(data);
		return resultVO;
	}
	
	public ResultVO success(Page page) {
		ResultVO resultVO = new ResultVO();
		resultVO.setPageNum(page.getNumber());
		resultVO.setPageSize(page.getSize());
		resultVO.setTotal(Integer.valueOf(String.valueOf(page.getTotalElements())));
		resultVO.setData(page.getContent());
		return resultVO;
	}

	/**
	 * 成功返回并转换data对象类型为目标对象类型
	 * 
	 * @param data
	 * @param destinationClass
	 *            目标Class对象
	 * @return
	 */
	public ResultVO success(Object data, Class destinationClass) {
		ResultVO resultVO = new ResultVO();
		if (data != null && data instanceof Collection) {
			Collection collection = (Collection) data;
			resultVO.setData(BeanCopyUtil.convertList(collection, destinationClass));
		} else {
			resultVO.setData(data);
		}
		return resultVO;
	}
	
	public ResultVO success(Page page, Class destinationClass) {
		ResultVO resultVO = new ResultVO();
		resultVO.setPageNum(page.getNumber());
		resultVO.setPageSize(page.getSize());
		resultVO.setTotal(Integer.valueOf(String.valueOf(page.getTotalElements())));
		resultVO.setData(BeanCopyUtil.convertList(page.getContent(), destinationClass));
		return resultVO;
	}
	
	@InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }
	
	public void validateForm(BindingResult bindingResult) {
		 // 参数验证
        if (bindingResult.hasErrors()) {
            throw new BizException(BizExceptionEnum.PARAM_ERROR,Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
		
	}
	

}
