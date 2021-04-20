package com.docky.wiki.req;

public class UserQueryReq extends PageReq{

    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "UserQueryReq{" +
                "username='" + username + '\'' +
                '}';
    }
}