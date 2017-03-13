
package converters;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import repositories.OfferRepository;
import domain.Offer;

@Component
@Transactional
public class StringToOfferConverter implements Converter<String, Offer> {

	@Autowired
	OfferRepository	offerRepository;


	@Override
	public Offer convert(final String text) {
		Offer res;
		int id;

		try {
			if (StringUtils.isEmpty(text))
				res = null;
			else {
				id = Integer.valueOf(text);
				res = this.offerRepository.findOne(id);
			}
		} catch (final Throwable th) {
			throw new IllegalArgumentException(th);
		}

		return res;
	}

}
