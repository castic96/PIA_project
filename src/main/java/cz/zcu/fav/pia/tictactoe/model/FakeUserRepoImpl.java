package cz.zcu.fav.pia.tictactoe.model;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class FakeUserRepoImpl implements UserRepository, UserDetailsService {

	private static final String ADMIN = "ADMIN";
	private static final String USER = "USER";
	private static final String PASSWORD = "heslo";

	private final Map<String, User> users = new HashMap<>();

	private final PasswordEncoder encoder;

	private void addUser(String username, String password, String... roles) {
		Set<GrantedAuthority> authorities = new HashSet<>();

		for (String role : roles) {
			authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
		}

		final var user = User.builder()
				.username(username)
				.password(encoder.encode(password))
				.authorities(authorities)
				.build();
		this.users.put(username, user);
	}

	@PostConstruct
	private void setup() {
		addUser("user", PASSWORD, USER);
		addUser("admin", PASSWORD, ADMIN);
		addUser("god", PASSWORD, ADMIN, USER);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return this.users.computeIfAbsent(username, k -> { throw new UsernameNotFoundException(username); });
	}

}
