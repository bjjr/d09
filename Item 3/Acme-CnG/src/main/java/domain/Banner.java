
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

@Entity
@Access(AccessType.PROPERTY)
public class Banner extends DomainEntity {

	// Attributes ------------------------------

	private String	path;


	// Constructors ----------------------------

	public Banner() {
		super();
	}

	// Getters and setters ---------------------

	@NotNull
	@NotBlank
	@URL
	public String getPath() {
		return this.path;
	}

	public void setPath(final String path) {
		this.path = path;
	}

}
