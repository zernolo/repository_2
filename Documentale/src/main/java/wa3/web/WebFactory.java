package wa3.web;

import java.io.File;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import wa3.dao.DocumentiDao;
import wa3.dao.TagDao;
import wa3.dao.TagDocumentiDao;
import wa3.model.Documenti;
import wa3.model.Tag;
import wa3.model.TagDocumenti;


public class WebFactory implements ServletContextListener {

	private static final Logger logger = LoggerFactory.getLogger(WebFactory.class);

	private static ApplicationContext ctx;

	private static String _repositoryRoot;

	public static String getRepositoryRoot() {
		return WebFactory._repositoryRoot;
	}

	public static void setRepositoryRoot(String repositoryRoot) {
		WebFactory._repositoryRoot = repositoryRoot;
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		logger.info("context destroyed:"+ctx);
		ctx = null;
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(event.getServletContext());
		//		repositoryRoot = System.getProperty("repositoryRoot");
		logger.info("context loaded:"+ctx);
	}

	public static ApplicationContext getCtx() {
		return ctx;
	}

	public static void setCtx(ApplicationContext ctx) {
		WebFactory.ctx = ctx;
	}

	@SuppressWarnings("unchecked")
	public static <T> T getBean(String id){
		if (ctx==null) throw new IllegalStateException("WebApplicationContext non presente");
		return (T)ctx.getBean(id);
	}

	public static <T> T getBean(Class<T> klass){
		if (ctx==null) throw new IllegalStateException("WebApplicationContext non presente");
		return (T)ctx.getBean(klass);
	}



	public static DocumentiDao getDocumentiDao() {
		return getBean("documentiDao");
	}
	public static byte[] getDatiDocumento(String id){
		if(StringUtils.isBlank(id)){
			throw new IllegalArgumentException("id mancante");
		}
		File file = new File(_repositoryRoot+File.separator+id+".docData");
		if(file.exists()){
			try {
				return FileUtils.readFileToByteArray(file);
			} catch (Exception e) {
				logger.error("errore in lettura id: "+id+" "+ExceptionUtils.getRootCauseMessage(e),e);
			}

		}
		return null;
	}

	public static void setDatiDocumento(String id, byte[] dati) {

		if(StringUtils.isBlank(id)){
			throw new IllegalArgumentException("id mancante");
		}
		if(dati == null){
			throw new IllegalArgumentException("dati mancanti");
		}
		if(WebFactory._repositoryRoot == null){
			throw new IllegalArgumentException("WebFactory.repositoryRoot mancante");
		}
		File file = new File(WebFactory._repositoryRoot);
		if(file.exists()){
			try {
				FileUtils.writeByteArrayToFile(new File(_repositoryRoot+File.separator+id+".docData"), dati);
			} catch (Exception e) {
				logger.error("errore in lettura id: "+id+" "+ExceptionUtils.getRootCauseMessage(e),e);
			}

		}else{
			throw new IllegalStateException("repositoryRoot non configurato");
		}
	}

	public static List<Documenti> getFigli(String idPadre){
		if(StringUtils.isNotBlank(idPadre)){
			DocumentiDao documentiDao = getDocumentiDao();
			Documenti padre = documentiDao.getById(idPadre);
			if (padre!=null && Boolean.TRUE.equals(padre.getDirectory())){
				return documentiDao.load(Restrictions.eq("idPadre", padre.getId()));
			}
		}
		return Collections.emptyList();
	}

	public static void removeDocumento(String id) {
		if(StringUtils.isBlank(id)){
			return;
		}
		File file = new File(_repositoryRoot+File.separator+id+".docData");
		if(file.exists()){
			try {
				FileUtils.deleteQuietly(file);
			} catch (Exception e) {
				logger.error("errore in lettura id: "+id+" "+ExceptionUtils.getRootCauseMessage(e),e);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public static List<Tag> getTags(String idDocumenti) {
		if(StringUtils.isNotBlank(idDocumenti)){
			TagDao tagDao = getTagDao();
			DetachedCriteria criteria2 = DetachedCriteria.forClass(TagDocumenti.class);
			criteria2.add(Restrictions.eq("idDocumenti", idDocumenti));
			criteria2.setProjection(Projections.property("idTag"));
			DetachedCriteria criteria = DetachedCriteria.forClass(Tag.class);
			criteria.add(Subqueries.propertiesIn(new String[]{"id"}, criteria2));
			return (List<Tag>) tagDao.load(criteria);
		}
		return Collections.emptyList();
	}

	public static TagDao getTagDao() {
		return getBean("tagDao");
	}

	public static TagDocumentiDao getTagDocumentiDao() {
		return getBean("tagDocumentiDao");
	}

}
