package com.ufab.biblioteca_ufab.configuracoes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.ufab.biblioteca_ufab.models.servicos.ServicoAutenticacao;

@Configuration
@EnableWebSecurity
public class ConfiguracaoAutenticacao extends WebSecurityConfigurerAdapter {

	@Autowired private ServicoAutenticacao servicoAutenticacao;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		
		auth
			.userDetailsService(servicoAutenticacao)
			.passwordEncoder(encoder());
			
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
				.antMatchers("/alunos/**").hasRole("ADMIN")
				.anyRequest().permitAll()
		.and()
			.formLogin()
				.loginPage("/login")
				.loginProcessingUrl("/autenticar")
				.defaultSuccessUrl("/alunos")
				.failureUrl("/login?error=true")
				.usernameParameter("email")
				.passwordParameter("password")
				
			.and()
				.logout()
					.logoutUrl("/logout")
					.logoutSuccessUrl("/login?logout=true");
	}
	
	@Bean
	public BCryptPasswordEncoder encoder(){
		return new BCryptPasswordEncoder();
	}
	
	public static void main(String[] args) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		System.out.println(encoder.encode("admin"));
	}

}