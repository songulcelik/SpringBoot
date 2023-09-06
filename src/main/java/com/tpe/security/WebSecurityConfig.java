package com.tpe.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

//---10 security icin configurasyon classi
@Configuration//configurasyon class old belirtmek icin
@EnableWebSecurity//bu classla web securityi enable hale getiriyorum
@EnableGlobalMethodSecurity(prePostEnabled = true)//security katmani methodlar seviyesinde olcak buna olanak icin. method seviyede security
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {//basic authotentionu yaparken configurasyon ayarlari icin
//11 extends WebSecurityConfigurerAdapter
    //12
    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable().authorizeHttpRequests()//authorizeHttpRequests() gelen http requestleri yerkili mi kontrol et
                .antMatchers("/","index.html","/css/*","/js/*","/register")//bu endpointlerle istek gelirse securityden muaf tut
                .permitAll().
                anyRequest().
                authenticated().
                and().
                httpBasic();
    }

    //13 PasswordEncoder securitnin kensi yazdigi class o nedenle bean ile anote ettik
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(10);//passwordu encode ediyor. sifre ne kkadar uzunsa sure o kadar uzuyor4-31 arasinda
    }

    //14  DaoAuthenticationProvider authendicationProvider classini extend eden concrite class
    @Bean
    public DaoAuthenticationProvider authProvider(){
        DaoAuthenticationProvider authProvider =new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    //15

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
      auth.authenticationProvider(authProvider());
    }
}
