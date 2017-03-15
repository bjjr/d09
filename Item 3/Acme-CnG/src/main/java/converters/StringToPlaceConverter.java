
package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Place;

@Component
@Transactional
public class StringToPlaceConverter implements Converter<String, Place> {

	@Override
	public Place convert(final String text) {
		Place res;

		try {
			res = new Place();
			res.setAddress(text);
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}

		return res;
	}

}
