package me.kimchidev.jwtapilogin;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebSecurity
@RequiredArgsConstructor
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http    .cors().and()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/user/login").permitAll()
                .antMatchers(HttpMethod.GET,"/user").hasAuthority("USER") //user api 유저권한
                .antMatchers("/admin/*").hasAuthority("ADMIN") //admin api 관리자 권한
                .anyRequest().authenticated()
                .and()
                .logout();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        System.out.println("WebSecurityConfig.configure Build Manager");
        auth.userDetailsService(userService)
                .passwordEncoder(passwordEncoder());
        /*auth.inMemoryAuthentication()
                .withUser("tgjeon").password(passwordEncoder().encode("password486"))
                .authorities("USER","ADMIN");*/
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }



    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
