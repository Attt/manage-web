package com.dszj.manage.base;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * 基础Dao封装
 * @author yangyuesong
 *
 */
@NoRepositoryBean
public interface BaseDao<T> extends JpaRepository<T, Integer>, JpaSpecificationExecutor<T> {
	
    void deleteById(Integer id);
    void deleteByIdIn(List<Integer> ids);
    List<T> findByIdIn(List<Integer> ids);
    
}
