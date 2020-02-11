package com.dszj.manage.base;

import java.util.List;

import org.springframework.data.domain.Page;

import com.dszj.manage.utils.BeanCopyUtil;

import lombok.Data;

/**
 * 分页数据返回基础类封装
 * 
 * @author yys
 * @param <T>
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
@Data
public class PageVO<T>{
	/**
	 * 当前页号
	 */
	private Integer pageNum = 1;
	/**
	 * 每一页记录数
	 */
	private Integer pageSize = 10;
	/**
	 * 总记录数
	 */
	private Integer total;
	/**
	 * 总页数
	 */
	private Integer pages;
	/**
	 * 数据列表
	 */
	private List<T> list;

	/**
	 * 最大id
	 */
	private Integer maxId;

	public PageVO() {
		super();
	}
	
	public PageVO(Page page) {
		this.pageNum = page.getNumber()+1;
		this.pageSize = page.getSize();
		this.total = (int) page.getTotalElements();
		this.pages = page.getTotalPages();
		this.list = page.getContent();
	}
	
	/**
	 * 把分页中的List对象类型转成destinationClass类型
	 * @param page
	 * @param destinationClass 目标对象Class
	 */
	public PageVO(Page page,Class destinationClass) {
		this.pageNum = page.getNumber()+1;
		this.pageSize = page.getSize();
		this.total = (int) page.getTotalElements();
		this.pages = page.getTotalPages();
		this.list= BeanCopyUtil.convertList(page.getContent(), destinationClass);
	}
	/**
	 * 设置分页参数及数据列表
	 * @param page
	 * @param dataList 返回前台的vo对象
	 */
	public PageVO(Page page,List<T> dataList) {
		this.pageNum = page.getNumber()+1;
		this.pageSize = page.getSize();
		this.total = (int) page.getTotalElements();
		this.pages = page.getTotalPages();
		this.list= dataList;
	}

	/**
	 * 设置分页参数及数据列表
	 * @param page
	 * @param dataList 返回前台的vo对象
	 */
	public PageVO(Page page,List<T> dataList,Integer maxId) {
		this.pageNum = page.getNumber()+1;
		this.pageSize = page.getSize();
		this.total = (int) page.getTotalElements();
		this.pages = page.getTotalPages();
		this.list= dataList;
		this.maxId = maxId;
	}
	
}
