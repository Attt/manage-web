package com.dszj.manage.utils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import com.google.common.collect.Lists;

/**
 * Java对象拷贝、转换工具类
 * 
 * @author yys
 */
public final class BeanCopyUtil {

	private BeanCopyUtil() {
	}

	private static DozerBeanMapper dozer = new DozerBeanMapper();

	/**
	 * 源对象转换为目标对象
	 * 
	 * @param source
	 *            源对象
	 * @param destinationClass
	 *            目标Class对象
	 * @return 目标对象
	 */
	public static <T> T convertBean(Object source, Class<T> destinationClass) {
		return dozer.map(source, destinationClass);
	}

	/**
	 * 源List中对象转换为目标对象
	 * 
	 * @param sourceList
	 *            源对象List
	 * @param destinationClass
	 *            目标List中的Class对象
	 * @return 转换后的List集合
	 */
	public static <T> List<T> convertList(Collection<?> sourceList, Class<T> destinationClass) {
		List<T> destinationList = Lists.newArrayList();
		for (Object sourceObject : sourceList) {
			T destinationObject = dozer.map(sourceObject, destinationClass);
			destinationList.add(destinationObject);
		}
		return destinationList;
	}

	/**
	 * 将源对象的属性复制目标对象
	 * 
	 * @param source
	 *            源对象
	 * @param destinationObject
	 *            目标对象
	 */
	public static void copyBean(Object source, Object destinationObject) {
		dozer.map(source, destinationObject);
	}

	/**
	 * 拷贝不为空的属性覆盖目标对象（常用于查询更新场景）
	 * @param src 源对象
	 * @param target 目标对象
	 */
	public static void copyNotNullProperties(Object src, Object target) {
		BeanWrapper srcBean = new BeanWrapperImpl(src);
		PropertyDescriptor[] pds = srcBean.getPropertyDescriptors();
		Set<String> emptyName = new HashSet<>();
		for (PropertyDescriptor p : pds) {
			Object srcValue = srcBean.getPropertyValue(p.getName());
			if (srcValue == null)
				emptyName.add(p.getName());
		}
		String[] result = new String[emptyName.size()];
		BeanUtils.copyProperties(src, target, emptyName.toArray(result));
	}
	
	/**
	 * map转换为bean
	 * 
	 * @param clazz
	 * @param map
	 * @return
	 */
	public static Object map2bean(Class<?> clazz, Map<String, Object> map) {
		try {
			if (map == null || map.size() <= 0) {
				return null;
			}
			BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
			Object obj = clazz.newInstance();
			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
			for (int i = 0; i < propertyDescriptors.length; i++) {
				PropertyDescriptor descriptor = propertyDescriptors[i];
				String propertyName = descriptor.getName();

				if (map.containsKey(propertyName)) {
					Object value = map.get(propertyName);
					Object[] args = new Object[1];
					args[0] = value;
					descriptor.getWriteMethod().invoke(obj, args);
				}
			}
			return obj;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 将bean转换成map
	 * 
	 * @param obj
	 * @param
	 * @return
	 */
	public static Map<String, Object> bean2Map(Object obj) {

		if (obj == null) {
			return null;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
			for (PropertyDescriptor property : propertyDescriptors) {
				String key = property.getName();
				// 过滤class属性
				if (!"class".equals(key)) {
					// 得到property对应的getter方法
					Method getter = property.getReadMethod();
					Object value = getter.invoke(obj);
					// if (notNull) {
					// if (!(null == value || value.toString().trim().length()
					// == 0
					// || "null".equalsIgnoreCase(value.toString()))) {
					// map.put(key, value);
					// } else {
					// continue;
					// }
					// } else {
					map.put(key, value);
					// }
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 批量转换bean为map
	 * 
	 * @author yys
	 * @param list
	 * @return
	 */
	public static List<Map<String, Object>> List2Map(List<?> list) {
		List<Map<String, Object>> resultList = new ArrayList<>();
		for (Object obj : list) {
			resultList.add(bean2Map(obj));
		}
		return resultList;
	}

}