package com.dszj.manage.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dszj.manage.base.BaseDao;
import com.dszj.manage.entity.Member;

/**
 * @author: zhangsy
 * @Date: 2019/10/8
 * @Description:
 */
public interface MemberDao extends BaseDao<Member> {
	
	   @Modifying
	   @Query(value = "update t_data set pic_status=1 where id in(:ids)",nativeQuery = true)
	   void updatePicStatus(@Param("ids") List<Integer> ids);
	   
	   @Modifying
	   @Query(value = "update t_data set name_status=1 where id in(:ids)",nativeQuery = true)
	   void updateNameStatus(@Param("ids") List<Integer> ids);
	   
	   @Query(value = "select count(1) from t_data where name_status=1 ",nativeQuery = true)
	   int  couontNickname();
	   @Query(value = "select count(1) from t_data where pic_status=1 ",nativeQuery = true)
	   int  couontHeaderpic();
}
