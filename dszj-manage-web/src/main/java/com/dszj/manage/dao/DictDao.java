package com.dszj.manage.dao;

import com.dszj.manage.base.BaseDao;
import com.dszj.manage.entity.Dict;

public interface DictDao extends BaseDao<Dict>  {

    public Dict findByLabel(String label);
    
}
