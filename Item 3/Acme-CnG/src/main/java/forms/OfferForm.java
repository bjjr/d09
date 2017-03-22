
package forms;

import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import domain.Offer;
import domain.Place;

public class OfferForm {

	private Integer	id;
	private Integer	version;
	private String	title;
	private String	description;
	private Date	moment;
	private Place	origin;
	private Place	destination;
	private Boolean	banned;


	public OfferForm() {
		super();
	}

	public OfferForm(final Offer offer) {
		this();
		this.id = offer.getId();
		this.version = offer.getVersion();
		this.title = offer.getTitle();
		this.description = offer.getDescription();
		this.moment = offer.getMoment();
		this.origin = offer.getOrigin();
		this.destination = offer.getDestination();
		this.banned = offer.isBanned();
	}

	public Offer getOffer() {
		Offer result;

		result = new Offer();
		result.setId(this.id);
		result.setVersion(this.version);
		result.setTitle(this.title);
		result.setDescription(this.description);
		result.setMoment(this.moment);
		result.setOrigin(this.origin);
		result.setDestination(this.destination);
		result.setBanned(this.banned);

		return result;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(final Integer id) {
		this.id = id;
	}

	public Integer getVersion() {
		return this.version;
	}

	public void setVersion(final Integer version) {
		this.version = version;
	}

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

	public Boolean getBanned() {
		return this.banned;
	}

	public void setBanned(final Boolean banned) {
		this.banned = banned;
	}

}
