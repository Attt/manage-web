package com.dszj.manage.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Entity
@Table(name = "t_activity")
@DynamicInsert
@DynamicUpdate
@EntityListeners(AuditingEntityListener.class)
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    /**
     * 活动名称
     */
    @Column(name = "name",length = 45)
    private String name;

    /**
     * 活动类型 0-课程签到 1-活动签到
     */
    @Column(name = "type",columnDefinition = "int(1)")
    private Integer type;

    /**
     * 活动描述
     */
    @Column(name = "description")
    private String description;

    /**
     * 签到开始时间
     */
    @Column(name = "start_time")
    private Date startTime;

    /**
     * 创建人id
     */
    @Column(name = "creator_id",columnDefinition = "int(10)")
    private Integer creatorId;

    /**
     * 签到结束时间
     */
    @Column(name = "end_time")
    private Date endTime;

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
