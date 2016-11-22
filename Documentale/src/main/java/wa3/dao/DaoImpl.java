package wa3.dao;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

@Transactional
public class DaoImpl<T> implements Dao<T> {

	private Class<T> entityClass;

	private SessionFactory sessionFactory;

	private TransactionTemplate transactionTemplate;

	protected TransactionTemplate getTransactionTemplate() {
		return transactionTemplate;
	}

	public void setTransactionManager(PlatformTransactionManager transactionManager) {
		this.transactionTemplate = new TransactionTemplate(transactionManager);
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public Class<T> getEntityClass() {
		return entityClass;
	}

	public void setEntityClass(Class<T> entityClass) {
		this.entityClass = entityClass;
	}

	public List<T> load(Criterion ...criterions) {
		return this.transactionTemplate.execute(new TransactionCallback<List<T>>() {

			@SuppressWarnings("unchecked")
			@Override
			public List<T> doInTransaction(TransactionStatus status) {
				DetachedCriteria c = DetachedCriteria.forClass(entityClass);
				if (criterions!=null)
					for (int i = 0; i < criterions.length; i++) {
						Criterion criterion = criterions[i];
						if (criterion!=null)
							c.add(criterion);
					}
				Session session;
				try {
					session = sessionFactory.getCurrentSession();
				} catch (Exception e) {
					session = sessionFactory.openSession();
				}
				Criteria ec = c.getExecutableCriteria(session);
				return ec.list();
			}
		});

	}

	@Override
	public Serializable save(T entity) {
		return this.transactionTemplate.execute(new TransactionCallback<Serializable>() {
			public Serializable doInTransaction(TransactionStatus status) {
				Session session = getSession();
				return session.save(entity);
			}
		});
	}

	protected Session getSession() {
		Session session;
		try {
			session = sessionFactory.getCurrentSession();
		} catch (Exception e) {
			session = sessionFactory.openSession();
		}
		return session;
	}

	@Override
	public void remove(T entity) {
		this.transactionTemplate.execute(new TransactionCallbackWithoutResult() {
			public void doInTransactionWithoutResult(TransactionStatus status) {
				Session session = getSession();
				session.remove(entity);
				status.isCompleted();
				session.flush();
			}
		});
	}

	@Override
	public void flush() {
		Session session;
		try {
			session = sessionFactory.getCurrentSession();
		} catch (Exception e) {
			session = sessionFactory.openSession();
		}
		session.flush();

	}

	public void closeSession(){
		Session session;
		try {
			session = sessionFactory.getCurrentSession();
		} catch (Exception e) {
			session = sessionFactory.openSession();
		}
		session.close();

	}

	@Override
	public T getById(Serializable id) {
		return this.transactionTemplate.execute(new TransactionCallback<T>() {

			@Override
			public T doInTransaction(TransactionStatus status) {
				Session session;
				try {
					session = sessionFactory.getCurrentSession();
				} catch (Exception e) {
					session = sessionFactory.openSession();
				}
				return session.get(entityClass, id);
			}
		});
	}

	@Override
	public void update(T entity) {
		this.transactionTemplate.execute(new TransactionCallbackWithoutResult() {
			public void doInTransactionWithoutResult(TransactionStatus status) {
				Session session;
				try {
					session = sessionFactory.getCurrentSession();
				} catch (Exception e) {
					session = sessionFactory.openSession();
				}
				session.update(entity);
			}
		});

	}

	public List<?> load(DetachedCriteria criteria) {
		if (criteria==null)
			return Collections.emptyList();
		return this.transactionTemplate.execute(new TransactionCallback<List<?>>() {

			@Override
			public List<?> doInTransaction(TransactionStatus status) {

				Session session;
				try {
					session = sessionFactory.getCurrentSession();
				} catch (Exception e) {
					session = sessionFactory.openSession();
				}
				Criteria ec = criteria.getExecutableCriteria(session);
				return ec.list();
			}
		});

	}

}
