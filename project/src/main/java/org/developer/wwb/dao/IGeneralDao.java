package org.developer.wwb.dao;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.developer.wwb.core.exception.AppDaoException;


public interface IGeneralDao {

	/**
	 * 查询
	 * 
	 * @param entityClass
	 * @param primaryKey
	 * @return
	 * @throws AppDaoException
	 */
	public <T> T getEntityByID(Class<T> entityClass, Object primaryKey) throws AppDaoException;

	/**
	 * 保存
	 * 
	 * @param entity
	 * @throws AppDaoException
	 */
	public void save(Object entity) throws AppDaoException;

	/**
	 * 更新或保存
	 * 
	 * @param entity
	 * @throws AppDaoException
	 */
	public void updateOrSave(Object entity) throws AppDaoException;

	/**
	 * 删除
	 * 
	 * @param entityClass
	 * @param primaryKey
	 * @throws AppDaoException
	 */
	public void delete(Class<?> entityClass, Object primaryKey) throws AppDaoException;

	/**
	 * 查询所有entity order by
	 * 
	 * @param entityClass
	 * @param orderBy
	 * @return
	 * @throws AppDaoException
	 */
	public List<?> getResultListOrderBy(Class<?> entityClass, LinkedHashMap<String, String> orderBy)
			throws AppDaoException;

	/**
	 * 查询单个entityClass
	 * 
	 * @param entityClass
	 * @param params
	 * @return
	 * @throws AppDaoException
	 */
	public Object getEntity(Class<?> entityClass, Map<String, Object> params) throws AppDaoException;

	/**
	 * 检查参数是否存在记录
	 * 
	 * @param entityClass
	 * @param params
	 * @return
	 * @throws AppDaoException
	 */
	public long checkisExisted(Class<?> entityClass, Map<String, Object> params) throws AppDaoException;

	/**
	 * 查询所有entityList
	 * 
	 * @param entityClass
	 * @param params
	 * @param orderBy
	 * @return
	 * @throws AppDaoException
	 */
	public List<?> getResultList(Class<?> entityClass, Map<String, Object> params,
			LinkedHashMap<String, String> orderBy) throws AppDaoException;

	public <T> List<T> getEntityByParams(Class<?> entityClass, Map<String, Object> params) throws AppDaoException;

	/**
	 * 求字段的最大值
	 * 
	 * @param entityClass
	 * @param field
	 * @return
	 */
	public Integer getMaxValue(Class<?> entityClass, String field);

	public <T> List<T> getEntitiesBySql(String sql, Object... params);

	public void saveWithOutfulsh(Object entity) throws AppDaoException;

	public void fulsh() throws AppDaoException;

	public <T> List<T> getEntitiesByNativeSql(String sql, Object... params);
}
