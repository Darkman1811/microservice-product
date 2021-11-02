package com.guestu.ecom.microserviceproduct.config;

import org.keycloak.adapters.springsecurity.KeycloakConfiguration;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

@KeycloakConfiguration
public class KeycloakSpringSecurityConfig extends KeycloakWebSecurityConfigurerAdapter {
    @Override
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
    }



    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
      auth.authenticationProvider(keycloakAuthenticationProvider());
      //super.configure(auth);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        http.csrf().disable()
        .cors().disable()
       // .authorizeRequests().anyRequest().authenticated()
         //.authorizeRequests().antMatchers("/test/**").permitAll()
        //.and()
         .authorizeRequests().anyRequest().permitAll()
        ;
    }
}
