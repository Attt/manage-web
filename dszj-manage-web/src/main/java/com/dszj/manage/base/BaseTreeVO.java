package com.dszj.manage.base;

import java.util.List;

import lombok.Data;

/**
 * 基础树形列表封装
 * @author yys
 */
@Data
public class BaseTreeVO <T>{
    private Integer id;
    private Integer parentId;
    private String title;
    private boolean checked= false;
    private boolean spread = false;
    private List<T> children;
}
