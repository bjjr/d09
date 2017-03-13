

package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import security.Authority;

@Component
@Transactional
public class StringToAuthorityConverter implements Converter<String, Authority> {

	@Override
	public Authority convert(String text) {
		Authority result;
		try {
			if (!text.equals("LESSOR") && !text.equals("TENANT") && !text.equals("AUDITOR")) {
				result = null;
			} else {
				result = new Authority();
				result.setAuthority(text);
			}
		} catch (Throwable oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}
}
