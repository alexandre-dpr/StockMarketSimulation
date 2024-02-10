package bourse.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bourse")
public class BourseController {

    @GetMapping
    public String bourse(){
        return "OK";
    }
}
