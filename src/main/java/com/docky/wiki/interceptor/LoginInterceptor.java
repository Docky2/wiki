package com.docky.wiki.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

;

/**
 * 拦截器：Spring框架特有的，常用于登录校验，权限校验，请求日志打印 /login
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {

    private static final Logger Log = LoggerFactory.getLogger(LoginInterceptor.class);

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 打印请求信息
        Log.info("------------- LoginInterceptor 开始 -------------");

        long startTime = System.currentTimeMillis();
        request.setAttribute("requestStartTime", startTime);

        // OPTIONS请求不做校验 跳过跨域检查
        // 前后端分离的架构，前端会发一个OPTIONS请求先做预检，对预检请求不做校验
        // 对于跨域访问设置拦截器时，需要对OPTION请求做相应的处理，否则对于非简单的请求会拦截访问
        if (request.getMethod().toUpperCase().equals("OPTIONS")) {
            return true;
        }
        String path = request.getRequestURL().toString();
        Log.info("接口登录拦截, path:{}", path);
        String token = request.getHeader("token");
        Log.info("登录校验开始，token:{}", token);

        if (token == null || token.isEmpty()) {
            Log.info("token为空，请求被拦截");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }
        Object object = redisTemplate.opsForValue().get(token);
        if (object == null) {
            Log.warn("token无效，请求被拦截");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        } else {
            Log.info("已登录：{}", object);
            // LoginUserContext.setUser(JSON.parseObject((String) object, UserLoginResp.class));
            return true;
        }

    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        long startTime = (Long) request.getAttribute("requestStartTime");
        Log.info("------------- LogInterceptor 结束 耗时：{} ms -------------", System.currentTimeMillis() - startTime);
    }
}
