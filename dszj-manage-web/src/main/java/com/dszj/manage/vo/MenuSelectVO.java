package com.dszj.manage.vo;

import com.dszj.manage.base.BaseSelectVO;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 菜单下拉树
 * @author yys
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class MenuSelectVO extends BaseSelectVO <MenuSelectVO>{
    private Integer type;
}
