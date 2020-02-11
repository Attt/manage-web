package com.dszj.manage.entity;

import java.util.Date;

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

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Entity
@Table(name = "sys_user")
@Data
@DynamicInsert
@DynamicUpdate
@EntityListeners(AuditingEntityListener.class)
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	@Column(name = "username")
	private String username;
	@JsonIgnore
	@Column(name = "password")
	private String password;
	@JsonIgnore
	@Column(name = "salt")
	private String salt;
	@Column(name = "nickname")
	private String nickname;
	@Column(name = "head_pic")
	private String headPic;
	@Column(name = "sex")
	private Integer sex;
	@Column(name = "phone")
	private String phone;
	@Column(name = "email")
	private String email;
	@Column(name = "remark")
	private String remark;
	@Column(name = "dept_id")
	private Integer deptId;
	@Column(name = "[status]")
	private Integer status;
	@CreatedDate
	@Column(name = "create_time")
	private Date createTime;
	@LastModifiedDate
	@Column(name = "update_time")
	private Date updateTime;
	
	@Transient
	private String deptName;
	@Transient
	private Integer roleId;
	@Transient
	private String roleName;
}
