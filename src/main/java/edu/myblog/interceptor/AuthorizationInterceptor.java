package edu.myblog.interceptor;

import ch.qos.logback.classic.Logger;
import com.alibaba.fastjson.JSON;
import com.auth0.jwt.exceptions.JWTVerificationException;
import edu.myblog.controller.UserController;
import edu.myblog.utils.Result;
import edu.myblog.utils.TokenGenerate;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class AuthorizationInterceptor implements HandlerInterceptor {
    // 忽略的请求
    private static final String[] IGNORE_URI ={"/login","/register","/upload","/deletePic","/blog","/wszan","/userfancy","/updateName"
            ,"/getoneuser","/guanzhu","/quguan","/search","/hot","/backUser","/userstatus"
            ,"/deletuser","/adminlist","/adminstatus","/deletadmin","/addAdmin","/getoneadmin",
    "/uadminpassword"};
    private static Logger logger = (Logger) LoggerFactory.getLogger(AuthorizationInterceptor.class);

    /**
     * 1.验证URL是否需要进行登录的
     * 2.验证token是否为空
     *      3.验证token是否合法
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.info("AuthorizationInterceptor preHandle --> ");
        boolean flag = false;
        String token = null;
        String servletPath = request.getServletPath();
        logger.info(String.format("servletPath ->> %s",servletPath));
        for(String s:IGNORE_URI){
            if(servletPath.contains(s)){
                flag = true;
                break;
            };

        }
        if(!flag) {

            token = request.getHeader("authorization");
            if(token != null){
                // 验证token
                if(TokenGenerate.checkToken(token)){
                    logger.info("校验token");
                    return true;

                }
            }
            // token为空或未通过验证则返回401
            sendJsonMessage(response,Result.createByUnAuthorize());
        }
        return flag;
    }
//  手动发送Json请求
    private void sendJsonMessage(HttpServletResponse response,Object obj) throws Exception{
        response.setContentType("application/json;charset=utf-8");
        PrintWriter writer = response.getWriter();
        writer.print(JSON.toJSON(obj));
        writer.close();
        response.flushBuffer();
    }
}
