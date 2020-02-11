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
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Data;

/**
 * @author: zhangsy
 * @Date: 2019/10/8
 * @Description:
 */
@Entity
@Table(name = "t_data")
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
     * 昵称
     */
    @Column(name="nickname",length=45)
    private String nickname;
    /**
     * 头像
     */
    @Column(name="headerpic",length=255)
    private String headerPic;
    
    @Column(name="pic_status",length=255)
    private String picStatus;
    
    @Column(name="name_status",length=255)
    private String nameStatus;
    
}
