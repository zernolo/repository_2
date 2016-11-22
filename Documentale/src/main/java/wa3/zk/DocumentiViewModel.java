package wa3.zk;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.media.Media;
import org.zkoss.zhtml.Filedownload;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Messagebox.ClickEvent;

import wa3.MimeUtils;
import wa3.dao.DocumentiDao;
import wa3.dao.TagDocumentiDao;
import wa3.model.Documenti;
import wa3.model.Tag;
import wa3.web.WebFactory;

public class DocumentiViewModel {

	private Documenti currentDir;
	private List<Documenti> files;
	private List<Tag> tags;

	@Init
	@NotifyChange({ "currentDir", "files" })
	public void init() {
		tags = WebFactory.getTagDao().load();//(Order.asc("tag"));
		HttpSession session = (HttpSession) Executions.getCurrent().getSession().getNativeSession();
		DocumentiDao documentiDao = WebFactory.getDocumentiDao();
		currentDir = (Documenti) session.getAttribute("currentDir");
		if (currentDir == null) {
			currentDir = documentiDao.getById("root");
			session.setAttribute("currentDir", currentDir);
		}
		if (currentDir != null) {
			files = WebFactory.getFigli(currentDir.getId());
		}
		BindUtils.postNotifyChange(null, null, this, "files");
		BindUtils.postNotifyChange(null, null, this, "currentDir");
	}

	public List<Documenti> getFiles() {
		return files;
	}

	public void setFiles(List<Documenti> files) {
		this.files = files;
	}

	public Documenti getCurrentDir() {
		return currentDir;
	}

	public void setCurrentDir(Documenti currentDir) {
		this.currentDir = currentDir;
	}

	@Command
	@NotifyChange({ "currentDir", "files" })
	public void deleteFile(@BindingParam("file") Documenti file) {
		if (file != null) {
			if (!Boolean.TRUE.equals(file.getDirectory())) {
				Messagebox.show("Eliminare il file:" + file.getNome() + " ?",
						new Messagebox.Button[] { Messagebox.Button.YES, Messagebox.Button.NO },
						new EventListener<Messagebox.ClickEvent>() {
					@Override
					public void onEvent(ClickEvent arg0) throws Exception {
						doDeleteFile(file);
					}
				});
			} else {
				List<Documenti> figli = WebFactory.getFigli(file.getId());
				if (figli.isEmpty()) {
					Messagebox.show("Eliminare il file:" + file.getNome() + " ?",
							new Messagebox.Button[] { Messagebox.Button.YES, Messagebox.Button.NO },
							new EventListener<Messagebox.ClickEvent>() {
						@Override
						public void onEvent(ClickEvent arg0) throws Exception {
							doDeleteFile(file);
						}
					});
				} else {
					cancellazioneRicorsiva(file);
//					Messagebox.show("Impossibile eliminare cartella:" + file.getNome() + " perchè non è vuota");
				}
			}
		}
	}

	private void cancellazioneRicorsiva(Documenti file) {
		Messagebox.show("Eliminare la cartella:" + file.getNome() + " e tutti i file contenuti ?",
				new Messagebox.Button[] { Messagebox.Button.YES, Messagebox.Button.NO },
				new EventListener<Messagebox.ClickEvent>() {
			@Override
			public void onEvent(ClickEvent arg0) throws Exception {
				Nodo root = leggiAlbero(file);
				if (root!=null){
					doDeleteFolder(root);
				}
			}

		});

	}

	private void doDeleteFolder(Nodo root) {
		if (root==null)
			return;
		for (Documenti documenti : root.getFigli()) {
			if (!Boolean.TRUE.equals(documenti.getDirectory()))
				doDeleteFile(documenti);
		}
		for (Nodo nodo : root.nodi) {
			doDeleteFolder(nodo);
		}
		doDeleteFile(root.folder);
	}

	// cancellazione cartella o file
	private void doDeleteFile(Documenti file) {
		DocumentiDao documentiDao = WebFactory.getDocumentiDao();
		documentiDao.remove(file);
		if (!Boolean.TRUE.equals(file.getDirectory()))
			WebFactory.removeDocumento(file.getId());
		BindUtils.postNotifyChange(null, null, this, "currentDir");
		BindUtils.postNotifyChange(null, null, this, "files");
		Executions.sendRedirect("/");
	}

