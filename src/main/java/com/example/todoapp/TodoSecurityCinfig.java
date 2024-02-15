package com.example.todoapp;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class TodoSecurityCinfig {
	
	@Autowired
	private DataSource dataSource;
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(AbstractHttpConfigurer::disable);
		http.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable));
		http.authorizeHttpRequests(requests -> requests.anyRequest().permitAll());
		http.formLogin(form -> form.defaultSuccessUrl("/secret").loginPage("/login"));
		http.logout(LogoutConfigurer::permitAll);
		return http.build();
	}
	
	@Bean
	public UserDetailsManager userDetailsManager() {
		return new JdbcUserDetailsManager(this.dataSource);
		
		// 初回起動時[user]というアカウントを作成、
		// またはアカウントを追加したい場合下記コードを実行
//		JdbcUserDetailsManager users = new JdbcUserDetailsManager(this.dataSource);
//		users.createUser(makeUser("user", "pass", "USER"));
//		return users;
	}
	
	// アカウント作成用メソッド
//	private UserDetails makeUser(String name, String pass, String role) {
//		return User.withUsername(name)
//				.password(PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(pass))
//				.roles(role)
//				.build();
//	}

}
