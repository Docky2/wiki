package com.docky.wiki.req;

public class UserResetPwdReq {
    private Long id;

//    @NotNull(message = "密码不能为空")
//    @Pattern(regexp="^(?![0-9]+$)(?![a-zA-z]+$)[0-9A-Za-z]{6,20}$",message = "密码应该包含数字和英文，长度为6-20位")
    private String password;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserResetPwdReq{" +
                "id=" + id +
                ", password='" + password + '\'' +
                '}';
    }
}