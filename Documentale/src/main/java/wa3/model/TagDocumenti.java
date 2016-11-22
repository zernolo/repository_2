package wa3.model;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "tag_documenti")
public class TagDocumenti {
	@Override
	public String toString() {
		return "TagDocumenti [id=" + id + ", idTag=" + idTag + ", idDocumenti=" + idDocumenti + "]";
	}

	private String id;

	private String idTag;
	private String idDocumenti;

	public TagDocumenti() {

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

	@Column(name = "id_tag", length = 36)
	public String getIdTag() {
		return idTag;
	}

	public void setIdTag(String idTag) {
		this.idTag = idTag;
	}

	@Column(name = "id_documenti", length = 36)
	public String getIdDocumenti() {
		return idDocumenti;
	}

	public void setIdDocumenti(String idDocumenti) {
		this.idDocumenti = idDocumenti;
	}

	//



}