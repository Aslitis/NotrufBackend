package htw.projektarbeit.webApplication;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<AppUser, Long>{
    
}
