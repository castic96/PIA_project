package cz.zcu.fav.pia.tictactoe.configuration;

import cz.zcu.fav.pia.tictactoe.domain.RoleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) // enables @PreAuthorize annotations for endpoint restrictions
@RequiredArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests(auth -> auth
					.antMatchers("/webjars/**").permitAll()
					.antMatchers("/admin**").hasRole(RoleEnum.ADMIN.getCode())
					.antMatchers("/authenticated/**").authenticated()
					.anyRequest().permitAll())
			.formLogin()
			.loginPage("/login")
			.defaultSuccessUrl("/")
			.failureUrl("/login?error=true")
			.and()
			.logout()
			.logoutUrl("/logout")
			.logoutSuccessUrl("/")
			.and().exceptionHandling().accessDeniedPage("/");
	}

}
