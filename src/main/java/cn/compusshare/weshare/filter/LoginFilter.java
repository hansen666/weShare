package cn.compusshare.weshare.filter;

import cn.compusshare.weshare.constant.Common;
import cn.compusshare.weshare.service.LoginService;
import cn.compusshare.weshare.utils.CommonUtil;
import cn.compusshare.weshare.utils.ResultUtil;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @Author: LZing
 * @Date: 2019/3/11
 * 登录过滤器
 */
@WebFilter(urlPatterns = "/*", filterName = "login")
public class LoginFilter implements Filter {

    private final static Logger logger = LoggerFactory.getLogger(Logger.class);

    //不需要拦截的路径
    private String[] unNeedCheckPathPrefix = {
            "/login",
            "/test/",
            "/customerService"
    };

    @Autowired
    private LoginService loginService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("初始化loginFilter");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        //如果是需要检查登录态的路径
        if (needCheck(request.getRequestURL().toString())) {
            logger.info("loginFilter拦截：" + request.getRequestURL().toString());
            String token = request.getHeader("token");
            //token为空
            if (CommonUtil.isEmpty(token)) {
                logger.error(Common.TOKEN_NULL_MSG);
                output(response, Common.TOKEN_NULL, Common.TOKEN_NULL_MSG);
                return;
            }
            String openID = loginService.getOpenIDFromToken(token);
            //openID为空，说明token失效
            if (CommonUtil.isEmpty(openID)) {
                logger.error(Common.TOKEN_INVALID_MSG);
                output(response, Common.TOKEN_INVALID, Common.TOKEN_INVALID_MSG);
                return;
            }
        }
        //下游过滤链
        filterChain.doFilter(request, response);
    }

    /**
     * 检查该路径是否需要通过过滤器
     *
     * @param path
     * @return
     */
    private boolean needCheck(String path) {
        for (String prefix : unNeedCheckPathPrefix) {
            if (path.contains(prefix)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 返回json数据
     *
     * @param response
     * @param code
     * @param msg
     */
    private void output(HttpServletResponse response, int code, String msg) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=utf-8");
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            writer.print(JSONObject.toJSON(ResultUtil.fail(code, msg)));
            return;

        } catch (IOException e) {
            logger.error("response error", e);
            return;
        } finally {
            if (writer != null)
                writer.close();
        }
    }

    @Override
    public void destroy() {
    }
}
