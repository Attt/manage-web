package com.dszj.manage.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dszj.manage.base.BaseService;
import com.dszj.manage.dao.DictDao;
import com.dszj.manage.entity.Dict;

@Service
public class DictService extends BaseService<Dict> {
	@Autowired
	private DictDao dictDao;

	public Dict findByLabel(String label) {
		return dictDao.findByLabel(label);
	}
}
