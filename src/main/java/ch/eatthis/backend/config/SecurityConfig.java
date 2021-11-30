package ch.eatthis.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .cors().and()
                .authorizeRequests(authorizedRequest -> authorizedRequest.mvcMatchers("/", "/csrf", "/v2/api-docs", "/swagger-resources/configuration/ui",
                        "/configuration/ui", "/swagger-resources", "/swagger-resources/configuration/security", "/configuration/security", "/swagger-ui.html",
                        "/webjars/**", "/favicon.ico", "/**/favicon.ico", "/**/favicon.*")
                        .permitAll()
                        .mvcMatchers("/api/v1/**")
                        .permitAll()
                        .anyRequest().denyAll());
    }

}
