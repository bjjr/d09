
package converters;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import repositories.ApplicationRepository;
import domain.Application;

@Component
@Transactional
public class StringToApplicationConverter implements Converter<String, Application> {

	@Autowired
	ApplicationRepository	applicationRepository;


	@Override
	public Application convert(final String text) {
		Application res;
		int id;

		try {
			if (StringUtils.isEmpty(text))
				res = null;
			else {
				id = Integer.valueOf(text);
				res = this.applicationRepository.findOne(id);
			}
		} catch (final Throwable th) {
			throw new IllegalArgumentException(th);
		}

		return res;
	}

}
