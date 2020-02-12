package com.dszj.manage.dao;

import java.util.List;


import com.dszj.manage.base.BaseDao;
import com.dszj.manage.entity.Member;

/**
 * @author: zhangsy
 * @Date: 2019/10/8
 * @Description:
 */
public interface MemberDao extends BaseDao<Member> {
	Member findByOpenId(String openId);
}
