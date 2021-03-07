package io.app.blog.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;

/**
 * 
 * @author Arm Fahim
 *
 */
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	final DataSource dataSource = null;

	@Value("${spring.admin.username}")
	private String adminUsername;

	@Value("${spring.admin.username}")
	private String adminPassword;

	@Value("${spring.queries.users-query}")
	private String usersQuery;

	@Value("${spring.queries.roles-query}")
	private String rolesQuery;

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.csrf().disable().authorizeRequests()
				.antMatchers("/home", "/registration", "/error", "/blog/**", "/post/**", "/h2-console/**").permitAll()
				.antMatchers("/newPost/**", "/commentPost/**", "/createComment/**").hasAnyRole("BLOGGER").anyRequest()
				.authenticated().and().formLogin().loginPage("/login").defaultSuccessUrl("/home").permitAll().and()
				.logout().permitAll()
//				.and().exceptionHandling().accessDeniedHandler(accessDeniedHandler)
				.and().headers().frameOptions().disable();
	}

	/**
	 * Authentication details
	 */
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

		// Database authentication 
		auth.jdbcAuthentication().usersByUsernameQuery(usersQuery).authoritiesByUsernameQuery(rolesQuery)
				.dataSource(dataSource).passwordEncoder(passwordEncoder());

		// In memory authentication
		auth.inMemoryAuthentication().withUser(adminUsername).password(adminPassword).roles("ADMIN");
	}

	/**
	 * Configure and retuBCrypt password encoder
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

//	@Override
//	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//		// setting configurations on the auth object
//		auth.inMemoryAuthentication().withUser("fahim").password("{noop}fahim").roles("USER").and().withUser("square")
//				.password("{noop}square").roles("ADMIN");
//	}
//
//	@Override
//	protected void configure(HttpSecurity http) throws Exception {
//		http.authorizeRequests().antMatchers("/admin").hasRole("ADMIN").antMatchers("/user").hasRole("USER")
//				.antMatchers("/").permitAll().and()
//				.formLogin();
//	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/h2-console/**");
	}

}
