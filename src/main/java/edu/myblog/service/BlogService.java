package edu.myblog.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import edu.myblog.config.Myhandler;
import edu.myblog.entity.*;
import edu.myblog.mapper.*;
import edu.myblog.utils.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.socket.TextMessage;

import javax.annotation.Resource;
import javax.websocket.Session;
import java.net.http.WebSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

@Service
public class BlogService {
    @Autowired
    private BlogMapper blogMapper;
    @Autowired
    private PictureMapper pictureMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private Myhandler myhandler;
    @Autowired
    private ThumbUpMapper thumbUpMapper;
    @Autowired
    private ThumbDownMapper thumbDownMapper;


    // 这个配置使Exception也回滚
    @Transactional(rollbackFor=Exception.class)
    public Boolean insertBlog(Map map){
        // 1.通过用户id 创建blog记录，存进text,返回这条记录的id
        try {
            Article article = new Article();
            article.setText(map.get("textarea").toString());
            article.setUid((Integer) map.get("uid"));
            article.setType((Integer)map.get("type"));
            int insert = blogMapper.insert(article);
            // 2.通过1的bid, 把图片链接批量存进数据库
            ArrayList<String> urlList = (ArrayList) map.get("uploadFileList");
                for(String s:urlList){
                    Picture pic = new Picture();
                    pic.setAid(article.getId());
                    pic.setImageUrl(s);
                    pictureMapper.insert(pic);
                }
        } catch (Exception e) {
            return false;
        }
        BroadCastDo bd = new BroadCastDo();
        bd.setType("updateblog");
        String s = JSON.toJSONString(bd);
        myhandler.sendAllMessage(new TextMessage(s));
        return true;
    }
// 通过页数返回微博内容
    public Page<BlogVo> getBlogByPageNum(Integer pageNum){
        Page<Article> articlePage = new Page<>();
        articlePage.setCurrent(pageNum);
        articlePage.setSize(5);
        // 查询条件
        QueryWrapper aqw = new QueryWrapper();


        aqw.select().orderByDesc("create_time");
        Page<Article> articlePage1 = blogMapper.selectPage(articlePage, aqw);
        List<Article> records = articlePage1.getRecords();
        List<BlogVo> BlogVos = new ArrayList<>();
        for(Article a:records){
            if(a.getType()==0){
                continue;
            }
            BlogVo blogVo = new BlogVo();
            // 昵称、账号、头像
            QueryWrapper uqw = new QueryWrapper();
            uqw.eq("id",a.getUid());
            User user = userMapper.selectOne(uqw);
            blogVo.setNickName(user.getNickName());
            blogVo.setAccount(user.getAccount());
            blogVo.setHeadThumb(user.getHeadThumb());
            // 图片列表
            QueryWrapper pqw = new QueryWrapper();
            BeanUtils.copyProperties(a,blogVo);
            pqw.eq("aid",a.getId());
            List list = pictureMapper.selectList(pqw);
            blogVo.setPicList(list);
            // 点赞数获取
            Integer thumbCount = thumbUpMapper.selectCount(pqw);
            blogVo.setThumbupCount(thumbCount);
            // 点踩数获取
            Integer integer = thumbDownMapper.selectCount(pqw);
            blogVo.setThumbdownCount(integer);
            // 评论数获取
            Integer commentCount = commentMapper.selectCount(pqw);
            blogVo.setCommentCount(commentCount);
            BlogVos.add(blogVo);
        }
        Page<BlogVo> rPage = new Page<>();
        BeanUtils.copyProperties(articlePage1,rPage);
        rPage.setRecords(BlogVos);
        return rPage;
    }
    // 查看评论
   public List<CommentVo> getCommnetByAid(Integer id){
        QueryWrapper qw = new QueryWrapper();
        QueryWrapper uw = new QueryWrapper();
        qw.eq("aid",id);
        qw.orderByDesc("create_time");
       List<Comment> selectList = commentMapper.selectList(qw);
       List<CommentVo> commentVos = new ArrayList<>();
       for(Comment c:selectList){
          CommentVo commentVo = new CommentVo();
          BeanUtils.copyProperties(c,commentVo);
           uw.eq("id",c.getUid());
           User user = userMapper.selectOne(uw);
           commentVo.setNickName(user.getNickName());
           commentVo.setAccount(user.getAccount());
           commentVo.setHeadThumb(user.getHeadThumb());
           commentVos.add(commentVo);
       }
       return commentVos;
   }
   // 发表评论
    public Boolean saveComent(Map map){
        Comment comment = new Comment();
        comment.setAid((Integer) map.get("aid"));
        comment.setUid((Integer) map.get("uid"));
        comment.setText(map.get("text").toString());
        int insert = commentMapper.insert(comment);
        // 广播
        BroadCastDo bd = new BroadCastDo();
        bd.setType("comment");
        bd.setTarget((Integer) map.get("aid"));
        QueryWrapper qq = new QueryWrapper();
        qq.eq("aid",(Integer) map.get("aid"));
        bd.setNum(commentMapper.selectCount(qq));
        String s = JSON.toJSONString(bd);
        if(insert>0){
            myhandler.sendAllMessage(new TextMessage(s));
            return true;
        }
        return false;
    }
    // 点赞微博
    public Boolean addThumbUpCount(Integer aid,Integer uid){
        ThumbUp tu = new ThumbUp();
        tu.setAid(aid);
        tu.setUid(uid);
        int i = thumbUpMapper.insert(tu);
        BroadCastDo bc = new BroadCastDo();
        QueryWrapper qw = new QueryWrapper();
        qw.eq("aid",aid);
        bc.setType("thumbup");
        bc.setTarget(aid);
        bc.setNum(thumbUpMapper.selectCount(qw));
        String json = JSON.toJSONString(bc);
        if(i>0){
            // 广播消息
            myhandler.sendAllMessage(new TextMessage(json));
            return true;
        }
        return false;
    }
    // 取消点赞
    public Boolean reduceThumbUpCount(Integer aid,Integer uid){
        QueryWrapper qw = new QueryWrapper();
        qw.eq("aid",aid);
        qw.eq("uid",uid);
        int i = thumbUpMapper.delete(qw);
        // 广播信息
        BroadCastDo bc = new BroadCastDo();
        QueryWrapper bqw = new QueryWrapper();
        bqw.eq("aid",aid);
        bc.setType("thumbup");
        bc.setTarget(aid);
        bc.setNum(thumbUpMapper.selectCount(bqw));
        String json = JSON.toJSONString(bc);
        if(i>0){
            myhandler.sendAllMessage(new TextMessage(json));
            return true;
        }
        return false;
    }
    // 点踩
    public Boolean addThumbDownCount(Integer aid,Integer uid){
        ThumbDown td = new ThumbDown();
        td.setAid(aid);
        td.setUid(uid);
        int i = thumbDownMapper.insert(td);
        BroadCastDo bc = new BroadCastDo();
        QueryWrapper qw = new QueryWrapper();
        qw.eq("aid",aid);
        bc.setType("thumbdown");
        bc.setTarget(aid);
        bc.setNum(thumbDownMapper.selectCount(qw));
        String json = JSON.toJSONString(bc);
        if(i>0){
            // 广播消息
            myhandler.sendAllMessage(new TextMessage(json));
            return true;
        }
        return false;
    }
    // 点踩取消
    public Boolean reduceThumbDownCount(Integer aid,Integer uid){
        QueryWrapper qw = new QueryWrapper();
        qw.eq("aid",aid);
        qw.eq("uid",uid);
        int i = thumbDownMapper.delete(qw);
        // 广播信息
        BroadCastDo bc = new BroadCastDo();
        QueryWrapper bqw = new QueryWrapper();
        bqw.eq("aid",aid);
        bc.setType("thumbdown");
        bc.setTarget(aid);
        bc.setNum(thumbDownMapper.selectCount(bqw));
        String json = JSON.toJSONString(bc);
        if(i>0){
            myhandler.sendAllMessage(new TextMessage(json));
            return true;
        }
        return false;
    }
    // blog 获取用户的树洞消息
    public Page<BlogVo> getShuDongByPageNum(Integer pageNum,Integer uid){
        Page<Article> articlePage = new Page<>();
        articlePage.setCurrent(pageNum);
        articlePage.setSize(5);
        // 查询条件
        QueryWrapper aqw = new QueryWrapper();

        aqw.eq("uid",uid);
        aqw.select().orderByDesc("create_time");
        Page<Article> articlePage1 = blogMapper.selectPage(articlePage, aqw);
        List<Article> records = articlePage1.getRecords();
        List<BlogVo> BlogVos = new ArrayList<>();
        for(Article a:records){
            if(a.getType()==1){
                continue;
            }
            BlogVo blogVo = new BlogVo();
            // 昵称、账号、头像
            QueryWrapper uqw = new QueryWrapper();
            uqw.eq("id",a.getUid());
            User user = userMapper.selectOne(uqw);
            blogVo.setNickName(user.getNickName());
            blogVo.setAccount(user.getAccount());
            blogVo.setHeadThumb(user.getHeadThumb());
            // 图片列表
            QueryWrapper pqw = new QueryWrapper();
            BeanUtils.copyProperties(a,blogVo);
            pqw.eq("aid",a.getId());
            List list = pictureMapper.selectList(pqw);
            blogVo.setPicList(list);
            // 点赞数获取
            Integer thumbCount = thumbUpMapper.selectCount(pqw);
            blogVo.setThumbupCount(thumbCount);
            // 点踩数获取
            Integer integer = thumbDownMapper.selectCount(pqw);
            blogVo.setThumbdownCount(integer);
            // 评论数获取
            Integer commentCount = commentMapper.selectCount(pqw);
            blogVo.setCommentCount(commentCount);
            BlogVos.add(blogVo);
        }
        Page<BlogVo> rPage = new Page<>();
        BeanUtils.copyProperties(articlePage1,rPage);
        rPage.setRecords(BlogVos);
        return rPage;
    }
    // 获取我的博客
    public Page<BlogVo> getMyBlog(Integer pageNum,Integer uid){
        Page<Article> articlePage = new Page<>();
        articlePage.setCurrent(pageNum);
        articlePage.setSize(5);
        // 查询条件
        QueryWrapper aqw = new QueryWrapper();
        aqw.eq("uid",uid);

        aqw.select().orderByDesc("create_time");
        Page<Article> articlePage1 = blogMapper.selectPage(articlePage, aqw);
        List<Article> records = articlePage1.getRecords();
        List<BlogVo> BlogVos = new ArrayList<>();
        for(Article a:records){
            if(a.getType() !=1){
                continue;
            }
            BlogVo blogVo = new BlogVo();
            // 昵称、账号、头像
            QueryWrapper uqw = new QueryWrapper();
            uqw.eq("id",a.getUid());
            User user = userMapper.selectOne(uqw);
            blogVo.setNickName(user.getNickName());
            blogVo.setAccount(user.getAccount());
            blogVo.setHeadThumb(user.getHeadThumb());
            // 图片列表
            QueryWrapper pqw = new QueryWrapper();
            BeanUtils.copyProperties(a,blogVo);
            pqw.eq("aid",a.getId());
            List list = pictureMapper.selectList(pqw);
            blogVo.setPicList(list);
            // 点赞数获取
//            Integer thumbCount = thumbUpMapper.selectCount(pqw);
//            blogVo.setThumbupCount(thumbCount);
            // 点踩数获取
//            Integer integer = thumbDownMapper.selectCount(pqw);
//            blogVo.setThumbdownCount(integer);
            // 评论数获取
//            Integer commentCount = commentMapper.selectCount(pqw);
//            blogVo.setCommentCount(commentCount);
            BlogVos.add(blogVo);
        }
        Page<BlogVo> rPage = new Page<>();
        BeanUtils.copyProperties(articlePage1,rPage);
        rPage.setRecords(BlogVos);
        return rPage;
    }

