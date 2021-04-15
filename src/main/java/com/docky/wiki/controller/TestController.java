package com.docky.wiki.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
    @PostMapping("/hello/post")
    public String helloPost(String name){

        return "Hello World! Post, "+ name;
    }
}
