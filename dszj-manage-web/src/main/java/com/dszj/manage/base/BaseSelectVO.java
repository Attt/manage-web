package com.dszj.manage.base;

import java.util.List;

import lombok.Data;

/**
 * 下拉列表基础封装类型
 * @author yys
 */
@Data
public class BaseSelectVO <T> {
    private Integer id;
    private Integer parentId;
    private String name;
    private boolean checked = false;
    private boolean open = false;
    private boolean parent = false;
    private List<T> children;
    
}
