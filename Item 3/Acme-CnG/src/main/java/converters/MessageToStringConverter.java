package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.MessageEntity;

@Component
@Transactional
public class MessageToStringConverter implements Converter<MessageEntity, String>{

	@Override
	public String convert(MessageEntity messageEntity) {
		String result;
		
		if(messageEntity == null){
			result = null;
		}
		else{
			result = String.valueOf(messageEntity.getId());
		}
		
		return result;
	}

}
