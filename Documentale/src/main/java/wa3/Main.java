package wa3;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import wa3.dao.DocumentiDao;
import wa3.model.Documenti;

public class Main {

	static ApplicationContext applicationContext;

	static DocumentiDao ordineDao;


	public static void main(String[] args) throws Exception {
		applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");

		ordineDao = (DocumentiDao) applicationContext.getBean("ordineDao");
//		List<Ordine> list = ordineDao.load();
//		for (Ordine ordine : list) {
//			System.out.println(ordine);
//		}

		for (int i = 0; i < 5; i++) {
			Documenti ordine = createOrdine();
			System.out.println(ordine);
		}

		for (Documenti ordine : ordineDao.load()) {
			System.out.println(ordine);
			
		}

//		ordineDao.closeSession();

//		org.hsqldb.DatabaseManager.closeDatabases(0);
	}

	private static Documenti createOrdine() {
		// TODO Auto-generated method stub
		Documenti ordine = new Documenti();

		System.out.println(ordine.getId());
	
		ordineDao.save(ordine);
		ordineDao.flush();
		return ordine;
	}


}
