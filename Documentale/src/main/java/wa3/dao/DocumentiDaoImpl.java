package wa3.dao;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

import wa3.model.Documenti;
import wa3.model.Tag;
import wa3.model.TagDocumenti;

public class DocumentiDaoImpl extends DaoImpl<Documenti> implements DocumentiDao {

	public DocumentiDaoImpl() {
		super();
		setEntityClass(Documenti.class);
	}

	@Override
	public Serializable save(Documenti entity) {
		return getTransactionTemplate().execute(new TransactionCallback<Serializable>() {
			public Serializable doInTransaction(TransactionStatus status) {
				Session session = getSession();
				List<Tag> tags = entity.getTags();
				String id = (String) session.save(entity);
				saveTags(id, tags);
				return id;
			}
		});
	}

	@SuppressWarnings("unchecked")
	private void saveTags(String idDocumenti, List<Tag> tagsToSave) {
		Session session = getSession();

		DetachedCriteria criteria2 = DetachedCriteria.forClass(TagDocumenti.class);
		criteria2.add(Restrictions.eq("idDocumenti", idDocumenti));
		criteria2.setProjection(Projections.property("idTag"));
		Set<String> existingsIdTag = new HashSet<>();
		existingsIdTag.addAll((Collection<? extends String>) load(criteria2));

		for (Tag tag : tagsToSave) {
			if (!existingsIdTag.contains(tag.getId())){
				TagDocumenti td = new TagDocumenti();
				td.setIdDocumenti(idDocumenti);
				td.setIdTag(tag.getId());
				session.save(td);
			}
		}

		List<String> idToSave = new ArrayList<>();
		for (Tag tag : tagsToSave) {
			idToSave.add(tag.getId());
		}

		existingsIdTag.removeAll(idToSave);
		for (String tagIdToRemove : existingsIdTag) {
			DetachedCriteria criteria3 = DetachedCriteria.forClass(TagDocumenti.class);
			criteria3.add(Restrictions.eq("idDocumenti", idDocumenti));
			criteria3.add(Restrictions.eq("idTag", tagIdToRemove));
			List<TagDocumenti> dt = (List<TagDocumenti>) load(criteria3);
			if (dt!=null && !dt.isEmpty()){
				for (TagDocumenti tagDocumenti : dt) {
					session.remove(tagDocumenti);
				}
			}
		}

	}

	@Override
	public void update(Documenti entity) {
		 getTransactionTemplate().execute(new TransactionCallbackWithoutResult() {

			@Override
			public void doInTransactionWithoutResult(TransactionStatus status) {
				Session session = getSession();
				List<Tag> tags = entity.getTags();
				session.update(entity);
				saveTags(entity.getId(), tags);
			}
		});

	}

	@Override
	public void remove(Documenti entity) {
		 getTransactionTemplate().execute(new TransactionCallbackWithoutResult() {
			@Override
			public void doInTransactionWithoutResult(TransactionStatus status) {
				Session session = getSession();
				removeTags(entity.getId());
				session.remove(entity);
			}
		});
	}

	@SuppressWarnings("unchecked")
	private void removeTags(String idDocumenti) {
		Session session = getSession();

		DetachedCriteria criteria2 = DetachedCriteria.forClass(TagDocumenti.class);
		criteria2.add(Restrictions.eq("idDocumenti", idDocumenti));
		criteria2.setProjection(Projections.property("idTag"));
		Set<String> existingsIdTag = new HashSet<>();
		existingsIdTag.addAll((Collection<? extends String>) load(criteria2));

		for (String tagIdToRemove : existingsIdTag) {
			DetachedCriteria criteria3 = DetachedCriteria.forClass(TagDocumenti.class);
			criteria3.add(Restrictions.eq("idDocumenti", idDocumenti));
			criteria3.add(Restrictions.eq("idTag", tagIdToRemove));
			List<TagDocumenti> dt = (List<TagDocumenti>) load(criteria3);
			if (dt!=null && !dt.isEmpty()){
				for (TagDocumenti tagDocumenti : dt) {
					session.remove(tagDocumenti);
				}
			}
		}

	}



}
