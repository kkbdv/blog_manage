package edu.myblog.controller;

import ch.qos.logback.classic.Logger;
import edu.myblog.entity.User;
import edu.myblog.exception.TypeErrorEception;
import edu.myblog.service.UserService;
import edu.myblog.utils.GenerateId;
import edu.myblog.utils.Result;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.net.http.HttpRequest;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("/upload")
public class UploadController {
    private static Logger logger = (Logger) LoggerFactory.getLogger(UploadController.class);
    private String uploadRootPath = "";
    @Autowired
    private UserService userService;
    /**
     * 上传单张图片
     * 1.从前端获取图片对象
     *  1.1 获取图片的Originalname
     *  1.2 验证格式是否合法
     * 2.创建静态资源文件夹
     * 生成唯一id 替换名字
     * @deprecated 3.把图片的唯一id+后缀存到数据库中，好处：删除时通过这个名字进行删除  @note 这里不进行数据存储
     * 4.把图片对象传进文件夹 （这一步可以通过前端的其他参数创建文件夹进行分类）
     * 5.返回图片的 半截url 如："/123456.jpg"
     * @param file
     * @param request
     * @return
     */
    @PostMapping("/pic")
    public Result uploadPicture(@RequestParam(value = "file",required = false)MultipartFile file, HttpServletRequest request){
        // 支持的格式
        String[] type = {"png","jpg","jpeg"};
        String[] split = {};
        String realFileName ="";
        // 1.创建工程路径
        uploadRootPath = request.getServletContext().getRealPath("images/");// D:\IdeaProjects\myblog\out\artifacts\myblog_Web_exploded\i 获取工程下的文件夹
        logger.info(uploadRootPath);
        // 1.1 检查路径+创建路径
        File uploadDir = new File(uploadRootPath);
        if(!uploadDir.exists()){
            uploadDir.mkdirs();
        }

        try {
            // 2获取文件名
            realFileName = file.getOriginalFilename();
            split = realFileName.split("\\.");

            boolean isValue = false;
            for (String s : type) {
                if(s.contains(split[1])){
                    isValue = true;
                }
            }
            if(!isValue){
                throw new TypeErrorEception();
            }

        } catch (NullPointerException e) {
            return Result.createByUploadFail();
        } catch (TypeErrorEception e){
            return Result.createByUploadFail();
        }
        // 3.拼接 生成新名字
        String newPicName = GenerateId.createOnlyIdByHex()+"."+split[1];
        logger.info("新的文件名:"+newPicName);
        // 4. 存储
        File targetFile = new File(uploadRootPath+newPicName);
        try {
            file.transferTo(targetFile);
        } catch (IOException e) {
            return Result.createByUploadFail();
        }
        // 临时对象，只返回名字和url
        class tempFIle{
            public String name="";
            public String url ="";

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }
        tempFIle reFile = new tempFIle();
        reFile.setName(realFileName);
        reFile.setUrl(newPicName);
        return Result.createOKByData(reFile);
    }
    @DeleteMapping("/deletePic")
    public Result deleteUploadFile(@RequestParam String url){
        File file = new File(uploadRootPath + url);
        if(file.isFile()&&file.exists()){
            try {
                file.delete();
            } catch (Exception e) {
                return Result.createByDeleteFailed();
            }
        }
        return Result.createByOK();
    }
    @PostMapping("updateheadThumb")
    public Result upDateHeadThumb(@RequestParam(value = "file",required = false)MultipartFile file, HttpServletRequest request,@RequestParam Integer uid){
        // 支持的格式
        String[] type = {"png","jpg","jpeg"};
        String[] split = {};
        String realFileName ="";
        // 1.创建工程路径
        uploadRootPath = request.getServletContext().getRealPath("images/");// D:\IdeaProjects\myblog\out\artifacts\myblog_Web_exploded\i 获取工程下的文件夹
        logger.info(uploadRootPath);
        // 1.1 检查路径+创建路径
        File uploadDir = new File(uploadRootPath);
        if(!uploadDir.exists()){
            uploadDir.mkdirs();
        }

        try {
            // 2获取文件名
            realFileName = file.getOriginalFilename();
            split = realFileName.split("\\.");

            boolean isValue = false;
            for (String s : type) {
                if(s.contains(split[1])){
                    isValue = true;
                }
            }
            if(!isValue){
                throw new TypeErrorEception();
            }

        } catch (NullPointerException e) {
            return Result.createByUploadFail();
        } catch (TypeErrorEception e){
            return Result.createByUploadFail();
        }
        // 3.拼接 生成新名字
        String newPicName = GenerateId.createOnlyIdByHex()+"."+split[1];
        logger.info("新的文件名:"+newPicName);
        // 4. 存储
        File targetFile = new File(uploadRootPath+newPicName);
        try {
            file.transferTo(targetFile);
        } catch (IOException e) {
            return Result.createByUploadFail();
        }
        // 修改头像地址
        User user = userService.updteHeadThumbByUid(newPicName, uid);
        if(user != null){
            return Result.createOKByData(user);
        }
        return Result.createByUploadFail();
    }

}
