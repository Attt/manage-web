package com.dszj.manage.dao;


import com.dszj.manage.base.BaseDao;
import com.dszj.manage.entity.Student;

public interface StudentDao extends BaseDao<Student> {
    Student findByStudentNo(String studentNo);
}
