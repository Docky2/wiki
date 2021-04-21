package com.docky.wiki.resp;

import javax.validation.constraints.NotNull;

public class UserLoginResp {
    private Long id;
    @NotNull(message = "用户名不能为空")
    private String username;
    @NotNull(message = "昵称不能为空")
    private String name;

    private String token;


    public Long getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return "UserLoginResp{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}