package wa3.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import wa3.dao.DocumentiDao;

import wa3.model.Documenti;


public class Main {

	static ApplicationContext applicationContext;

	static DocumentiDao documentiDao;

	public static void main(String[] args) throws Exception {
		applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");

		documentiDao = (DocumentiDao) applicationContext.getBean("documentiDao");
	
//		List<Documenti> list = documentiDao.load();
//		for (Documenti documenti : list) {
//			System.out.println(documenti);
//		}

		for (int i = 0; i < 5; i++) {
			Documenti documenti = createDocumenti();
			System.out.println(documenti);
		}

		for (Documenti documenti : documentiDao.load()) {
			System.out.println(documenti);
			
		}

//		documentiDao.closeSession();

//		org.hsqldb.DatabaseManager.closeDatabases(0);
	}

	private static Documenti createDocumenti() {
		// TODO Auto-generated method stub
		Documenti documenti = new Documenti();
		
		System.out.println(documenti.getId());
		
		
		documenti.setMime("xx");
		documenti.setNome("xxx-"+System.currentTimeMillis());
		
		
		documentiDao.save(documenti);
		documentiDao.flush();
		return documenti;
	}


}
