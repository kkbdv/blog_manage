package edu.myblog.controller;

import edu.myblog.service.HotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("/hot")
public class HotController {
    @Autowired
    HotService hotService;
    // 未使用的定时任务
    @GetMapping("/test")
    public void test(){
        hotService.produceHot();
    }

}
