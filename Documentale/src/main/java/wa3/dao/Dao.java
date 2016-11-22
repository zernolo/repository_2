package wa3.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;

public interface Dao<T> {

	public List<T> load(Criterion ...criterions);

	public Serializable save(T entity);

	public void remove(T entity);

	public void update(T entity);

	public void flush();

	public void closeSession();

	public T getById(Serializable id);

	public List<?> load(DetachedCriteria criteria);


}
