package wa3.test.junit;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import wa3.dao.DocumentiDao;
import wa3.model.Documenti;
import wa3.web.WebFactory;


public class TestDoc01  {

	@BeforeClass
	public static void init() throws Exception{
		WebFactory.setCtx(new ClassPathXmlApplicationContext("applicationContext.xml"));
	}

	@Test
	public void test01() throws Exception {

		DocumentiDao documentiDao = WebFactory.getDocumentiDao();
		List<Documenti> lista = documentiDao.load();
		for (Documenti documenti : lista) {
			System.out.println(documenti);
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
