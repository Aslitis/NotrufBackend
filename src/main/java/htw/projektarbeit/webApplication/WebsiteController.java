package htw.projektarbeit.webApplication;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WebsiteController {
    
    @GetMapping("/login") //"/home")
    public String login(@RequestParam(name="name", required=false, defaultValue="default") String name, Model model){
        model.addAttribute("name", name);
        return "login";
    }

    @GetMapping("/website")
    public String website(@RequestParam(name="name", required=false, defaultValue="default") String name, Model model){
        model.addAttribute("name", name);
        return "website";
    }
}
