package htw.projektarbeit.webApplication;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class AppUser {

    private @Id @GeneratedValue Long id;
    private String name;
    private String role;

    AppUser() {
    }

    AppUser(String name, String role) {
        this.name = name;
        this.role = role;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return this.role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o){
        if(this == o){
            return true;
        }
        if(!(o instanceof AppUser)){
            return false;
        }
        AppUser user = (AppUser) o;
        return(
            this.id == user.getId() && this.name.equals(user.getName()) && this.role.equals(user.getRole())
        );
    }


    @Override
    public int hashCode(){
        return Objects.hash(this.id, this.name, this.role);
    }


    @Override
    public String toString(){
        return "User " + this.id + "\nName: " + this.name + "\nRole: " + this.role;
    }
}
