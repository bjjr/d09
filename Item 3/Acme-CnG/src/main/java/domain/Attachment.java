
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Access(AccessType.PROPERTY)
public class Attachment extends DomainEntity {

	private String	path;


	@NotNull
	@NotBlank
	public String getPath() {
		return this.path;
	}

	public void setPath(final String path) {
		this.path = path;
	}


	// Relationships -------------------------------

	private Message	message;


	@NotNull
	@Valid
	@ManyToOne(optional = false)
	public Message getMessage() {
		return this.message;
	}

	public void setMessage(final Message message) {
		this.message = message;
	}

}
