package com.dszj.manage.base;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * 基础服务封装
 * 
 * @author yangyuesong
 *
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class BaseService<T> {
	@Autowired
	private BaseDao<T> baseDao;

	protected Class<T> clazz;

	public BaseService() {
		Class clazz = getClass();
		while (clazz != Object.class) {
			Type t = clazz.getGenericSuperclass();
			if (t instanceof ParameterizedType) {
				Type[] args = ((ParameterizedType) t).getActualTypeArguments();
				if (args[0] instanceof Class) {
					this.clazz = (Class<T>) args[0];
					break;
				}
			}
			clazz = clazz.getSuperclass();
		}
	}

	public T findById(Integer id) {
		return baseDao.findById(id).orElse(null);
	}

	public List<T> findByIdIn(List<Integer> ids) {
		return baseDao.findByIdIn(ids);
	}

	public List<T> findAll() {
		return baseDao.findAll();
	}

	public boolean exists(Integer id) {
		return baseDao.existsById(id);
	}

	@Transactional
	public T save(T entity) {
		return baseDao.save(entity);
	}

	@Transactional
	public T update(T entity) {
		return baseDao.save(entity);
	}

	@Transactional
	public List<T> save(List<T> entities) {
		return baseDao.saveAll(entities);
	}

	@Transactional
	public void delete(T entity) {
		baseDao.delete(entity);
	}

	@Transactional
	public void delete(List<T> list) {
		baseDao.deleteInBatch(list);
		;
	}

	@Transactional
	public void deleteById(Integer id) {
		baseDao.deleteById(id);
	}

	@Transactional
	public void deleteByIdIn(List<Integer> ids) {
		baseDao.deleteByIdIn(ids);
	}

	/**
	 * 分页查询数据列表
	 * 
	 * @param pageForm
	 * @return
	 * @author yys
	 */
	public Page<T> findPageList(PageForm<T> pageForm) {
		int pageNum = pageForm.getPageNum() - 1;
		int pageSize = pageForm.getPageSize();

		Specification<T> spec = handleSpec(pageForm);
		// 构建分页对象
		Pageable pageable = PageRequest.of(pageNum, pageSize);
		return baseDao.findAll(spec, pageable);
	}

	/**
	 * 查询数据列表
	 * 
	 * @param baseForm
	 * @return
	 * @author yys
	 */
	public List<T> findList(BaseForm<T> baseForm) {

		Specification<T> spec = handleSpec(baseForm);
		return baseDao.findAll(spec);
	}

	/**
	 * 构建 jpa Specification
	 * 
	 * @param baseForm
	 * @return
	 * @author yys
	 */
	private Specification<T> handleSpec(BaseForm<T> baseForm) {

		return new Specification<T>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<>();
				// 实体类字段与类型class映射Map对象
				Map<String, Class<?>> entityFieldClassMap = null;

				Class<?> formClazz = baseForm.getClass();

				// 1、处理DynamicWhere注解
				Field[] fields = formClazz.getDeclaredFields();

				for (Field field : fields) {
					if (field.isAnnotationPresent(DynamicWhere.class)) {
						DynamicWhere where = field.getAnnotation(DynamicWhere.class);
						if (where != null) {

							// 初始化操作实体类字段和字段类型映射map
							if (entityFieldClassMap == null) {
								entityFieldClassMap = new HashMap<>();
								for (Field entityField : clazz.getDeclaredFields()) {
									entityFieldClassMap.put(entityField.getName(), entityField.getType());
								}
							}
							// 判断DynamicWhere注解中field值，为空则使用form对象字段名称填充该值
							String fieldName = StringUtils.isEmpty(where.field()) ? field.getName() : where.field();
							Method method;
							try {
								method = (Method) baseForm.getClass().getMethod(getGetterMethod(field.getName()));
							} catch (NoSuchMethodException e) {
								throw new RuntimeException(
										"类[" + formClazz.getName() + "]中找不到[" + field.getName() + "]的getter方法", e);
							} catch (SecurityException e) {
								throw new RuntimeException(
										"未知错误：获取类[" + formClazz.getName() + "]中的[" + field.getName() + "]的getter方法失败",
										e);
							} // 获取查询对象fieldName的getter方法
							OptEnum opt = where.opt();
							Class<?> fieldType = field.getType();
							Object fieldValue;
							try {
								fieldValue = method.invoke(baseForm);
							} catch (Exception e) {
								throw new RuntimeException(
										"未知错误：调用类[" + formClazz.getName() + "]中的[" + method.getName() + "]方法失败", e);
							}

							// 验证form类型与实体类中的字段名称和类型是否一致
							if (entityFieldClassMap.get(fieldName) == null) {
								throw new RuntimeException("类[" + formClazz.getName() + "]@DynamicWhere注解中field字段["
										+ fieldName + "]在实体[" + clazz.getName() + "]中不存在");
							}
							
							//form对象中List类型字段处理
							if (fieldType.isAssignableFrom(List.class)) {
								try {
									// 获取form中的List的字段名称
									Field listField = formClazz.getDeclaredField(field.getName());
									// 判断是否使用泛型参数
									if (listField.getGenericType() instanceof ParameterizedType) {
										// 获取 list 字段的泛型参数
										ParameterizedType listGenericType = (ParameterizedType) listField
												.getGenericType();
										Type[] listActualTypeArguments = listGenericType.getActualTypeArguments();
										if (listActualTypeArguments[0] != entityFieldClassMap.get(fieldName)) {
											throw new RuntimeException(
													"类[" + formClazz.getName() + "]@DynamicWhere注解field字段["
															+ field.getName() + "]的泛型参数类型[" + listActualTypeArguments[0]
															+ "]与实体[" + clazz.getName() + "]中字段[" + fieldName + "]的类型["
															+ entityFieldClassMap.get(fieldName) + "]不一致");
										}
									} else {
										throw new RuntimeException("类[" + formClazz.getName() + "]@DynamicWhere注解的字段["
												+ field.getName() + "]泛型参数不能为空");
									}

								} catch (NoSuchFieldException e) {
									throw new RuntimeException(
											"类[" + formClazz.getName() + "]中找不到[" + field.getName() + "]字段", e);
								} catch (SecurityException e) {
									throw new RuntimeException(
											"未知错误：获取类[" + formClazz.getName() + "]中的[" + field.getName() + "]Field对象失败",
											e);
								}

							} else if (entityFieldClassMap.get(fieldName) != fieldType) {
								throw new RuntimeException("类[" + formClazz.getName() + "]@DynamicWhere注解中field["+fieldName+"]字段类型["
										+ fieldType + "]与实体[" + clazz.getName() + "]中["
										+ entityFieldClassMap.get(fieldName) + "]不一致");
							}

							handleWhere(root, predicates, cb, fieldName, fieldType, fieldValue, opt, true);
						}

					}
				}

				// 2、处理ExtraWhere注解
				ExtraWhere extraWhere = formClazz.getAnnotation(ExtraWhere.class);
				if (extraWhere != null) {
					String[] extraFields = extraWhere.field();
					String[] extraValues = extraWhere.value();
					OptEnum[] extraOpts = extraWhere.opt();
					Class[] fieldEnum = extraWhere.fieldEnum();

					if (fieldEnum.length == 0 || extraFields.length != extraValues.length
							|| extraValues.length != extraOpts.length || extraOpts.length != fieldEnum.length) {
						throw new RuntimeException(
								"类[" + formClazz.getName() + "]中的@ExtraWhere注解配置错误：field、value、opt、fieldEnum 数组大小不一致");
					}

					// 初始化操作实体类字段和字段类型映射map
					if (entityFieldClassMap == null) {
						entityFieldClassMap = new HashMap<>();
						for (Field entityField : clazz.getDeclaredFields()) {
							entityFieldClassMap.put(entityField.getName(), entityField.getType());
						}
					}

					for (int i = 0; i < extraFields.length; i++) {
						// 验证字段在实体类中是否存在
						if (entityFieldClassMap.get(extraFields[i]) == null) {
							throw new RuntimeException("类[" + formClazz.getName() + "]@ExtraWhere注解中field字段["
									+ extraFields[i] + "]在实体[" + clazz.getName() + "]中不存在");
						}
						handleWhere(root, predicates, cb, extraFields[i], entityFieldClassMap.get(extraFields[i]),
								extraValues[i], extraOpts[i], false);
					}

				}

				// 3、处理OrderBy注解
				OrderBy orderBy = formClazz.getAnnotation(OrderBy.class);
				if (orderBy != null) {
					String[] orderFields = orderBy.field();
					SortEnum[] sorts = orderBy.sort();

					if (orderFields.length == 0) {
						throw new RuntimeException("类[" + formClazz.getName() + "]中的@OrderBy注解配置错误：field不能为空");

					}
					if (orderFields.length != sorts.length) {
						throw new RuntimeException("类[" + formClazz.getName() + "]中的@OrderBy注解配置错误：field与sort数组大小不一致");
					}

					if (entityFieldClassMap == null) {
						entityFieldClassMap = new HashMap<>();
						for (Field field : clazz.getDeclaredFields()) {
							entityFieldClassMap.put(field.getName(), field.getType());
						}
					}

					for (int i = 0; i < orderFields.length; i++) {
						// 验证字段在实体类中是否存在
						if (entityFieldClassMap.get(orderFields[i]) == null) {
							throw new RuntimeException("类[" + formClazz.getName() + "]@OrderBy注解中field字段["
									+ orderFields[i] + "]在实体[" + clazz.getName() + "]中不存在");
						}
						handleOrder(root, query, predicates, cb, orderFields[i], entityFieldClassMap, sorts[i]);
					}

				}

				query.where(cb.and(predicates.toArray(new Predicate[0])));

				return query.getRestriction();
			}

		};
	}

	/**
	 * 排序处理
	 * 
	 * @param root
	 * @param predicates
	 * @param cb
	 * @param fieldName
	 * @param fieldType
	 * @param fieldValue
	 * @param opt
	 * @param flag：true
	 *            fieldValue为object类型 ; false 为String类型
	 * @author yys
	 * @throws Exception
	 */
	private void handleOrder(Root<T> root, CriteriaQuery<?> query, List<Predicate> predicates, CriteriaBuilder cb,
			String fields, Map<String, Class<?>> entityFieldClassMap, SortEnum sort) {

		if (sort == SortEnum.Desc) {
			for (String field : fields.split(",")) {
				query.orderBy(cb.desc(root.get(field)));
			}
		} else {
			for (String field : fields.split(",")) {
				query.orderBy(cb.asc(root.get(field)));
			}
		}

	}

	/**
	 * 动态查询条件处理
	 * 
	 * @param root
	 * @param predicates
	 * @param cb
	 * @param fieldName
	 * @param fieldType
	 * @param fieldValue
	 * @param opt
	 * @param flag
	 * @throws Exception
	 * @author yys
	 */
	private void handleWhere(Root<T> root, List<Predicate> predicates, CriteriaBuilder cb, String fieldName,
			Class<?> fieldType, Object fieldValue, OptEnum opt, boolean flag) {

		// ExtraWhere 占不支持实体字段类型Integer之外的类型
		if (!flag) {
			if (fieldType == null) {
				throw new RuntimeException("@ExtraWhere注解中的字段[" + fieldName + "]在实体类中不存在");
			}
			if (fieldType != Integer.class) {
				throw new RuntimeException("@ExtraWhere注解中的字段[" + fieldName + "]在实体类中的类型必须是Integer");
			}
		}

		if (fieldType == String.class) {
			String value = (String) fieldValue;
			if (!StringUtils.isEmpty(value)) {
				if (opt == OptEnum.Equals) {
					predicates.add(cb.equal(root.get(fieldName), value));
				} else if (opt == OptEnum.NotEquals) {
					predicates.add(cb.notEqual(root.get(fieldName), value));
				} else if (opt == OptEnum.Like) {
					predicates.add(cb.like(root.get(fieldName), "%" + value + "%"));
				} else if (opt == OptEnum.LeftLike) {
					predicates.add(cb.like(root.get(fieldName), "%" + value));
				} else if (opt == OptEnum.RightLike) {
					predicates.add(cb.like(root.get(fieldName), value + "%"));
				} else if (opt == OptEnum.GreaterThan) {
					predicates.add(cb.greaterThan(root.get(fieldName), value));
				} else if (opt == OptEnum.GreaterThanEquals) {
					predicates.add(cb.greaterThanOrEqualTo(root.get(fieldName), value));
				} else if (opt == OptEnum.LessThan) {
					predicates.add(cb.lessThan(root.get(fieldName), value));
				} else if (opt == OptEnum.LessThanEquals) {
					predicates.add(cb.lessThanOrEqualTo(root.get(fieldName), value));
				}
//				else if (opt == OptEnum.IsNull) {
//					predicates.add(cb.isNull(root.get(fieldName)));
//				} else if (opt == OptEnum.IsNotNull) {
//					predicates.add(cb.isNotNull(root.get(fieldName)));
//				} 
				else if (opt == OptEnum.In) {
					// Expression<String> expression =
					// root.<String>get(fieldName);
					// Expression expression =
					// root.get(fieldName).in(Arrays.asList(value));
					predicates.add(root.get(fieldName).in(value));

				}
			}
		} else if (fieldType == Integer.class || fieldType == int.class) {
			Integer value = null;
			List<Integer> values = null;
			if (flag) {
				value = (Integer) fieldValue;
			} else {
				//处理NotEquals查询
				if (opt == OptEnum.NotEquals) {
					value = Integer.valueOf((String)fieldValue);
				}
				//处理IN查询
				values = new ArrayList<>();
				String valueStr = (String) fieldValue;
				for (String str : valueStr.split(",")) {
					values.add(Integer.valueOf(str));
				}
			}

			if (!StringUtils.isEmpty(value)) {
				if (opt == OptEnum.Equals) {
					predicates.add(cb.equal(root.get(fieldName), value));
				} else if (opt == OptEnum.NotEquals) {
					predicates.add(cb.notEqual(root.get(fieldName), value));
				} else if (opt == OptEnum.Like) {
					predicates.add(cb.like(root.get(fieldName), "%" + value + "%"));
				} else if (opt == OptEnum.LeftLike) {
					predicates.add(cb.like(root.get(fieldName), "%" + value));
				} else if (opt == OptEnum.RightLike) {
					predicates.add(cb.like(root.get(fieldName), value + "%"));
				} else if (opt == OptEnum.GreaterThan) {
					predicates.add(cb.greaterThan(root.get(fieldName), value));
				} else if (opt == OptEnum.GreaterThanEquals) {
					predicates.add(cb.greaterThanOrEqualTo(root.get(fieldName), value));
				} else if (opt == OptEnum.LessThan) {
					predicates.add(cb.lessThan(root.get(fieldName), value));
				} else if (opt == OptEnum.LessThanEquals) {
					predicates.add(cb.lessThanOrEqualTo(root.get(fieldName), value));
				} 
//				else if (opt == OptEnum.IsNull) {
//					predicates.add(cb.isNull(root.get(fieldName)));
//				} else if (opt == OptEnum.IsNotNull) {
//					predicates.add(cb.isNotNull(root.get(fieldName)));
//				} 
				else if (opt == OptEnum.In) {
					predicates.add(root.get(fieldName).in(Arrays.asList(value)));
				}
			}

			if (values != null && values.size() > 0) {
				if (opt == OptEnum.In) {
					predicates.add(root.get(fieldName).in(values));
				}
			}

		} else if (fieldType == Long.class || fieldType == long.class) {
			Long value = flag ? (Long) fieldValue : Long.valueOf((String) fieldValue);
			if (!StringUtils.isEmpty(value)) {
				if (opt == OptEnum.Equals) {
					predicates.add(cb.equal(root.get(fieldName), value));
				} else if (opt == OptEnum.NotEquals) {
					predicates.add(cb.notEqual(root.get(fieldName), value));
				} else if (opt == OptEnum.Like) {
					predicates.add(cb.like(root.get(fieldName), "%" + value + "%"));
				} else if (opt == OptEnum.LeftLike) {
					predicates.add(cb.like(root.get(fieldName), "%" + value));
				} else if (opt == OptEnum.RightLike) {
					predicates.add(cb.like(root.get(fieldName), value + "%"));
				} else if (opt == OptEnum.GreaterThan) {
					predicates.add(cb.greaterThan(root.get(fieldName), value));
				} else if (opt == OptEnum.GreaterThanEquals) {
					predicates.add(cb.greaterThanOrEqualTo(root.get(fieldName), value));
				} else if (opt == OptEnum.LessThan) {
					predicates.add(cb.lessThan(root.get(fieldName), value));
				} else if (opt == OptEnum.LessThanEquals) {
					predicates.add(cb.lessThanOrEqualTo(root.get(fieldName), value));
				} 
//				else if (opt == OptEnum.IsNull) {
//					predicates.add(cb.isNull(root.get(fieldName)));
//				} else if (opt == OptEnum.IsNotNull) {
//					predicates.add(cb.isNotNull(root.get(fieldName)));
//				} 
				else if (opt == OptEnum.In) {
					predicates.add(root.get(fieldName).in(Arrays.asList(value)));
				}
			}
		} else if (fieldType == Double.class || fieldType == double.class) {
			Double value = flag ? (Double) fieldValue : Double.valueOf((String) fieldValue);
			if (!StringUtils.isEmpty(value)) {
				if (opt == OptEnum.Equals) {
					predicates.add(cb.equal(root.get(fieldName), value));
				} else if (opt == OptEnum.NotEquals) {
					predicates.add(cb.notEqual(root.get(fieldName), value));
				} else if (opt == OptEnum.Like) {
					predicates.add(cb.like(root.get(fieldName), "%" + value + "%"));
				} else if (opt == OptEnum.LeftLike) {
					predicates.add(cb.like(root.get(fieldName), "%" + value));
				} else if (opt == OptEnum.RightLike) {
					predicates.add(cb.like(root.get(fieldName), value + "%"));
				} else if (opt == OptEnum.GreaterThan) {
					predicates.add(cb.greaterThan(root.get(fieldName), value));
				} else if (opt == OptEnum.GreaterThanEquals) {
					predicates.add(cb.greaterThanOrEqualTo(root.get(fieldName), value));
				} else if (opt == OptEnum.LessThan) {
					predicates.add(cb.lessThan(root.get(fieldName), value));
				} else if (opt == OptEnum.LessThanEquals) {
					predicates.add(cb.lessThanOrEqualTo(root.get(fieldName), value));
				} 
//				else if (opt == OptEnum.IsNull) {
//					predicates.add(cb.isNull(root.get(fieldName)));
//				} else if (opt == OptEnum.IsNotNull) {
//					predicates.add(cb.isNotNull(root.get(fieldName)));
//				} 
				else if (opt == OptEnum.In) {
					predicates.add(root.get(fieldName).in(Arrays.asList(value)));
				}
			}
		} else if (fieldType == Float.class || fieldType == float.class) {
			Float value = flag ? (Float) fieldValue : Float.valueOf((String) fieldValue);
			if (!StringUtils.isEmpty(value)) {
				if (opt == OptEnum.Equals) {
					predicates.add(cb.equal(root.get(fieldName), value));
				} else if (opt == OptEnum.NotEquals) {
					predicates.add(cb.notEqual(root.get(fieldName), value));
				} else if (opt == OptEnum.Like) {
					predicates.add(cb.like(root.get(fieldName), "%" + value + "%"));
				} else if (opt == OptEnum.LeftLike) {
					predicates.add(cb.like(root.get(fieldName), "%" + value));
				} else if (opt == OptEnum.RightLike) {
					predicates.add(cb.like(root.get(fieldName), value + "%"));
				} else if (opt == OptEnum.GreaterThan) {
					predicates.add(cb.greaterThan(root.get(fieldName), value));
				} else if (opt == OptEnum.GreaterThanEquals) {
					predicates.add(cb.greaterThanOrEqualTo(root.get(fieldName), value));
				} else if (opt == OptEnum.LessThan) {
					predicates.add(cb.lessThan(root.get(fieldName), value));
				} else if (opt == OptEnum.LessThanEquals) {
					predicates.add(cb.lessThanOrEqualTo(root.get(fieldName), value));
				} 
//				else if (opt == OptEnum.IsNull) {
//					predicates.add(cb.isNull(root.get(fieldName)));
//				} else if (opt == OptEnum.IsNotNull) {
//					predicates.add(cb.isNotNull(root.get(fieldName)));
//				} 
				else if (opt == OptEnum.In) {
					predicates.add(root.get(fieldName).in(Arrays.asList(value)));
				}
			}
		} else if (fieldType == Date.class) {
			Date value = null;
			if (flag) {
				value = (Date) fieldValue;
			} else {
				try {
					value = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse((String) fieldValue);
				} catch (ParseException e) {
					throw new RuntimeException(
							"日期格式错误：@ExtraWhere注解中的字段[" + fieldName + "]值[" + fieldValue + "]转换为java.util.Date异常");
				}
			}

			if (!StringUtils.isEmpty(value)) {
				if (opt == OptEnum.Equals) {
					predicates.add(cb.equal(root.get(fieldName), value));
				} else if (opt == OptEnum.NotEquals) {
					predicates.add(cb.notEqual(root.get(fieldName), value));
				} else if (opt == OptEnum.Like) {
					predicates.add(cb.like(root.get(fieldName), "%" + value + "%"));
				} else if (opt == OptEnum.LeftLike) {
					predicates.add(cb.like(root.get(fieldName), "%" + value));
				} else if (opt == OptEnum.RightLike) {
					predicates.add(cb.like(root.get(fieldName), value + "%"));
				} else if (opt == OptEnum.GreaterThan) {
					predicates.add(cb.greaterThan(root.get(fieldName), value));
				} else if (opt == OptEnum.GreaterThanEquals) {
					predicates.add(cb.greaterThanOrEqualTo(root.get(fieldName), value));
				} else if (opt == OptEnum.LessThan) {
					predicates.add(cb.lessThan(root.get(fieldName), value));
				} else if (opt == OptEnum.LessThanEquals) {
					predicates.add(cb.lessThanOrEqualTo(root.get(fieldName), value));
				} 
//				else if (opt == OptEnum.IsNull) {
//					predicates.add(cb.isNull(root.get(fieldName)));
//				} else if (opt == OptEnum.IsNotNull) {
//					predicates.add(cb.isNotNull(root.get(fieldName)));
//				} 
				else if (opt == OptEnum.In) {
					predicates.add(root.get(fieldName).in(Arrays.asList(value)));
				}
			}
		} else if (fieldType == BigDecimal.class) {
			BigDecimal value = flag ? (BigDecimal) fieldValue : new BigDecimal((String) fieldValue);
			if (!StringUtils.isEmpty(value)) {
				if (opt == OptEnum.Equals) {
					predicates.add(cb.equal(root.get(fieldName), value));
				} else if (opt == OptEnum.NotEquals) {
					predicates.add(cb.notEqual(root.get(fieldName), value));
				} else if (opt == OptEnum.Like) {
					predicates.add(cb.like(root.get(fieldName), "%" + value + "%"));
				} else if (opt == OptEnum.LeftLike) {
					predicates.add(cb.like(root.get(fieldName), "%" + value));
				} else if (opt == OptEnum.RightLike) {
					predicates.add(cb.like(root.get(fieldName), value + "%"));
				} else if (opt == OptEnum.GreaterThan) {
					predicates.add(cb.greaterThan(root.get(fieldName), value));
				} else if (opt == OptEnum.GreaterThanEquals) {
					predicates.add(cb.greaterThanOrEqualTo(root.get(fieldName), value));
				} else if (opt == OptEnum.LessThan) {
					predicates.add(cb.lessThan(root.get(fieldName), value));
				} else if (opt == OptEnum.LessThanEquals) {
					predicates.add(cb.lessThanOrEqualTo(root.get(fieldName), value));
				} 
//				else if (opt == OptEnum.IsNull) {
//					predicates.add(cb.isNull(root.get(fieldName)));
//				} else if (opt == OptEnum.IsNotNull) {
//					predicates.add(cb.isNotNull(root.get(fieldName)));
//				} 
				else if (opt == OptEnum.In) {
					predicates.add(root.get(fieldName).in(Arrays.asList(value)));
				}
			}
		} else if (fieldType == BigInteger.class) {
			BigInteger value = flag ? (BigInteger) fieldValue : BigInteger.valueOf(Long.valueOf((String) fieldValue));
			if (!StringUtils.isEmpty(value)) {
				if (opt == OptEnum.Equals) {
					predicates.add(cb.equal(root.get(fieldName), value));
				} else if (opt == OptEnum.NotEquals) {
					predicates.add(cb.notEqual(root.get(fieldName), value));
				} else if (opt == OptEnum.Like) {
					predicates.add(cb.like(root.get(fieldName), "%" + value + "%"));
				} else if (opt == OptEnum.LeftLike) {
					predicates.add(cb.like(root.get(fieldName), "%" + value));
				} else if (opt == OptEnum.RightLike) {
					predicates.add(cb.like(root.get(fieldName), value + "%"));
				} else if (opt == OptEnum.GreaterThan) {
					predicates.add(cb.greaterThan(root.get(fieldName), value));
				} else if (opt == OptEnum.GreaterThanEquals) {
					predicates.add(cb.greaterThanOrEqualTo(root.get(fieldName), value));
				} else if (opt == OptEnum.LessThan) {
					predicates.add(cb.lessThan(root.get(fieldName), value));
				} else if (opt == OptEnum.LessThanEquals) {
					predicates.add(cb.lessThanOrEqualTo(root.get(fieldName), value));
				} 
//				else if (opt == OptEnum.IsNull) {
//					predicates.add(cb.isNull(root.get(fieldName)));
//				} else if (opt == OptEnum.IsNotNull) {
//					predicates.add(cb.isNotNull(root.get(fieldName)));
//				} 
				else if (opt == OptEnum.In) {
					predicates.add(root.get(fieldName).in(Arrays.asList(value)));
				}
			}
		} else if (fieldType.isAssignableFrom(List.class)) {
			List values = (List) fieldValue;
			;
			if (values != null && values.size() > 0) {
				if (opt == OptEnum.In) {
					predicates.add(root.get(fieldName).in(values));
				}
			}
		} else {
			throw new RuntimeException("字段[" + fieldName + "]的数据类型[" + fieldType + "]不支持");
		}
	}

	/**
	 * 通过fieldName 获取getter方法
	 * 
	 * @param fieldName
	 * @return
	 * @throws Exception
	 */
	private static String getGetterMethod(String fieldName) {
		byte[] items = fieldName.getBytes();
		items[0] = (byte) ((char) items[0] - 'a' + 'A');
		return "get" + new String(items);
	}

	// private static Pattern humpPattern = Pattern.compile("[A-Z]");
	// private static Pattern linePattern = Pattern.compile("_(\\w)");

	// /**
	// * 驼峰命名转下划线命名
	// *
	// * @param str
	// * @return
	// */
	// private static String humpToLine(String str) {
	// Matcher matcher = humpPattern.matcher(str);
	// StringBuffer sb = new StringBuffer();
	// while (matcher.find()) {
	// matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
	// }
	// matcher.appendTail(sb);
	// return sb.toString();
	// }
	//
	// /**下划线转驼峰命名
	// *
	// * @param str
	// * @return
	// * @author yys
	// */
	// private static String lineToHump(String str) {
	// str = str.toLowerCase();
	// Matcher matcher = linePattern.matcher(str);
	// StringBuffer sb = new StringBuffer();
	// while (matcher.find()) {
	// matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
	// }
	// matcher.appendTail(sb);
	// return sb.toString();
	// }

}
