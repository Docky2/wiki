package com.docky.wiki.controller;

import com.alibaba.fastjson.JSONObject;
import com.docky.wiki.req.UserLoginReq;
import com.docky.wiki.req.UserQueryReq;
import com.docky.wiki.req.UserResetPwdReq;
import com.docky.wiki.req.UserSaveReq;
import com.docky.wiki.resp.CommonResp;
import com.docky.wiki.resp.PageResp;
import com.docky.wiki.resp.UserLoginResp;
import com.docky.wiki.resp.UserQueryResp;
import com.docky.wiki.service.UserService;
import com.docky.wiki.util.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.concurrent.TimeUnit;

/**
 * @author Docky
 * @date 2021/4/15 9:52
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private SnowFlake snowFlake;


    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public CommonResp list(@Valid UserQueryReq req) throws Exception{
        CommonResp<PageResp<UserQueryResp>> resp = new CommonResp<>();
        PageResp<UserQueryResp> list = userService.query(req);
        resp.setContent(list);
        return resp;
    }


    /**
     * @RequestBody这个注解对应的就是json方式的(POST)提交。
     * */
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public CommonResp save(@RequestBody @Valid UserSaveReq req) throws Exception{
        req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes()));
        CommonResp resp = new CommonResp<>();
        userService.save(req);
        return resp;
    }
    /**
     * 删除功能
     * */
    @RequestMapping(value = "/delete/{id}",method = RequestMethod.DELETE)
    public CommonResp delete(@PathVariable Long id) throws Exception{
        CommonResp resp = new CommonResp<>();
        userService.delete(id);
        return resp;
    }

    @RequestMapping(value = "/reset-password",method = RequestMethod.POST)
    public CommonResp resetPassword(@RequestBody @Valid UserResetPwdReq req) throws Exception{
        req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes()));
        CommonResp resp = new CommonResp<>();
        userService.resetPassword(req);
        return resp;
    }

    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public CommonResp login(@RequestBody @Valid UserLoginReq req) throws Exception{
        log.info("用户名为{}",req.getUsername());
        req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes()));
        CommonResp<UserLoginResp> resp = new CommonResp<>();

        UserLoginResp userLoginResp = userService.login(req);


        Long token = snowFlake.nextId();
        log.info("生成单点登录token{},放入redis中",token);

        // key是token value 是 UserLoginResp
        userLoginResp.setToken(token.toString());
        redisTemplate.opsForValue().set(token, JSONObject.toJSONString(userLoginResp),3600*24, TimeUnit.SECONDS);


        resp.setContent(userLoginResp);
        return resp;
    }


    /**
     * 删除功能
     * */
    @RequestMapping(value = "/logout/{token}",method = RequestMethod.GET)
    public CommonResp logout(@PathVariable String token) throws Exception{
        CommonResp resp = new CommonResp<>();
        redisTemplate.delete(token);
        log.info("从redis中删除token:{}",token);
        return resp;
    }


}
