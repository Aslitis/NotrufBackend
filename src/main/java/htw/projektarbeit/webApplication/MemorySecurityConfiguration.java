package htw.projektarbeit.webApplication;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
public class MemorySecurityConfiguration extends WebSecurityConfigurerAdapter {

    //hardcoding der Anmeldung
    //Verschluesselung der Passwoerter mit BCrypt
    //Nur Authentifizierung
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .inMemoryAuthentication()
            .withUser("fabian")
                .password("fabian")
                .roles("admin")
            .and().withUser("max")
                .password("max")
                .roles("admin")
            .and().withUser("fabienne")
                .password("fabienne")
                .roles("admin")
            .and().withUser("nutzer")
                .password("nutzer")
                .roles("nutzer")
            .and().passwordEncoder(new BCryptPasswordEncoder());
    }

    //Autorisierung
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin();

        //gibt Zugriff auf Registry-Objekt zum Speichern der Webrouten
        //festlegen der Routen fuer verschiedene Rollen
        http.authorizeRequests()
            .antMatchers("/admin/*").hasAnyRole("admin", "nutzer")
            .antMatchers("/nutzer/*").hasRole("nutzer")
            .antMatchers("/").permitAll();
    }
    
}
