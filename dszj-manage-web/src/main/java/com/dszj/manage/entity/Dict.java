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
@Table(name="sys_dict")
@DynamicInsert 
@DynamicUpdate 
@EntityListeners(AuditingEntityListener.class)
public class Dict{
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;
    @Column(name="label")
    private String label;
    @Column(name="title")
    private String title;
    @Column(name="value")
    private String value;
    @Column(name="remark")
    private String remark;
    @CreatedDate
    @Column(name="create_time")
    private Date createTime;
    @LastModifiedDate
    @Column(name="update_time")
    private Date updateTime;
}
