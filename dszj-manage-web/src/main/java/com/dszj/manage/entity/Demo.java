package com.dszj.manage.entity;

import java.math.BigDecimal;
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

/**
 * 示例demo
 * 
 * @author yys
 */
@Data
@Entity
@DynamicInsert 
@DynamicUpdate  
@Table(name = "t_demo")
@EntityListeners(AuditingEntityListener.class)
public class Demo {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private Integer id;
	
	@Column(name="title")
	private String title;
	
	@Column(name="long_num")
	private Long longNum;
	
	@Column(name="float_num")
	private Float floatNum;
	
	@Column(name="double_num")
	private Double doubleNum;
	
	@Column(name="big_decimal_num")
	private BigDecimal bigDecimalNum;	
	
	@CreatedDate
	@Column(name="create_time")	
	private Date createTime;	
	
	@LastModifiedDate
	@Column(name="update_time")
	private Date updateTime;

}
