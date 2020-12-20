package edu.myblog.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import edu.myblog.entity.Article;
import edu.myblog.entity.Hot;
import edu.myblog.mapper.BlogMapper;
import edu.myblog.mapper.HotMapper;
import edu.myblog.mapper.ThumbUpMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HotService {
    @Autowired
    private HotMapper hotMapper;
    @Autowired
    private BlogMapper blogMapper;
    @Autowired

    private ThumbUpMapper thumbUpMapper;
    @Scheduled(cron = "*/30 * * * * ?")
    public void produceHot(){
    }



}
