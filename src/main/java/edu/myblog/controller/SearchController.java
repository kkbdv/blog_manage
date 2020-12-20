package edu.myblog.controller;

import edu.myblog.entity.User;
import edu.myblog.service.UserService;
import edu.myblog.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin
@RestController
@RequestMapping("/search")
public class SearchController {
    @Autowired
    private UserService userService;
    @GetMapping("/user")
    public Result getList(@RequestParam String keyword){

        List<User> users = userService.selectUserByKeyWord(keyword);
        return Result.createOKByData(users);
    }
}
