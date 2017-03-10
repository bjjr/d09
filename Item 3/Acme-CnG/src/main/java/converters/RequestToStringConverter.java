
package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Request;

@Component
@Transactional
public class RequestToStringConverter implements Converter<Request, String> {

	@Override
	public String convert(final Request request) {
		String res;

		if (request == null)
			res = null;
		else
			res = String.valueOf(request.getId());

		return res;
	}

}
