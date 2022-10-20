package com.armtimes.armtimes.config;


import com.armtimes.armtimes.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;


@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .formLogin()
                .loginPage("/loginPage")
                .usernameParameter("username") //optional
                .passwordParameter("password") //optional
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/loginSuccess")
                .failureUrl("/loginPage?error=true")
                .and()
                .logout()
                .logoutSuccessUrl("/")
                .and()
                .authorizeRequests()
                .antMatchers("/articles/add").hasAuthority(Role.ADMIN.name())
                .antMatchers("/articles").authenticated()
                .antMatchers("/users").hasAuthority(Role.ADMIN.name())
                .antMatchers("/users/delete").hasAuthority(Role.ADMIN.name())
                .antMatchers("/editors").hasAuthority(Role.EDITOR.name())
                .antMatchers("/user").hasAuthority(Role.USER.name())
                .anyRequest()
                .permitAll()
                .and()
                .exceptionHandling().accessDeniedPage("/accessDenied");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
    }
}
