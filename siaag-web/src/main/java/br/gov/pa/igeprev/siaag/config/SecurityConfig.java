package br.gov.pa.igeprev.siaag.config;

import br.gov.pa.igeprev.siaag.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // require all requests to be authenticated except for the resources
        http.authorizeRequests().antMatchers("/javax.faces.resource/pages/**").permitAll()
                .antMatchers("/javax.faces.resource/**").permitAll()
                .antMatchers("/javax.faces.resource/pages/public/recuperar-senha.xhtml").permitAll()
                .antMatchers("/siaag/recuperar-senha").permitAll()
                .antMatchers("/recuperar-senha").permitAll()
                .antMatchers("/pages/public/**").permitAll()
                .antMatchers("/resources/**").permitAll()
                .antMatchers("/logout").permitAll()
                .antMatchers("/**").authenticated()
                /*.antMatchers("../").authenticated()*/
                .antMatchers("/").authenticated()
                .antMatchers("/home").authenticated()
                .antMatchers("/pages/protected/**").authenticated()
                .antMatchers("/pages/protected/admin/**").hasRole("Administrador");

        http.exceptionHandling().accessDeniedPage("/pages/acessonegado.xhtml");
        // login
        http.formLogin().loginPage("/pages/login.xhtml").permitAll().failureUrl("/pages/login.xhtml?error=true")
                .successHandler(successHandler());

        // logout
        http.logout().logoutSuccessUrl("/pages/login.xhtml");

        // not needed as JSF 2.2 is implicitly protected against CSRF
        http.csrf().disable();

    }
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationSuccessHandler successHandler() {
        return new CustomAuthenticationSuccessHandler();
    }
}