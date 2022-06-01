package ru.rozvezev.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class StartPageController {

    @GetMapping("/")
    public String startPage(){
        return "start_page";
    }
}
