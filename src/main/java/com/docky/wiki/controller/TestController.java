package com.docky.wiki.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Docky
 * @date 2021/4/15 9:52
 */
@RestController
public class TestController {
    @Value("${test.hello}")
    private  String testHello;
    @RequestMapping("/hello")
    public String hello(){
        return "Hello"+testHello;
    }
    @PostMapping("/hello/post")
    public String helloPost(String name){

        return "Hello World! Post, "+ name;
    }
}
