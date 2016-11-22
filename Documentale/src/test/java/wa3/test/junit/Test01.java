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
import wa3.model.Documenti;
import wa3.model.Tag;
import wa3.model.TagDocumenti;
import wa3.web.WebFactory;


public class Test01  {

	@BeforeClass
	public static void init() throws Exception{
		WebFactory.setCtx(new ClassPathXmlApplicationContext("applicationContext.xml"));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void test04() throws Exception {
		System.out.println("inizio");

		DocumentiDao documentiDao = WebFactory.getDocumentiDao();
		List<Documenti> lista = documentiDao.load(Restrictions.eq("id", "8a0ff748-fe60-4775-911f-d650c3ca3937"));
		if (!lista.isEmpty()){
			Documenti doc = lista.get(0);
			System.out.println("documento->"+doc);
			DetachedCriteria criteria2 = DetachedCriteria.forClass(TagDocumenti.class);
			criteria2.add(Restrictions.eq("idDocumenti", doc.getId()));
			criteria2.setProjection(Projections.property("idTag"));
			List<String> idList = (List<String>) documentiDao.load(criteria2);
			for (String idTag : idList) {
				System.out.println(idTag);
			}

			DetachedCriteria criteria = DetachedCriteria.forClass(Tag.class);
			criteria.add(Restrictions.in("id", idList)); //(Subqueries.propertiesIn(new String[]{"id"}, criteria2));
			TagDao td = WebFactory.getTagDao();
			List<Tag> lt = (List<Tag>) td.load(criteria);
			for (Tag tag : lt) {
				System.out.println(tag);
			}

		}
		System.out.println("finito");
	}

	@Test
	public void test05() throws Exception {
		System.out.println("inizio");

		DocumentiDao documentiDao = WebFactory.getDocumentiDao();
		List<Documenti> lista = documentiDao.load(Restrictions.eq("id", "8a0ff748-fe60-4775-911f-d650c3ca3937"));
		if (!lista.isEmpty()){
			Documenti doc = lista.get(0);
			System.out.println("documento->"+doc);
			List<Tag> lt = WebFactory.getTags(doc.getId());
			for (Tag tag : lt) {
				System.out.println(tag);
			}

		}
		System.out.println("finito");
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
