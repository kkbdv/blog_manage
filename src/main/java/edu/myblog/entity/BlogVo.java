package edu.myblog.entity;

import java.util.List;

public class BlogVo extends Article{
    private List<Picture> picList;
    private String nickName;
    private String account;
    private String headThumb;
    private Integer thumbupCount;
    private Integer thumbdownCount;
    private Integer commentCount;

    public List<Picture> getPicList() {
        return picList;
    }

    public void setPicList(List<Picture> picList) {
        this.picList = picList;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getHeadThumb() {
        return headThumb;
    }

    public void setHeadThumb(String headThumb) {
        this.headThumb = headThumb;
    }

    public Integer getThumbupCount() {
        return thumbupCount;
    }

    public void setThumbupCount(Integer thumbupCount) {
        this.thumbupCount = thumbupCount;
    }

    public Integer getThumbdownCount() {
        return thumbdownCount;
    }

    public void setThumbdownCount(Integer thumbdownCount) {
        this.thumbdownCount = thumbdownCount;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }
}
