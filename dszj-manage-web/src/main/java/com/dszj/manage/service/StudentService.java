package com.dszj.manage.service;

import com.dszj.manage.base.BaseService;
import com.dszj.manage.dao.StudentDao;
import com.dszj.manage.entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentService extends BaseService<Student> {
    @Autowired
    private StudentDao studentDao;

    public Student findByStudentNo(String studentNo){
        return studentDao.findByStudentNo(studentNo);
    }
}
