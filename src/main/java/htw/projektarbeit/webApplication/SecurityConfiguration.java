package htw.projektarbeit.webApplication;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

/*
 * Methode zum Anmelden des Nutzers
 * und zum Ausloggen nachdem ein erfolgreicher Login bereits stattgefunden hat
 */
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    //Login mit Hardcoding
    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http
                .authorizeRequests()
                    .antMatchers("/login-page.css").permitAll()
                    .anyRequest().authenticated()
                .and()
                .formLogin()
                    //nutzen der Custom Login-Seite statt der von Spring
                    //Nutzer zugreifen lassen ohne bereits Authentication zu brauchen
                    .loginPage("/login")
                    //.loginProcessingUrl("/login.html")
                    .defaultSuccessUrl("/website")
                    .permitAll()
                .and()
                //logout Prozessverwaltung
                //es wird zurueck auf die Login-Page verwiesen
                .logout(logout -> logout
                    .clearAuthentication(true)
                    .invalidateHttpSession(true)
                    .logoutSuccessUrl("/login"));
    } 

    /*
     * Methode zum Hardcoden der Anmeldedaten eines Nutzers
     * Rolle wird zum aktuellen Zeitpunkt nicht genutzt
     */ 
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