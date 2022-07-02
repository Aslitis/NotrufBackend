package htw.projektarbeit.webApplication;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

//Fuer Nutzeranmeldung
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                //nutzen der Custom Login-Seite statt der von Spring
                //Nutzer zugreifen lassen ohne bereits Authentication zu brauchen
                .loginPage("/login.html")
                .defaultSuccessUrl("/website.html")
                .permitAll();
    }

    //Nutzer hardcoden
    //Rolle aktuell irrelevant
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        //Konfiguration auf auth Objekt setzen
        auth.inMemoryAuthentication()
                .withUser("test")
                .password("test")
                .roles("USER");

    }

    //Verschluesselung des Passworts
    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    
    
}