	// DOWNLOAD FILE
	@Command
	@NotifyChange({ "currentDir", "files" })
	public void selectDir(@BindingParam("file") Documenti file) {
		if (file != null) {
			if (Boolean.TRUE.equals(file.getDirectory())) {
				HttpSession session = (HttpSession) Executions.getCurrent().getSession().getNativeSession();
				session.setAttribute("currentDir", file);
				BindUtils.postNotifyChange(null, null, this, "currentDir");
				BindUtils.postNotifyChange(null, null, this, "files");
				Executions.sendRedirect("/");
			} else {
				byte[] dati = file.getDati();
				if (dati != null)
					Filedownload.save(dati, file.getMime(), file.getNome());
				else
					Messagebox.show("file " + file.getNome() + " non trovato");
			}
		}
	}

	@Command
	public void goToParent() {

		if (currentDir != null && currentDir.getIdPadre() != null) {
			DocumentiDao documentiDao = WebFactory.getDocumentiDao();
			currentDir = documentiDao.getById(currentDir.getIdPadre());

			HttpSession session = (HttpSession) Executions.getCurrent().getSession().getNativeSession();
			session.setAttribute("currentDir", currentDir);
			BindUtils.postNotifyChange(null, null, this, "currentDir");
			BindUtils.postNotifyChange(null, null, this, "files");
			Executions.sendRedirect("/");
		}

	}

	// CARICA FILE
	@Command
	public void uploadfile(@BindingParam("media") Media media) {

		if (media != null) {

			Documenti documenti = new Documenti();
			documenti.setNome(media.getName());
			documenti.setMime(media.getContentType());
			documenti.setIdPadre(currentDir.getId());

			DocumentiDao documentiDao = WebFactory.getDocumentiDao();

			documentiDao.save(documenti);
			documenti.setDati(media.getByteData());
		}

	}

	// AGGIUNGI CARTELLA
	@Command
	@NotifyChange({ "currentDir", "files" })
	public void addFolder() {
		if (currentDir != null) {
			Executions.createComponents("/creaFolder.zul", null, null);
		}
	}

	@GlobalCommand
	public void creazioneFolder() {
		Executions.sendRedirect("/");
	}

	public String getImageExt(String mime) {
		return MimeUtils.guessExtensionFromMimeType(mime);
	}

	public class Nodo {
		private Documenti folder;
		private List<Documenti> figli;
		private List<Nodo> nodi;

		public Nodo() {

			figli = new ArrayList<>();
			nodi = new ArrayList<>();
		}

		public Documenti getFolder() {
			return folder;
		}

		public void setFolder(Documenti folder) {
			this.folder = folder;
		}

		public List<Documenti> getFigli() {
			return figli;
		}

		public void setFigli(List<Documenti> figli) {
			this.figli = figli;
		}

		public List<Nodo> getNodi() {
			return nodi;
		}

		public void setNodi(List<Nodo> nodi) {
			this.nodi = nodi;
		}

	}

	private Nodo leggiAlbero(Documenti folder){
		if (folder==null || !Boolean.TRUE.equals(folder.getDirectory()))
			return null;
		Nodo nodo = new Nodo();
		nodo.setFolder(folder);
		List<Documenti> r = WebFactory.getFigli(folder.getId());
		for (Documenti doc : r) {
			if (Boolean.TRUE.equals(doc.getDirectory())){
				Nodo y = leggiAlbero(doc);
				if (y!=null)
					nodo.getNodi().add(y);
			} else {
				nodo.figli.add(doc);
			}
		}
		return nodo;
	}

	public List<Tag> getTags() {
		return tags;
	}


	@Command
	public void saveTags(@BindingParam("documenti") Documenti documenti, @BindingParam("tags") Set<Tag> tags) {
		DocumentiDao dao = WebFactory.getDocumentiDao();
//		System.out.println(tags);
		documenti.setTags(new ArrayList<>(tags));
		dao.update(documenti);

	}


}
