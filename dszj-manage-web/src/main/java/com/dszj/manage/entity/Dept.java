package com.dszj.manage.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Data;

@Data
@Entity
@Table(name = "sys_dept")
@DynamicInsert 
@DynamicUpdate 
@EntityListeners(AuditingEntityListener.class)
public class Dept {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	// 部门名称
	@Column(name = "name")
	private String name;
	// 父级编号
	@Column(name = "parent_id")
	private Integer parentId;
	// 排序
	@Column(name = "order_num")
	private Integer orderNum;
	// 备注
	@Column(name = "remark")
	private String remark;
	// 创建时间
	@CreatedDate
	@Column(name = "create_time")
	private Date createTime;
	// 更新时间
	@LastModifiedDate
	@Column(name = "update_time")
	private Date updateTime;

}
