package com.docky.wiki.controller;

import com.docky.wiki.domain.Demo;
import com.docky.wiki.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Docky
 * @date 2021/4/15 9:52
 */
@RestController
public class TestController {
    @Value("${test.hello}")
    private  String testHello;
    @Autowired
    private TestService testService;
    @Autowired
    private TestService demoService;
    @RequestMapping("/hello")
    public String hello(){
        return "Hello"+testHello;
    }
    @PostMapping("/hello/post")
    public String helloPost(String name){

        return "Hello World! Post, "+ name;
    }

    @GetMapping("/demo/list")
    public List<Demo> list() throws Exception{
        return demoService.list();
    }
}
