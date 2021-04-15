package com.docky.wiki.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Docky
 * @date 2021/4/15 9:52
 */
@RestController
public class TestController {

    @RequestMapping("/hello")
    public String hello(){
        return "Hello";
    }
}
