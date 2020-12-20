package edu.myblog.entity;

import java.util.List;

public class UserVo{

    private List<User> userList;
    private Long total;

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
}
