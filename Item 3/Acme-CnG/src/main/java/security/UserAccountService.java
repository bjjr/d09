
package security;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Service
@Transactional
public class UserAccountService {

	// Managed repository --------------------
	@Autowired
	private UserAccountRepository	userAccountRepository;


	// Constructors --------------------------

	public UserAccountService() {
		super();
	}

	// Simple CRUD methods -------------------

	public UserAccount create(String authority) {
		Assert.notNull(authority);
		UserAccount res;
		Authority a;
		Collection<Authority> authorities = new ArrayList<Authority>();

		a = new Authority();
		a.setAuthority(authority);
		authorities.add(a);

		res = new UserAccount();
		res.setAuthorities(authorities);

		return res;
	}

	public UserAccount save(UserAccount ua) {
		Assert.notNull(ua);

		UserAccount res;
		res = userAccountRepository.save(ua);

		return res;
	}

	public UserAccount findByUsername(String username) {
		Assert.notNull(username);
		UserAccount res;

		res = userAccountRepository.findByUsername(username);
		Assert.notNull(res);

		return res;
	}
}
