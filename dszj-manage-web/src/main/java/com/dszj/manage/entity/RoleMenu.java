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
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Data;

@Entity
@Table(name = "sys_role_menu")
@Data
@DynamicInsert 
@DynamicUpdate 
@EntityListeners(AuditingEntityListener.class)
public class RoleMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;
    @Column(name="role_id")
    private Integer roleId;
    @Column(name="menu_id")
    private Integer menuId;
    @CreatedDate
    @Column(name="create_time")
    private Date createTime;
}
