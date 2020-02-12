package com.dszj.manage.entity;


import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "t_student")
@Data
@DynamicInsert
@DynamicUpdate
@EntityListeners(AuditingEntityListener.class)
public class Student {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    /**
     * 学号
     */
    @Column(name = "student_no",length = 25)
    private String studentNo;

    /**
     * 姓名
     */
    @Column(name = "student_name",length = 25)
    private String studentName;

    /**
     * 性别 0-男 1-女
     */
    @Column(name = "gender",columnDefinition = "int(1)")
    private Integer gender;

    /**
     * 班级id
     */
    @Column(name = "class_id",columnDefinition = "int(10)")
    private Integer classId;
}
