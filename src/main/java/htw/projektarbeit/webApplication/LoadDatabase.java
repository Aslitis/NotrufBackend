package htw.projektarbeit.webApplication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadDatabase {
    
    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(UserRepo repo){
        return args -> {
            log.info("Lade " + repo.save(new AppUser("Fabi", "Feuerwehr")));
            log.info("Lade " + repo.save(new AppUser("Knirps", "Anrufer")));
        };
    }
}
