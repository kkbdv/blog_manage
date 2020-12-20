package edu.myblog.entity;

import java.util.List;

public class AdminVo {

    private List<Admin> adminList;
    private Long total;

    public List<Admin> getAdminList() {
        return adminList;
    }

    public void setAdminList(List<Admin> adminList) {
        this.adminList = adminList;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
}
