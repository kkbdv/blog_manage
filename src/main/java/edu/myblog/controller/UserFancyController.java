package edu.myblog.controller;

import edu.myblog.mapper.ThumbUpMapper;
import edu.myblog.service.UserFancyService;
import edu.myblog.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/userfancy")
public class UserFancyController {
    @Autowired
    private UserFancyService userFancyService;
    @GetMapping("/userlike")
    public Result userLike(@RequestParam Integer uid){
        return Result.createOKByData(userFancyService.getUserLike(uid));
    }
    @GetMapping("/userunlike")
    public Result userunLike(@RequestParam Integer uid){
        return Result.createOKByData(userFancyService.getUserunLike(uid));
    }
    @GetMapping("/fcount")
    public Result userFensi(@RequestParam Integer fid){
        return Result.createOKByData(userFancyService.getFollowCount(fid));
    }
    @GetMapping("/ccount")
    public Result userConcer(@RequestParam Integer uid){
        return Result.createOKByData(userFancyService.getConcernCount(uid));
    }
    @GetMapping("/hasconcer")
    public Result hasConcer(@RequestParam Integer uid, @RequestParam Integer fid){
        Boolean aBoolean = userFancyService.selectFollower(uid, fid);
        if(aBoolean){
            return Result.createByOK();
        }
        return Result.createByUnprocesableEntity();
    }

    // 关注列表
    @GetMapping("/getConcerlist")
    public Result getList(@RequestParam Integer uid){
        List<Integer> integers = userFancyService.selectConcerList(uid);
        return Result.createOKByData(integers);
    }

}
