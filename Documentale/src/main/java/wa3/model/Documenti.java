package wa3.model;

import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;

import wa3.web.WebFactory;

@Entity(name = "documenti")
public class Documenti {
	private String id;
	private String idPadre = "root";
	private String nome;
	private String mime;
	private Boolean directory = false;
	private List<Tag> tags;

	public Documenti() {
		id = UUID.randomUUID().toString();
	}

	@Id
	@Column(name = "id", length = 36)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "id_padre", length = 36)
	public String getIdPadre() {
		return idPadre;
	}

	public void setIdPadre(String idPadre) {
		this.idPadre = idPadre;
	}

	@Column(name = "nome", length = 255)
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Column(name = "mime")
	public String getMime() {
		return mime;
	}

	public void setMime(String mime) {
		this.mime = mime;
	}

	//conversione boolean in Y_N
	@org.hibernate.annotations.Type(type="yes_no")
	@Column(name = "directory", length = 1, columnDefinition="ENUM")
	public Boolean getDirectory() {

		return directory;
	}

	public void setDirectory(Boolean directory) {
		this.directory = directory;
	}

	@Override
	public String toString() {
		return "Documenti [id=" + id + ", idPadre=" + idPadre + ", nome=" + nome + ", mime=" + mime + ", directory="
				+ directory + "]";
	}

	@Transient
	public byte[] getDati(){
		if(Boolean.TRUE.equals(directory)){
			throw new IllegalArgumentException("Il documento è una directory");
		}
		if(id != null){
			return WebFactory.getDatiDocumento(id);
		}
		return null;
	}

	public void setDati(byte[] dati){
		if(Boolean.TRUE.equals(directory)){
			throw new IllegalArgumentException("Il documento è una directory");
		}
		if(id != null){
			WebFactory.setDatiDocumento(id,dati);
		}
	}

	@Transient
	public List<Tag> getTags(){
		if(tags == null && StringUtils.isNotBlank(id)){
			tags = WebFactory.getTags(id);
		}
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}



}