package htw.projektarbeit.webApplication;

import java.util.List;

import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    private final UserRepo repo;

    UserController(UserRepo repo){
        this.repo = repo;
    }

    @GetMapping("/users")
    List<AppUser> all(){
        return repo.findAll();
    }

    @PostMapping("/users")
    AppUser newUser(@RequestBody AppUser newUser){
        return repo.save(newUser);
    }

    @GetMapping("/users/{id}")
    EntityModel<AppUser> one(@PathVariable Long id){
        AppUser user = repo.findById(id).orElseThrow( () -> new UserNotFoundException(id));
        return EntityModel.of(user); //, linkTo(methodOn(UserController.class).one(id)).withSelfRel(),
        //linkTo(linkTo(methodOn(UserController.class).all()).withSelfRel("users")));
    }
    /*AppUser one(@PathVariable Long id){
        return repo.findById(id).orElseThrow( () -> new UserNotFoundException(id));
    }*/

    @PutMapping("/users/{id}")
    AppUser replaceUser(@RequestBody AppUser newUser, @PathVariable Long id){
        return( repo.findById(id).map( user -> {
            user.setName(newUser.getName());
            user.setRole(newUser.getRole());
            return repo.save(user);
        }).orElseGet( () -> {
            newUser.setId(id);
            return repo.save(newUser);
        }));
    }

    @DeleteMapping("/users/{id}")
    void deleteUser(@PathVariable Long id){
        repo.deleteById(id);
    }
}
