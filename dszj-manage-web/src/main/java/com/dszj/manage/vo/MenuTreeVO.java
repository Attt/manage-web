package com.dszj.manage.vo;

import com.dszj.manage.base.BaseTreeVO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class MenuTreeVO extends BaseTreeVO<MenuTreeVO> {
	private Integer type;
}
