package  org.developer.wwb.dao.impl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.developer.wwb.core.exception.AppDaoException;
import org.developer.wwb.dao.IGeneralDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;


@Repository
public  class GeneralDaoImpl implements IGeneralDao {
	private static final Logger LOGGER = LoggerFactory.getLogger(GeneralDaoImpl.class);

	@PersistenceContext
	protected EntityManager em;

	@Override
	public <T> T getEntityByID(Class<T> entityClass, Object primaryKey) throws AppDaoException {
		T obj = em.find(entityClass, primaryKey);
		return obj;
	}

	@Override
	public void save(Object entity) throws AppDaoException {
		try {
			em.persist(entity);
			em.flush();
		} catch (Exception e) {
			LOGGER.error("Failed to save entity(" + entity.getClass().getName() + "): {}", e.getMessage());
			throw new AppDaoException(e);
		}
	}

	@Override
	public void saveWithOutfulsh(Object entity) throws AppDaoException {
		try {
			em.merge(entity);
		} catch (Exception e) {
			LOGGER.error("Failed to save entity(" + entity.getClass().getName() + "): {}", e.getMessage());
			throw new AppDaoException(e);
		}
	}

	@Override
	public void fulsh() throws AppDaoException {
		try {
			em.flush();
		} catch (Exception e) {
			LOGGER.error("Failed to flush", e.getMessage());
			throw new AppDaoException(e);
		}
	}

	@Override
	public void updateOrSave(Object entity) throws AppDaoException {
		try {
			em.merge(entity);
			em.flush();
		} catch (Exception e) {
			LOGGER.error("Failed to update or save entity(" + entity.getClass().getName() + "): {}", e.getMessage());
			throw new AppDaoException(e);
		}
	}

	@Override
	public void delete(Class<?> entityClass, Object primaryKey) throws AppDaoException {
		try {
			em.remove(em.getReference(entityClass, primaryKey));
			em.flush();
		} catch (Exception e) {
			LOGGER.error("Failed to remove entity(" + entityClass.getClass().getName() + "): {}", e.getMessage());
			throw new AppDaoException(e);
		}
	}

	@Override
	public List<?> getResultListOrderBy(Class<?> entityClass, LinkedHashMap<String, String> orderBy)
			throws AppDaoException {
		try {
			String entityName = entityClass.getSimpleName();
			Query query = em.createQuery("select o from " + entityName + " o " + buildOrderby(orderBy));
			List<?> list = query.getResultList();
			return list;
		} catch (Exception e) {
			LOGGER.error("Failed to find " + entityClass.getSimpleName() + " : {}", e.getMessage());
			throw new AppDaoException(e);
		}
	}

	@Override
	public List<?> getResultList(Class<?> entityClass, Map<String, Object> params,
			LinkedHashMap<String, String> orderBy) throws AppDaoException {
		try {
			String entityName = entityClass.getSimpleName();
			Query query = em
					.createQuery("select o from " + entityName + " o " + buildParams(params) + buildOrderby(orderBy));
			List<?> list = query.getResultList();
			return list;
		} catch (Exception e) {
			LOGGER.error("Failed to find " + entityClass.getSimpleName() + " : {}", e.getMessage());
			throw new AppDaoException(e);
		}
	}

	private String buildOrderby(LinkedHashMap<String, String> orderBy) {
		StringBuffer sb = new StringBuffer();
		if (orderBy != null && orderBy.size() > 0) {
			sb.append(" order by ");
			for (String key : orderBy.keySet()) {
				sb.append(" o." + key + " " + orderBy.get(key));
				sb.append(",");
			}
			sb.deleteCharAt(sb.length() - 1);
		}
		return sb.toString();
	}

	@Override
	public Object getEntity(Class<?> entityClass, Map<String, Object> params) throws AppDaoException {
		try {
			String entityName = entityClass.getSimpleName();
			Query query = em.createQuery("select o from " + entityName + " o " + buildParams(params));
			Object entity = query.getSingleResult();
			return entity;
		} catch (NoResultException e) {
			LOGGER.debug("There is no record");
			return null;
		} catch (NonUniqueResultException e) {
			LOGGER.error("More than one record");
			throw new AppDaoException(e);
		} catch (Exception e) {
			LOGGER.error("The query fails");
			throw new AppDaoException(e);
		}
	}

	private String buildParams(Map<String, Object> params) {
		StringBuffer sb = new StringBuffer();
		if (params != null && params.size() > 0) {
			sb.append("where ");
			int i = 0;
			for (String key : params.keySet()) {
				if (i > 0) {
					sb.append(" and");
				}
				sb.append(" o." + key + "=");
				Object obj = params.get(key);
				if (obj instanceof String) {
					sb.append("'" + (String) obj + "'");
				} else if (obj instanceof Integer) {
					sb.append((Integer) obj);
				} else if (obj instanceof Long) {
					sb.append((Long) obj);
				} else if (obj instanceof Double) {
					sb.append((Double) obj);
				} else if (obj instanceof Boolean) {
					sb.append((Boolean) obj);
				}
				i++;
			}
		}
		return sb.toString();
	}

	@Override
	public long checkisExisted(Class<?> entityClass, Map<String, Object> params) throws AppDaoException {
		try {
			String entityName = entityClass.getSimpleName();
			Query query = em.createQuery("select COUNT(*) from " + entityName + " o " + buildParams(params));
			long count = (Long) query.getSingleResult();
			return count;
		} catch (Exception e) {
			LOGGER.error("Failed to check {}", e.getMessage());
			throw new AppDaoException(e);
		}
	}

	@Override
	public <T> List<T> getEntityByParams(Class<?> entityClass, Map<String, Object> params) throws AppDaoException {
		try {
			String entityName = entityClass.getSimpleName();
			Query query = em.createQuery("select o from " + entityName + " o " + buildParams(params));
			@SuppressWarnings("unchecked")
			List<T> list = (List<T>) query.getResultList();
			return list;
		} catch (Exception e) {
			LOGGER.error("Failed to check {}", e.getMessage());
			throw new AppDaoException(e);
		}
	}

	@Override
	public Integer getMaxValue(Class<?> entityClass, String field) {
		String entityName = entityClass.getSimpleName();
		Query query = em.createQuery("select MAX(o." + field + ") from " + entityName + " o ");
		return (Integer) query.getSingleResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> getEntitiesBySql(String sql, Object... params) {
		Query query = em.createQuery(sql);
		for (int i = 1; i <= params.length; i++) {
			query.setParameter(i, params[i - 1]);
		}
		return (List<T>) query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> getEntitiesByNativeSql(String sql, Object... params) {
		Query query = em.createNativeQuery(sql);
		for (int i = 1; i <= params.length; i++) {
			query.setParameter(i, params[i - 1]);
		}
		return (List<T>) query.getResultList();
	}

}
