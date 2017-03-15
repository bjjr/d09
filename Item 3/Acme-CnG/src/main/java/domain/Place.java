
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;

@Embeddable
@Access(AccessType.PROPERTY)
public class Place {

	private String	address;
	private String	coordinates;


	@NotNull
	@NotBlank
	public String getAddress() {
		return this.address;
	}

	public void setAddress(final String address) {
		this.address = address;
	}

	@Pattern(regexp = "^[-+]?([1-8]?\\d(\\.\\d{1,6})?|90(\\.0+)?),\\s*[-+]?(180(\\.0+)?|((1[0-7]\\d)|([1-9]?\\d))(\\.\\d{1,6})?)$")
	public String getCoordinates() {
		return this.coordinates;
	}

	public void setCoordinates(final String coordinates) {
		this.coordinates = coordinates;
	}

}
