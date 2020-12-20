package edu.myblog.utils;

public enum ResponsCode {
    OK(200,"请求成功"),
    CREATED(201,"创建成功"),
    DELETED(204,"删除成功"),
    BAD_REQUEST(400,"请求的地址不存在或者包含不支持的参数"),
    UNAUTHORIZED(401,"未授权"),
    FORBIDDEN(403,"被禁止访问"),
    NOT_FOUND(404,"请求的资源不存在"),
    Unprocesable_entity(422,"[POST/PUT/PATCH] 当创建一个对象时，发生一个验证错误"),
    INTERNAL_SERVER_ERROR(500,"内部错误"),
    ISEXIST(111,"账号已存在！"),
    NOT_EXIST(112,"账号不存在"),
    PASSWORDERROR(113,"密码错误"),
    UploadFaile(211,"上传失败"),
    DeleteFailed(212,"删除失败")
    ;
    private final int code;
    private final String msg;

    ResponsCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    public int getCode(){
        return code;
    }
    public String getMsg(){
        return msg;
    }
}
