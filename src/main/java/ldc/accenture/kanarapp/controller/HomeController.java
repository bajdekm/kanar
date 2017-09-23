package ldc.accenture.kanarapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    @RequestMapping("/")
    public String sayHello(){
        return "welcome";
    }
}
