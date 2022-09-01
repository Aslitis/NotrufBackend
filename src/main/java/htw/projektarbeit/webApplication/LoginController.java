package htw.projektarbeit.webApplication;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

//Klasse, die auf Login Route hoert
@Controller
public class LoginController {
    
    @GetMapping ("/login.html")
    public String login(){
        return "login.html";
    }

    @GetMapping ("/website.html")
    public String website() {
        return "website.html";
    }

}
