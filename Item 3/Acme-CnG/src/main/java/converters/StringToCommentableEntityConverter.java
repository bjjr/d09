
package converters;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import repositories.CommentableEntityRepository;
import domain.CommentableEntity;

@Component
@Transactional
public class StringToCommentableEntityConverter implements Converter<String, CommentableEntity> {

	@Autowired
	CommentableEntityRepository	commentableEntityRepository;


	@Override
	public CommentableEntity convert(String text) {
		CommentableEntity res;
		int id;

		try {
			if (StringUtils.isEmpty(text))
				res = null;
			else {
				id = Integer.valueOf(text);
				res = commentableEntityRepository.findOne(id);
			}
		} catch (Throwable th) {
			throw new IllegalArgumentException(th);
		}

		return res;
	}
}
