package edu.myblog.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import edu.myblog.entity.Follow;
import edu.myblog.entity.ThumbUp;
import edu.myblog.mapper.FollowMapper;
import edu.myblog.mapper.ThumbDownMapper;
import edu.myblog.mapper.ThumbUpMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserFancyService {
    @Autowired
    private ThumbUpMapper thumbUpMapper;
    @Autowired
    private ThumbDownMapper thumbDownMapper;
    @Autowired
    private FollowMapper followMapper;

    public List<ThumbUp> getUserLike(Integer uid){
        QueryWrapper qw = new QueryWrapper();
        qw.eq("uid",uid);
        return  thumbUpMapper.selectList(qw);
    }
    public List<ThumbUp> getUserunLike(Integer uid){
        QueryWrapper qw = new QueryWrapper();
        qw.eq("uid",uid);
        return  thumbDownMapper.selectList(qw);
    }

    // 获取粉丝数量
    public Integer getFollowCount(Integer fid) {
        QueryWrapper qw = new QueryWrapper();
        qw.eq("fid",fid);
        return followMapper.selectCount(qw);
    }
    // 获取关注的人数
    public Integer getConcernCount(Integer uid){
        QueryWrapper qw = new QueryWrapper();
        qw.eq("uid",uid);
        return followMapper.selectCount(qw);
    }

    public Boolean selectFollower(Integer uid,Integer fid){
        QueryWrapper qw = new QueryWrapper();
        qw.eq("uid",uid);
        qw.eq("fid",fid);
        Follow follow = followMapper.selectOne(qw);
        if(follow != null){
            return true;
        }
        return false;
    }

    public List<Integer> selectConcerList(Integer uid){
        QueryWrapper qw = new QueryWrapper();
        qw.eq("uid",uid);
        List<Follow> list = followMapper.selectList(qw);
        List<Integer> arr = new ArrayList<>();
        for(Follow f:list){
            arr.add(f.getFid());
        }
        return arr;
    }
}
