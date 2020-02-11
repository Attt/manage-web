package com.dszj.manage.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dszj.manage.base.BaseService;
import com.dszj.manage.dao.MemberDao;
import com.dszj.manage.entity.Member;

/**
 * @author: zhangsy
 * @Date: 2019/10/8
 * @Description:
 */
@Service
public class MemberService extends BaseService<Member> {

	@Autowired
	private MemberDao memberDao;
	
	@Transactional
	public void batchUpdate(boolean flag,List<Integer> ids) {
		if (flag) {
			memberDao.updateNameStatus(ids);
		}else{
			memberDao.updatePicStatus(ids);
		}
	}

}
