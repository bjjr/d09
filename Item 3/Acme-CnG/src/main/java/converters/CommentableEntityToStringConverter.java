
package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.CommentableEntity;

@Component
@Transactional
public class CommentableEntityToStringConverter implements Converter<CommentableEntity, String> {

	@Override
	public String convert(CommentableEntity commentableEntity) {
		String res;

		if (commentableEntity == null) {
			res = null;
		} else {
			res = String.valueOf(commentableEntity.getId());
		}
		return res;
	}
}
