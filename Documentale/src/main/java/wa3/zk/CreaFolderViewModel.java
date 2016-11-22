package wa3.zk;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Executions;

import wa3.dao.DocumentiDao;
import wa3.model.Documenti;
import wa3.web.WebFactory;

public class CreaFolderViewModel {

	private String nomeFolder;
	private Documenti currentDir;

	@Init
	public void init() {
		HttpSession session = (HttpSession) Executions.getCurrent().getSession().getNativeSession();
		currentDir = (Documenti) session.getAttribute("currentDir");

	}

	@Command
	public void creaFolder(@ContextParam(ContextType.COMPONENT)org.zkoss.zk.ui.Component comp) {
		if (currentDir!=null && StringUtils.isNotBlank(nomeFolder)){
			DocumentiDao documentiDao = WebFactory.getDocumentiDao();
			Documenti folder = new Documenti();
			folder.setIdPadre(currentDir.getId());
			folder.setNome(nomeFolder);
			folder.setDirectory(true);
			folder.setMime("");
			documentiDao.save(folder);
			Map<String, Object> args = new HashMap<>();
			args.put("folder", folder);
			BindUtils.postGlobalCommand(null, null, "creazioneFolder", args);
			comp.getRoot().detach();
		}
	}

	@Command
	public void annulla(@ContextParam(ContextType.COMPONENT)org.zkoss.zk.ui.Component comp)  {
		comp.getRoot().detach();
	}

	public String getNomeFolder() {
		return nomeFolder;
	}

	public void setNomeFolder(String nomeFolder) {
		this.nomeFolder = nomeFolder;
	}

	public Documenti getCurrentDir() {
		return currentDir;
	}

	public void setCurrentDir(Documenti currentDir) {
		this.currentDir = currentDir;
	}



}
