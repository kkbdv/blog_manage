package edu.myblog.controller;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.util.StatusViaSLF4JLoggerFactory;
import edu.myblog.entity.Admin;
import edu.myblog.entity.AdminVo;
import edu.myblog.entity.User;
import edu.myblog.entity.UserVo;
import edu.myblog.exception.NotExistException;
import edu.myblog.exception.PasswordErrorException;
import edu.myblog.service.UserService;
import edu.myblog.utils.ResponsCode;
import edu.myblog.utils.Result;
import org.apache.ibatis.annotations.Param;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.http.HttpHeaders;
import java.util.Map;

@CrossOrigin(maxAge = 3600)
@RestController
public class UserController {
    @Autowired
    UserService userService;
    private static Logger logger = (Logger) LoggerFactory.getLogger(UserController.class);

    @PostMapping("/register")
    public Result register(@RequestBody Map<String,Object> map){

        ResponsCode responsCode = userService.insertUser(map);
        switch (responsCode){
            case OK -> { return Result.createByOK(); }
            case ISEXIST -> {return Result.createDuplicate();}
            default -> {return Result.createByUnprocesableEntity();}
        }
    }

    @PostMapping("/login")
    public Result login(@RequestBody Map<String,String> fontMap){
        Map map = null;
        try {
            map = userService.loginUser(fontMap);
        } catch (NotExistException e) {
            return Result.createNotExist();
        } catch (PasswordErrorException e) {
            return Result.createPasswordError();
        }
        return Result.createOKByData(map);
    }

    /**
     * 用于测试，完成后删除
     * @return
     */
    @GetMapping("/testA")
    public Result tesA(){

        logger.info("执行testA");
        return Result.createByOK();
    }
    @GetMapping("/updateName")
    public Result changeName(@RequestParam String name,@RequestParam Integer uid){
        User user = userService.updateNickName(name, uid);
        return Result.createOKByData(user);
    }
    // 获取一个用户的信息
    @GetMapping("/getoneuser")
    public Result getOne(@RequestParam Integer uid){
        User user = userService.selectUserById(uid);
        return Result.createOKByData(user);
    }

    // 关注用户
    @GetMapping("/guanzhu")
    public Result concerUser(@RequestParam Integer uid,@RequestParam Integer fid){
        Boolean aBoolean = userService.insertFollower(uid, fid);
        if(aBoolean){
            return Result.createByOK();
        }
        return Result.createByUnprocesableEntity();
    }

    // 取消关注用户
    @GetMapping("/quguan")
    public Result unconcerUser(@RequestParam Integer uid,@RequestParam Integer fid){
        Boolean aBoolean = userService.deleteFollower(uid, fid);
        if(aBoolean){
            return Result.createByOK();
        }
        return Result.createByUnprocesableEntity();
    }

    // 管理后端获取用户
    @GetMapping("/backUser")
    public Result getBackendUser(@RequestParam(required = false,defaultValue = "") String query, @RequestParam Integer currentPage,@RequestParam  Integer size){
        UserVo userVo = userService.selectUserList(query, currentPage, size);
        return Result.createOKByData(userVo);
    }
    // 改变user状态
    @GetMapping("/userstatus")
    public Result changeUserState(@RequestParam Integer uid,@RequestParam Integer status){
        userService.updateUserStatus(uid,status);
        return Result.createByOK();
    }
    // 删除用户通过id
    @GetMapping("/deletuser")
    public Result RemoveUser(@RequestParam Integer uid){
        Boolean aBoolean = userService.deletUserById(uid);
        if(aBoolean){
            return Result.createByOK();
        }else {
            return Result.createByUnprocesableEntity();
        }
    }
    @GetMapping("/adminlist")
    public Result getAdminList(@RequestParam(required = false,defaultValue = "") String query, @RequestParam Integer currentPage,@RequestParam  Integer size){
        AdminVo adminVo = userService.selectAdminList(query, currentPage, size);
        return Result.createOKByData(adminVo);
    }

    @GetMapping("/adminstatus")
    public Result changeAdminState(@RequestParam Integer uid,@RequestParam Integer status){
        userService.updateAdminState(uid,status);
        return Result.createByOK();
    }
   @GetMapping("/deletadmin")
    public Result deletAdmin(@RequestParam Integer uid){
       Boolean aBoolean = userService.deletAdmin(uid);
       if(aBoolean){
           return Result.createByOK();
       }
       return Result.createByDeleteFailed();
   }
    @GetMapping("/addAdmin")
   public Result addAdmin(@RequestParam String account,@RequestParam String password){
        Boolean aBoolean = userService.insertAdmin(account, password);
        if(aBoolean){
            return Result.createByOK();
        }
        return Result.createByUnprocesableEntity();
    }
    @GetMapping("/getoneadmin")
    public Result getAdminById(@RequestParam Integer id){
        Admin admin = userService.selectOneAdmin(id);
        if(admin != null){
            return Result.createOKByData(admin);
        }
        return Result.createNotExist();
    }
    // 修改密码
    @GetMapping("/uadminpassword")
    public Result changeAdminPassword(@RequestParam Integer id,@RequestParam String password){
        Boolean aBoolean = userService.updateAdminPassword(id, password);
        if(aBoolean){
            return Result.createByOK();
        }
        return Result.createByUnprocesableEntity();
    }
}

