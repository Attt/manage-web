package com.dszj.manage.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Data;

@Data
@Entity
@Table(name = "sys_menu")
@DynamicInsert 
@DynamicUpdate 
@EntityListeners(AuditingEntityListener.class)
public class Menu {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "parent_id")
	private Integer parentId;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "url")
	private String url;
	
	@Column(name = "type")
	private Integer type;
	
	@Column(name = "label")
	private String label;

	@Column(name = "icon")
	private String icon;

	@Column(name = "order_num")
	private Integer orderNum;
	
	@Column(name = "remark")
	private String remark;
	
	@Column(name = "status")
	private Integer status;
	
	@Column(name = "is_leaf")
	private Integer isLeaf;
	
	@CreatedDate
	@Column(name = "create_time")
	private Date createTime;
	
	@LastModifiedDate
	@Column(name = "update_time")
	private Date updateTime;
	
	
	@Transient
	private List<Menu> children = new ArrayList<>();
	@Transient
	private boolean checked = false;
	
	
}
