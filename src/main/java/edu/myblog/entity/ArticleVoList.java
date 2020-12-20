package edu.myblog.entity;

import java.util.List;

public class ArticleVoList {
    private List<ArticleVo> avos;
    private Long total;

    public List<ArticleVo> getAvos() {
        return avos;
    }

    public void setAvos(List<ArticleVo> avos) {
        this.avos = avos;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
}
