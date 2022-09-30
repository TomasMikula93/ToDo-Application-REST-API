package com.example.todoapp.Configurations;

import com.example.todoapp.Filters.CustomAuthenticationFilter;
import com.example.todoapp.Filters.JwtRequestFilter;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService; // interface is used to retrieve user-related data. It has one method named loadUserByUsername()
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception { // allows configuring web based security for specific http requests
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManagerBean());
        customAuthenticationFilter.setFilterProcessesUrl("/api/login");

        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); // we don't store any information about the logged-in user in memory, (only DB users)
        http.authorizeRequests().antMatchers("/api/registration").permitAll();
        http.authorizeRequests().antMatchers("/api/registration/confirm").permitAll();
        http.authorizeRequests().antMatchers("/api/registration/newToken").permitAll();
        http.authorizeRequests().antMatchers("/api/login").permitAll();
        http.authorizeRequests().antMatchers("/api/user").permitAll();
        http.authorizeRequests().anyRequest().authenticated(); // any other request, must be authenticated
        http.addFilter(customAuthenticationFilter);
        http.addFilterBefore(new JwtRequestFilter(), UsernamePasswordAuthenticationFilter.class);

    }


    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}

