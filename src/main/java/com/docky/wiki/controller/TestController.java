package com.docky.wiki.controller;

import com.docky.wiki.domain.Test;
import com.docky.wiki.service.TestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Docky
 * @date 2021/4/15 9:52
 */
@RestController
public class TestController {

    private static final Logger log = LoggerFactory.getLogger(TestController.class);

    @Autowired
    private RedisTemplate redisTemplate;

    @Value("${test.hello}")
    private  String testHello;
    @Autowired
    private TestService testService;

    @RequestMapping("/hello")
    public String hello(){
        return "Hello"+testHello;
    }
    @PostMapping("/hello/post")
    public String helloPost(String name){

        return "Hello World! Post, "+ name;
    }

    @GetMapping("/test/list")
    public List<Test> list() throws Exception{
        return testService.list();
    }


    @RequestMapping("/redis/set/{key}/{value}")
    public String set(@PathVariable Long key, @PathVariable String value){
        redisTemplate.opsForValue().set(key,value,3600, TimeUnit.SECONDS);
        log.info("key: {},value:{}",key,value);
        return "success";
    }

    @RequestMapping("/redis/get/{key}")
    public Object get(@PathVariable Long key){
        Object object = redisTemplate.opsForValue().get(key);
        log.info("key: {},value: {}",key,object);
        return object;
    }
}
