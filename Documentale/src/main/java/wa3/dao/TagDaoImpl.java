package wa3.dao;


import wa3.model.Tag;

public class TagDaoImpl extends DaoImpl<Tag> implements TagDao {

	public TagDaoImpl() {
		super();
		setEntityClass(Tag.class);
	}


}
