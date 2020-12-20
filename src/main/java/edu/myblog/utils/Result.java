package edu.myblog.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.apache.ibatis.jdbc.Null;

import java.io.Serializable;
// 忽略变量为null的 成员
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result<T> implements Serializable {
    private int status;
    private String msg;
    private T data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Result(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public Result(int status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }
    //----------------------------------------------
    // 快速构造方法
    //----------------------------------------------
    // 创建成功
    public static<T> Result<T> createByOK(){
        return new Result<T>(ResponsCode.OK.getCode(),ResponsCode.OK.getMsg());
    }
    public static<T> Result<T> createOKByData(T data){
        return new Result<T>(ResponsCode.OK.getCode(),ResponsCode.OK.getMsg(),data);
    }
    // 创建失败
    public static<T> Result<T> createByUnprocesableEntity(){
        return new Result<T>(ResponsCode.Unprocesable_entity.getCode(),ResponsCode.Unprocesable_entity.getMsg());
    }
    // 创建重复
    public static<T> Result<T> createDuplicate(){
        return new Result<T>(ResponsCode.ISEXIST.getCode(),ResponsCode.ISEXIST.getMsg());
    }
    // 不存在账号
    public static<T> Result<T> createNotExist(){
        return new Result<T>(ResponsCode.NOT_EXIST.getCode(), ResponsCode.NOT_EXIST.getMsg());
    }
    // 密码错误
    public static<T> Result<T> createPasswordError(){
        return new Result<T>(ResponsCode.PASSWORDERROR.getCode(),ResponsCode.PASSWORDERROR.getMsg());
    }
    // 未授权
    public static <T> Result<T> createByUnAuthorize(){
        return new Result<T>(ResponsCode.UNAUTHORIZED.getCode(), ResponsCode.UNAUTHORIZED.getMsg());
    }
    // 上传失败
    public static <T> Result<T> createByUploadFail(){
        return new Result<T>(ResponsCode.UploadFaile.getCode(), ResponsCode.OK.getMsg());
    }
    // 删除资源识别
    public static <T> Result<T> createByDeleteFailed(){
        return new Result<T>(ResponsCode.DeleteFailed.getCode(),ResponsCode.DeleteFailed.getMsg());
    }

}
