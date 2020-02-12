package com.dszj.manage.service;

import com.dszj.manage.base.BaseService;
import com.dszj.manage.dao.RecordDao;
import com.dszj.manage.entity.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RecordService extends BaseService<Record> {
    @Autowired
    private RecordDao recordDao;

    public Record findByActivityIdAndMemberId(Integer activityId,Integer memberId){
        return recordDao.findByActivityIdAndMemberId(activityId,memberId);
    }

}
