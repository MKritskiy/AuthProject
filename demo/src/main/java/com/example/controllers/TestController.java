package com.example.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.swing.text.View;

@Controller
public class TestController {
    @GetMapping("/test")
    public ModelAndView test() {
        ModelAndView mv = new ModelAndView("test");
        return mv;
    }
}
