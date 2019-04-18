package cn.compusshare.weshare.filter;


import cn.compusshare.weshare.constant.Common;
import cn.compusshare.weshare.service.common.CacheService;
import cn.compusshare.weshare.utils.CommonUtil;
import cn.compusshare.weshare.utils.ResultUtil;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @Author: LZing
 * @Date: 2019/4/11
 */
@WebFilter(urlPatterns = "/*", filterName = "AdminLogin")
public class AdminLoginFilter implements Filter {

    private final static Logger logger = LoggerFactory.getLogger(AdminLoginFilter.class);


    @Autowired
    private CacheService cacheService;


    @Autowired
    private Environment environment;


    @Value("${overdueTime}")
    private long overdueTime;

    //需要拦截的路径
    private String[] needCheckPathPrefix = {
            "/admin",
    };


    @Override
    public void init(FilterConfig filterConfig) {
        logger.info("初始化adminLoginFilter");
    }


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        //解决跨域的问题
        response.setHeader("Access-Control-Allow-Origin","*");
        response.setHeader("Access-Control-Allow-Credentials","true");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type,Content-Length, Authorization, Accept,X-Requested-With,X-App-Id, X-Token");
        response.setHeader("Access-Control-Allow-Methods","PUT,POST,GET,DELETE,OPTIONS");
        response.setHeader("Access-Control-Max-Age", "3600");


        //如果是需要检查登录态的路径
        if (needCheck(request.getRequestURL().toString())) {
            logger.info("adminLoginFilter拦截：" + request.getRequestURL().toString());
            String currentToken = request.getHeader("token");
            String account = request.getHeader("account");
            //token或account为空
            if (CommonUtil.isEmpty(currentToken) || CommonUtil.isEmpty(account)) {
                logger.info(Common.TOKEN_OR_ACCOUNT_EMPTY_MSG);
                output(response, Common.TOKEN_OR_ACCOUNT_EMPTY, Common.TOKEN_OR_ACCOUNT_EMPTY_MSG);
                return;
            }

            String token = cacheService.getString(account);
            //token无效或account错误
            if (CommonUtil.isEmpty(token) || !currentToken.equals(token)) {
                logger.info(Common.TOKEN_INVALID_OR_ACCOUNT_ERROR_MSG);
                output(response, Common.TOKEN_INVALID_OR_ACCOUNT_ERROR, Common.TOKEN_INVALID_OR_ACCOUNT_ERROR_MSG);
                return;
            }


            //刷新token的过期时间
            boolean result = cacheService.expire(account,Long.valueOf(environment.getProperty("overdueTime")));
            logger.info("token过期时间刷新：{}", result);
        }
        //下游过滤链
        filterChain.doFilter(request, response);
    }


    /**
     *  返回json数据
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
            logger.error("adminLoginFilter error", e);
            return;
        } finally {
            if (writer != null)
                writer.close();
        }
    }

    /**
     * 检查路径是否需要过滤
     * @param path
     * @return
     */
    private boolean needCheck(String path) {
        for (String prefix : needCheckPathPrefix) {
            if (path.contains(prefix) && (! path.contains("login"))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void destroy() {
        logger.info("adminLoginFilter destroy");
    }
}