    public Boolean deletBlogByUid(Integer uid,Integer aid){
        QueryWrapper qw = new QueryWrapper();
        qw.eq("uid",uid);
        qw.eq("id",aid);
        int delete = blogMapper.delete(qw);
        if(delete>0){
            // 广播
            return true;
        }
        return false;

    }

    // 获取ArtistVo给后台提供数据
    public ArticleVoList selectBlogVo(String query,Integer pagenum,Integer pagesieze){
        QueryWrapper qw = new QueryWrapper();
        Page<Article> pageArticle = new Page<>();
        pageArticle.setCurrent(pagenum);
        pageArticle.setSize(pagesieze);
        Page page = blogMapper.selectPage(pageArticle, qw);
        List<Article> records = page.getRecords();
        List<ArticleVo> avos = new ArrayList<>();
        for(Article a:records){
            QueryWrapper aw = new QueryWrapper();
            aw.eq("id",a.getUid());
            User user = userMapper.selectOne(aw);
            ArticleVo avo = new ArticleVo();
            BeanUtils.copyProperties(a,avo);
            avo.setUsername(user.getNickName());
            avo.setAccount(user.getAccount());
            avos.add(avo);
        }
        ArticleVoList avl = new ArticleVoList();
        avl.setAvos(avos);
        avl.setTotal(page.getTotal());
        return avl;
    }
}
