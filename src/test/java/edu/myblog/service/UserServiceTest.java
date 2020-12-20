package edu.myblog.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import edu.myblog.entity.User;
import edu.myblog.mapper.UserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class UserServiceTest {
    @Autowired
    private UserMapper userMapper;
    @Test
    @Transactional
    public  void testInsert(){
        User user = new User();
        user.setNickName("我是兔兔");
        user.setAccount("1234");
        user.setPassword("1234");
        user.setBirthday(1231L);
        userMapper.insert(user);
    }

    @Test
    public void testCheckAccount(){
        System.out.println( userMapper.seletOneUserById("12345"));
    }
    @Test
    public void testSelectOne(){
        QueryWrapper<User> wapper = new QueryWrapper<>();
        wapper.eq("account","12345");
        User user = userMapper.selectOne(wapper);
        System.out.println(user);
    }

}