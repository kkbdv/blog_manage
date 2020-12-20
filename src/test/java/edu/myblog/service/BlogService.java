package edu.myblog.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import edu.myblog.entity.Article;
import edu.myblog.entity.BlogVo;
import edu.myblog.entity.Picture;
import edu.myblog.mapper.BlogMapper;
import edu.myblog.mapper.PictureMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class BlogService {
    @Autowired
    private BlogMapper blogMapper;
    @Autowired
    private PictureMapper pictureMapper;

    // 测试返回id
    @Test
    public void testRet(){
        Article article = new Article();
        article.setText("我是test\uD83E\uDD75");
        article.setUid(11);
        int insert = blogMapper.insert(article);
        System.out.println(article);
    }
    @Test
    public void test2(){
        System.out.println( blogMapper.selectById(6));
    }

    /**
     * 批量存图测试
     */
    @Test
    @Transactional
    public void inserBatchTest(){
        String[] arr = {"1.jpg","2.jpg","3.jpg"};
       // 1.获取aid
        for(String s:arr){
            Picture picture = new Picture();
            picture.setAid(6);
            picture.setImageUrl(s);
            pictureMapper.insert(picture);
        }
    }
    @Test
    public void seleBlog(){
        QueryWrapper<Article> artQuery = new QueryWrapper<>();
        QueryWrapper<Picture> picQuery = new QueryWrapper<>();
        artQuery.eq("uid",9);
        List<Article> articles = blogMapper.selectList(artQuery);
        List<BlogVo> blogVos = new ArrayList<>();
        for(Article a:articles){
            BlogVo blogVo = new BlogVo();
            BeanUtils.copyProperties(a,blogVo);
            // 注入picture
            picQuery.eq("aid",a.getId());
            List<Picture> pictures = pictureMapper.selectList(picQuery);
            blogVo.setPicList(pictures);
            blogVos.add(blogVo);
        }


        for(BlogVo b:blogVos){
            System.out.println(String.format("=== %s",b));
        }

    }
    @Test
    public void fenye(){
        Page<Article> articlePage = new Page<>();
        articlePage.setCurrent(2);
        articlePage.setSize(2);
        Page<Article> articlePage1 = blogMapper.selectPage(articlePage, null);
        List<Article> records = articlePage1.getRecords();
        for(Article a:records){
            System.out.println(a);
        }
    }
    @Test
    @Transactional
    public void testDianzan(){
        Article article = blogMapper.selectById(24);
//        article.setThumbupCount(article.getThumbupCount()+1);
        blogMapper.updateById(article);
    }
}
