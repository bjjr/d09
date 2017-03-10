
package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Comment;

@Component
@Transactional
public class CommentToStringConverter implements Converter<Comment, String> {

	@Override
	public String convert(Comment comment) {
		String res;

		if (comment == null) {
			res = null;
		} else {
			res = String.valueOf(comment.getId());
		}
		return res;
	}
}
