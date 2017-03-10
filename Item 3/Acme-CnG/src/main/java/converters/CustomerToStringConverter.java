
package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Customer;

@Component
@Transactional
public class CustomerToStringConverter implements Converter<Customer, String> {

	@Override
	public String convert(final Customer customer) {
		String res;

		if (customer == null)
			res = null;
		else
			res = String.valueOf(customer.getId());

		return res;
	}

}
