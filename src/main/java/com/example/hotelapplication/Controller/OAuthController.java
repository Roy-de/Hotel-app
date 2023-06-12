package com.example.hotelapplication.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/OAuth")
public class OAuthController {
    @GetMapping
    public String home(){
        return "home";
    }
    @GetMapping("/secured")
    public String secured(){
        return "Hello secured";
    }
}
