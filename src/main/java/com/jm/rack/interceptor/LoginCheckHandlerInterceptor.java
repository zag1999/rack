package com.jm.rack.interceptor;

import com.jm.rack.untils.CommonUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginCheckHandlerInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String userId = request.getHeader("userId");
        System.out.println(request.getServletPath());
        String path = request.getServletPath();
        if (!CommonUtils.getInstance().IsStringEmpty(path) && ((path.contains("rackuser/login")|| path.contains("rackuser/insert"))))
        {
            return true;
        }else {
            if (CommonUtils.getInstance().IsStringEmpty(userId)) {
                response.setStatus(401);
                response.setHeader("Content-Type", "text/html;charset=UTF-8");
                response.getWriter().print("温馨提示无用户信息，请登录");
                response.getWriter().flush();
                return false;
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

}
