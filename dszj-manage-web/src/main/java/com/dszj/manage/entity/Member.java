package com.dszj.manage.entity;

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

import java.util.Date;

/**
 * @author: zhangsy
 * @Date: 2019/10/8
 * @Description:
 */
@Entity
@Table(name = "t_member")
@Data
@DynamicInsert
@DynamicUpdate
@EntityListeners(AuditingEntityListener.class)
public class Member {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;
    /**
     * 姓名
     */
    @Column(name="nickname",length=45)
    private String name;

    /**
     * 学生id
     */
    @Column(name = "student_id",columnDefinition = "int(10)")
    private Integer studentId;

    /**
     * 头像
     */
    @Column(name="header_pic")
    private String headerPic;

    /**
     * 微信openid
     */
    @Column(name = "open_id",length = 45)
    private String openId;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    @CreatedDate
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    @LastModifiedDate
    private Date updateTime;
    
}
