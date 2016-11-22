package wa3.dao;


import wa3.model.TagDocumenti;

public class TagDocumentiDaoImpl extends DaoImpl<TagDocumenti> implements TagDocumentiDao {

	public TagDocumentiDaoImpl() {
		super();
		setEntityClass(TagDocumenti.class);
	}


}
