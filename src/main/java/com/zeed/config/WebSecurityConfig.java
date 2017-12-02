package com.zeed.config;

import com.zeed.security.JwtAuthenticationEntryPoint;
import com.zeed.security.JwtAuthenticationTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@SuppressWarnings("SpringJavaAutowiringInspection")
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtAuthenticationEntryPoint unauthorizedHandler;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    public void configureAuthentication(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .userDetailsService(this.userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationTokenFilter authenticationTokenFilterBean() throws Exception {
        return new JwtAuthenticationTokenFilter();
    }
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // we don't need CSRF because our token is invulnerable
                .csrf().disable()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()

                // don't create session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()

                .authorizeRequests()
                //.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                // allow anonymous resource requests
                .antMatchers(
                        HttpMethod.GET,
                        "/",
                        "/*.html",
                        "/favicon.ico",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js",
                        "/font-awesome-4.7.0 2/**",
                        "https://132.10.200.52:7200/fiwebservice/FIWebService",
                        "/images/**",
                        "/swagger-ui.html"
                ).permitAll()
                .antMatchers("/auth/**").permitAll()
                .antMatchers("/api/process").permitAll()
                .antMatchers("/fashion/**").permitAll()
                .antMatchers("/api/getPostResource").permitAll()
                .antMatchers("/user").permitAll()
                .antMatchers("/dashboard").permitAll()
                .antMatchers("/admindashboard").permitAll()
                .antMatchers("/getuserEnquiry").permitAll()
                .antMatchers("/adminlogout").permitAll()
                .antMatchers("/editProfile").permitAll()
                .antMatchers("/declined").permitAll()
                .antMatchers("/verified").permitAll()
                .antMatchers("/declineCard").permitAll()
                .antMatchers("/modalVerDecDet").permitAll()
                .antMatchers("/verifyCard").permitAll()
                .antMatchers("/profileModal").permitAll()
                .antMatchers("/userEnquiry").permitAll()
                .antMatchers("/adminHome").permitAll()
//                .antMatchers("/adminsignup").permitAll()
                .antMatchers("/userlogout").permitAll()
                .antMatchers("/uploadCard").permitAll()
                .antMatchers("/usersignup").permitAll()
                .antMatchers("/user/**").permitAll()
                .antMatchers("/swagger-ui.html").permitAll()
//                .and().authorizeRequests().antMatchers("/fashion/Register").permitAll();
                .anyRequest().authenticated();

        // Custom JWT based security filter
        httpSecurity
                .addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);

        // disable page caching
//        httpSecurity.headers().cacheControl();
    }
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/v2/api-docs", "/configuration/ui", "/swagger-resources", "/configuration/security", "/swagger-ui.html", "/webjars/**");
    }
}