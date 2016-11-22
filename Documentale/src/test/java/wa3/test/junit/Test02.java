package wa3.test.junit;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import wa3.dao.DocumentiDao;
import wa3.dao.TagDao;
import wa3.dao.TagDocumentiDao;
import wa3.model.Documenti;
import wa3.model.Tag;
import wa3.model.TagDocumenti;
import wa3.web.WebFactory;


public class Test02  {

	@BeforeClass
	public static void init() throws Exception{
		WebFactory.setCtx(new ClassPathXmlApplicationContext("applicationContext.xml"));
	}

	@Test
	public void test01() throws Exception {

		DocumentiDao documentiDao = WebFactory.getDocumentiDao();
		TagDao tagDao = WebFactory.getTagDao();
		List<Documenti> lista = documentiDao.load();
		for (Documenti documenti : lista) {
			System.out.println(documenti);
//			DetachedCriteria criteria2 = DetachedCriteria.forClass(TagDocumenti.class);
//			criteria2.add(Restrictions.eq("idDocumenti", documenti.getId()));
//			criteria2.setProjection(Projections.property("idTag"));
//			List<String> x = (List<String>) tagDao.load(criteria2);
//			for (String tag : x) {
//				System.out.println(tag);
//			}
			List<Tag> t = documenti.getTags();
			for (Tag tag : t) {
				System.out.println(tag);
			}
		}
	}

	@Test
	public void test02() throws Exception {

		TagDocumentiDao documentiDao = WebFactory.getTagDocumentiDao();
		List<TagDocumenti> lista = documentiDao.load();
		for (TagDocumenti documenti : lista) {
			System.out.println(documenti);
		}
	}

	@Test
	public void test03() throws Exception {

		TagDao documentiDao = WebFactory.getTagDao();
		List<Tag> lista = documentiDao.load();
		for (Tag documenti : lista) {
			System.out.println(documenti);
		}
	}


	@Test
	public void test04() throws Exception {

		TagDao documentiDao = WebFactory.getTagDao();
		List<Tag> lista = documentiDao.load();
		if (!lista.isEmpty()){
			Tag doc = lista.get(0);
			DetachedCriteria criteria2 = DetachedCriteria.forClass(TagDocumenti.class);
			criteria2.add(Restrictions.eq("idDocumenti", doc.getId()));
			criteria2.setProjection(Projections.property("idTag"));
			List<String> idList = (List<String>) documentiDao.load(criteria2);
			for (String idDoc : idList) {
				System.out.println(idDoc);
			}

//			DetachedCriteria criteria = DetachedCriteria.forClass(Tag.class);
//			criteria.add(Restrictions.in("id", idList)); //(Subqueries.propertiesIn(new String[]{"id"}, criteria2));

		}
	}

//	@Test(expected=DataIntegrityViolationException.class)
//	public void test03(){
//		RigaDao rigaDao = WebFactory.getRigaDao();
//		List<Riga> list = rigaDao.load();
//		for (Riga riga : list) {
//			System.out.println(riga);
//			rigaDao.save(riga);
//		}
//	}

}
