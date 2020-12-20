package edu.myblog.service;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import edu.myblog.entity.*;
import edu.myblog.exception.NotExistException;
import edu.myblog.exception.PasswordErrorException;
import edu.myblog.mapper.*;
import edu.myblog.utils.ResponsCode;
import edu.myblog.utils.TokenGenerate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    FollowMapper followMapper;
    @Autowired
    SearchMapper searchMapper;
    @Autowired
    BlogMapper blogMapper;
    @Autowired
    AdminMapper adminMapper;

    /**
     * 新建用户
     * @param map
     * @return
     */
    public ResponsCode insertUser(Map map) {
        if(isExistAccount(map)){
            return ResponsCode.ISEXIST;
        }
        User user = new User();
        user.setAccount(map.get("account").toString());
        user.setBirthday(Long.parseLong(map.get("birthDate").toString()));
        user.setNickName(map.get("nickname").toString());
        user.setPassword(map.get("password").toString());
        user.setSex(map.get("sex").toString());
        int insert = userMapper.insert(user);
        if(insert>0){
            return ResponsCode.OK;
        }else {
           return ResponsCode.Unprocesable_entity;
        }
    }

    /**
     * 用户登录
     * @return
     */
    public Map loginUser(Map fontMap) throws NotExistException, PasswordErrorException {
        Map map = new HashMap();
        String account = (String) fontMap.get("account");
        String password = (String) fontMap.get("password");
        map.put("token",TokenGenerate.createToken(30,account));
        QueryWrapper<User> wapper = new QueryWrapper<>();
        wapper.eq("account",account);
        User user = userMapper.selectOne(wapper);
        if(user == null) {
            // 不存在该用户
            throw new NotExistException();
        }
        if(!user.getPassword().equals(password)){
            throw new PasswordErrorException();
        }
        map.put("user",user);
        return map;
    }

    /**
     * 校验是否重名
     * @param map
     * @return
     */
    private boolean isExistAccount(Map map){
        if(userMapper.seletOneUserById(map.get("account").toString()) != null){
            return true;
        }
        return false;
    }

    public User updteHeadThumbByUid(String url,Integer uid){
        QueryWrapper qw = new QueryWrapper();
        qw.eq("id",uid);
        User user = userMapper.selectOne(qw);
        user.setHeadThumb(url);
        userMapper.updateById(user);
        return userMapper.selectOne(qw);
    }

    // 修改昵称
    public User updateNickName(String name,Integer uid){
        QueryWrapper qw = new QueryWrapper();
        qw.eq("id",uid);
        User user = userMapper.selectOne(qw);
        user.setNickName(name);
        userMapper.updateById(user);
        return userMapper.selectOne(qw);
    }

    // 关注用户
    public Boolean insertFollower(Integer uid,Integer fid){
        Follow follow = new Follow();
        follow.setFid(fid);
        follow.setUid(uid);
        int insert = followMapper.insert(follow);
        if(insert>0){
            return true;
        }
        return false;
    }
    // 取关用户
    public Boolean deleteFollower(Integer uid,Integer fid){
       QueryWrapper qw = new QueryWrapper();
       qw.eq("uid",uid);
        qw.eq("fid",fid);
        int insert = followMapper.delete(qw);
        if(insert>0){
            return true;
        }
        return false;
    }
    // 通过id获取用户的信息
    public User selectUserById(Integer uid){
        QueryWrapper qw = new QueryWrapper();
        qw.eq("id",uid);
        return userMapper.selectOne(qw);

    }
    // 通过关键词查找用户信息
    public List<User> selectUserByKeyWord(String keyword){
        // 存进Search
        QueryWrapper sw = new QueryWrapper();
        sw.eq("key_word",keyword);
        Search search = new Search();
        search.setKeyWord(keyword);
        Search search1 = searchMapper.selectOne(sw);
        if(search1 != null){
            search1.setCount(search1.getCount()+1);
            searchMapper.update(search1,sw);
        }else{
            searchMapper.insert(search);
        }


        QueryWrapper qw = new QueryWrapper();
        qw.like("account",keyword);
        qw.or();
        qw.like("nick_name",keyword);
        List list = userMapper.selectList(qw);
        return list;
    }

    // 通过页数获取所有用户信息
    public UserVo selectUserList(String queryInfo, Integer currentPage, Integer size){
        Page<User> userPage = new Page<>();
        userPage.setCurrent(currentPage);
        userPage.setSize(size);
        QueryWrapper qw = new QueryWrapper();
        if(!queryInfo.isEmpty()){
            qw.like("account",queryInfo);
            qw.or();
            qw.like("nick_name",queryInfo);
        }
        Page page = userMapper.selectPage(userPage, qw);
        UserVo uv = new UserVo();
        uv.setUserList(page.getRecords());
        uv.setTotal( page.getTotal());
        return uv;
    }
    // 更新用户状态
    public void updateUserStatus(Integer uid,Integer status){
        if(status ==1){
            QueryWrapper qw = new QueryWrapper();
            qw.eq("id",uid);
            User user = userMapper.selectOne(qw);
            user.setStatus(1);
            userMapper.updateById(user);
        }
        if(status == 0){
            QueryWrapper qw = new QueryWrapper();
            qw.eq("id",uid);
            User user = userMapper.selectOne(qw);
            user.setStatus(0);
            userMapper.updateById(user);
        }
    }
    // 删除用户
    @Transactional
    public Boolean deletUserById(Integer uid){
        int i = userMapper.deleteById(uid);
        if(i>0){
            // 删除微博内容
            QueryWrapper qw = new QueryWrapper();
            qw.eq("uid",uid);
            blogMapper.delete(qw);
            return true;

        }else {
            return false;
        }
    }

    // 获取用户列表
    public AdminVo selectAdminList(String query,Integer currentPage,Integer size){

        Page<Admin> page = new Page<>();
        page.setCurrent(currentPage);
        page.setSize(size);
        QueryWrapper qw = new QueryWrapper();
        if(!query.isEmpty()){
            qw.like("account",query);
        }
        Page pageAdmin = adminMapper.selectPage(page, qw);
        AdminVo av = new AdminVo();
        av.setAdminList(pageAdmin.getRecords());
        av.setTotal(pageAdmin.getTotal());
        return av;
    }

    // 修改管理员状态
    public void updateAdminState(Integer uid,Integer status){
        if(status ==1){
            QueryWrapper qw = new QueryWrapper();
            qw.eq("id",uid);
            Admin admin = adminMapper.selectOne(qw);
            admin.setStatus(1);
            adminMapper.updateById(admin);
        }
        if(status == 0){
            QueryWrapper qw = new QueryWrapper();
            qw.eq("id",uid);
            Admin admin = adminMapper.selectOne(qw);
            admin.setStatus(0);
            adminMapper.updateById(admin);
        }
    }
    // 删除管理员
    @Transactional
    public Boolean deletAdmin(Integer uid){
        int i = adminMapper.deleteById(uid);
        if(i>0){
            return true;
        }
        return false;
    }
    // 添加管理员
    public Boolean insertAdmin(String account,String password){
        Admin admin = new Admin();
        admin.setAccount(account);
        admin.setPassword(password);
        int insert = adminMapper.insert(admin);
        if(insert>0){
            return true;
        }else {
            return false;
        }

    }
    // 获取一个管理员的账号信息
    public Admin selectOneAdmin(Integer id){
        return adminMapper.selectById(id);
    }
    // 修改用户密码
    public Boolean updateAdminPassword(Integer id,String password){
        Admin admin = adminMapper.selectById(id);
        admin.setPassword(password);
        int i = adminMapper.updateById(admin);
        if(i>0){
            return true;
        }
        return false;
    }
}
