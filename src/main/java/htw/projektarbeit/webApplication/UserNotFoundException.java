package htw.projektarbeit.webApplication;

public class UserNotFoundException extends RuntimeException {
    UserNotFoundException(Long id){
        super("Benutzer mit folgender ID konnte nicht gefunden werden: " + id);
    }
}
