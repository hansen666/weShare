package cn.compusshare.weshare.filter;


import cn.compusshare.weshare.constant.Common;
import cn.compusshare.weshare.repository.mapper.AdminMapper;
import cn.compusshare.weshare.service.LoginService;
import cn.compusshare.weshare.service.common.CacheService;
import cn.compusshare.weshare.utils.CommonUtil;
import cn.compusshare.weshare.utils.ResultUtil;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

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
    private LoginService loginService;

    @Autowired
    private CacheService cacheService;

    @Autowired
    private AdminMapper adminMapper;

    @Value("${adminTokenKey}")
    private String adminTokenKey;

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
        //如果是需要检查登录态的路径
        if (needCheck(request.getRequestURL().toString())) {
            logger.info("adminLoginFilter拦截：" + request.getRequestURL().toString());
            String token = request.getHeader("token");
            //token为空
            if (CommonUtil.isEmpty(token)) {
                logger.error(Common.TOKEN_NULL_MSG);
                output(response, Common.TOKEN_NULL, Common.TOKEN_NULL_MSG);
                return;
            }

            String account = request.getHeader("account");
            //account为空说明token无效
            if (CommonUtil.isEmpty(account)) {
                logger.error(Common.TOKEN_INVALID_MSG);
                output(response, Common.TOKEN_INVALID, Common.TOKEN_INVALID_MSG);
                return;
            }

            //从缓存中取sessionId
            String sessionId = (String) cacheService.get(token);
            //sessionId为空，说明token失效过期
            if (CommonUtil.isEmpty(sessionId)) {
                logger.error(Common.TOKEN_INVALID_MSG);
                output(response, Common.TOKEN_INVALID, Common.TOKEN_INVALID_MSG);
                return;
            }

            //当前请求的sessionId
            String currentSessionId = request.getSession().getId();
            if (! currentSessionId.equals(sessionId)) {
                //Id不相等则刷新缓存和数据库
                cacheService.set(token,currentSessionId);
//                String account = loginService.getIDFromToken(token, adminTokenKey, "account");
//                int result = adminMapper.updateSessionId(account);
//                logger.info("更新sessionId结果：{}",result);
            }

            //刷新token的过期时间
            cacheService.expire(token,overdueTime);
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
