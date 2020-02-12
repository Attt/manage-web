package com.dszj.manage.service;


import com.dszj.manage.dao.RecordDao;
import com.dszj.manage.entity.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	@Autowired
	private RecordDao recordDao;
	
	public Member findByOpenId(String openId){
		return memberDao.findByOpenId(openId);
	}

	/**
	 * 更新用户信息 添加签到记录
	 * @param member
	 * @param record
	 */
	public void saveInfo(Member member, Record record){
		memberDao.save(member);
		recordDao.save(record);
	}

}
