
package domain;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
public class Trip extends CommentableEntity {

	private String	title;
	private String	description;
	private Date	moment;
	private Place	origin;
	private Place	destination;
	private boolean	banned;


	@NotNull
	@NotBlank
	public String getTitle() {
		return this.title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	@NotNull
	@NotBlank
	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	@NotNull
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getMoment() {
		return this.moment;
	}

	public void setMoment(final Date moment) {
		this.moment = moment;
	}

	@NotNull
	@Valid
	@AttributeOverrides({
		@AttributeOverride(name = "address", column = @Column(name = "origin_address")), @AttributeOverride(name = "coordinates", column = @Column(name = "origin_coordinates"))
	})
	public Place getOrigin() {
		return this.origin;
	}

	public void setOrigin(final Place origin) {
		this.origin = origin;
	}

	@NotNull
	@Valid
	@AttributeOverrides({
		@AttributeOverride(name = "address", column = @Column(name = "destination_address")), @AttributeOverride(name = "coordinates", column = @Column(name = "destination_coordinates"))
	})
	public Place getDestination() {
		return this.destination;
	}

	public void setDestination(final Place destination) {
		this.destination = destination;
	}

	public boolean isBanned() {
		return this.banned;
	}

	public void setBanned(final Boolean banned) {
		this.banned = banned;
	}


	// Relationships -----------------------------------

	private Customer	customer;


	@NotNull
	@Valid
	@ManyToOne(optional = false)
	public Customer getCustomer() {
		return this.customer;
	}

	public void setCustomer(final Customer customer) {
		this.customer = customer;
	}

}
