package com.docky.wiki.req;

import javax.validation.constraints.NotNull;

public class UserSaveReq {
    private Long id;
    @NotNull(message = "用户名不能为空")
    private String username;
    @NotNull(message = "昵称不能为空")
    private String name;
//    @NotNull(message = "密码不能为空")
//    @Pattern(regexp="^(?![0-9]+$)(?![a-zA-z]+$)[0-9A-Za-z]{6,20}$",message = "密码应该包含数字和英文，长度为6-20位")
    private String password;

    public Long getId() {
        return id;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", username=").append(username);
        sb.append(", name=").append(name);
        sb.append(", password=").append(password);
        sb.append("]");
        return sb.toString();
    }
}