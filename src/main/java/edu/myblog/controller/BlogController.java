package edu.myblog.controller;

import ch.qos.logback.classic.Logger;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import edu.myblog.entity.ArticleVo;
import edu.myblog.entity.ArticleVoList;
import edu.myblog.entity.BlogVo;
import edu.myblog.entity.Comment;
import edu.myblog.service.BlogService;
import edu.myblog.utils.Result;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("/blog")
public class BlogController {
    private static Logger logger = (Logger) LoggerFactory.getLogger(BlogController.class);
    @Autowired
    private BlogService blogService;
    @PostMapping("/sendblog")
    public Result UserSend(@RequestBody Map<String,Object> map){
        Boolean isSuccess = blogService.insertBlog(map);
        if(isSuccess){
            return Result.createByOK();
        }
        return Result.createByUnprocesableEntity();
    }
// 获取树洞消息
    @GetMapping("/getShudong")
    public Result getShudonhList(@RequestParam Integer currPage,@RequestParam Integer uid){
        Page<BlogVo> blogVos = blogService.getShuDongByPageNum(currPage,uid);
        return Result.createOKByData(blogVos);
    }
    // 获取微博消息
    @GetMapping("/getblog")
    public Result getBlogList(@RequestParam Integer currPage){
        Page<BlogVo> blogVos = blogService.getBlogByPageNum(currPage);
        return Result.createOKByData(blogVos);
    }
    @GetMapping("/getmyblog")
    public Result getMyblogList(@RequestParam Integer currPage,@RequestParam Integer uid){
        Page<BlogVo> blogVos = blogService.getMyBlog(currPage, uid);
        return Result.createOKByData(blogVos);
    }

    @GetMapping("/getcommnet")
    public Result getComment(@RequestParam Integer aid){
             return Result.createOKByData(blogService.getCommnetByAid(aid));
    }
    @PostMapping("/sendcoment")
    public Result sendCommnet(@RequestBody Map<String,Object> map){
        Boolean aBoolean = blogService.saveComent(map);
        if(aBoolean){
            return Result.createByOK();
        }
        return Result.createByUnprocesableEntity();
    }

    @GetMapping("/zan")
    public Result clickThumbUp(@RequestParam Integer aid,@RequestParam Integer uid){
        Boolean aBoolean = blogService.addThumbUpCount(aid,uid);
        if(aBoolean){
            return Result.createByOK();
        }
        return Result.createByUnprocesableEntity();
    }
    @GetMapping("/unzan")
    public Result unclickThumbUp(@RequestParam Integer aid,@RequestParam Integer uid){
        Boolean aBoolean = blogService.reduceThumbUpCount(aid,uid);
        if(aBoolean){
            return Result.createByOK();
        }
        return Result.createByUnprocesableEntity();
    }
    @GetMapping("/cai")
    public Result clickThumbdown(@RequestParam Integer aid,@RequestParam Integer uid){
        Boolean aBoolean = blogService.addThumbDownCount(aid,uid);
        if(aBoolean){
            return Result.createByOK();
        }
        return Result.createByUnprocesableEntity();
    }
    @GetMapping("/uncai")
    public Result unclickThumbdown(@RequestParam Integer aid,@RequestParam Integer uid){
        Boolean aBoolean = blogService.reduceThumbDownCount(aid,uid);
        if(aBoolean){
            return Result.createByOK();
        }
        return Result.createByUnprocesableEntity();
    }
    @DeleteMapping("/deletblog")
    public Result deletBlog(@RequestParam Integer uid ,@RequestParam Integer aid){
        Boolean aBoolean = blogService.deletBlogByUid(uid, aid);
        if(aBoolean){
            return Result.createByOK();
        }
        return Result.createByUnprocesableEntity();
    }

    @RequestMapping(value = "push",produces = "text/event-stream;charset=UTF-8")
    @ResponseBody
    public String push(){
        System.out.println("push...");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "data:current time: "+new SimpleDateFormat("YYYY-MM-dd hh:mm:ss").format(new Date())+"\n\n";
    }
    @RequestMapping("/getbackblog")
    public Result getBackBlogList(@RequestParam(required = false,defaultValue = "")  String query,@RequestParam Integer pagenum, @RequestParam Integer pagesize){
        ArticleVoList articleVoList = blogService.selectBlogVo(query, pagenum, pagesize);
        return Result.createOKByData(articleVoList);
    }

}
