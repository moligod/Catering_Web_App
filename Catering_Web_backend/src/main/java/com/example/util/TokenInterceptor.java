package com.example.util;

import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.net.HttpCookie;
import java.util.List;
//继承Filter代表过滤器类
public class TokenInterceptor extends Filter {

    //接收HttpExchange对象和过滤器链Chain对象，并抛出IOException异常
    @Override
    public void doFilter(HttpExchange exchange, Chain chain) throws IOException {

        // 获取请求的URI路径并将其存储在path变量中
        String path = exchange.getRequestURI().getPath();

        // 从请求头中获取Cookie字段
        List<String> cookieHeaders = exchange.getRequestHeaders().get("Cookie");

        String token = null;

        // 检查请求头是否存在且不为空
        if (cookieHeaders != null && !cookieHeaders.isEmpty()) {
            // 获取Cookie头里面的token
            for (String cookieHeader : cookieHeaders) {
                List<HttpCookie> cookies = HttpCookie.parse(cookieHeader);
                for (HttpCookie cookie : cookies) {
                    if ("jwtToken".equals(cookie.getName())) {
                        token = cookie.getValue();
                        break;
                    }
                }
            }
        }

        // 验证JWT令牌
        if (token != null && JWTUtil.validateToken(token)) {
            // 如果令牌有效，继续执行过滤器链中的下一个过滤器或请求处理器
            chain.doFilter(exchange);
            return;
        }
        // 对于登录和注册路径，如果没有令牌或令牌无效，则允许请求通过，进行注册或登录
        if ("/login".equals(path) || "/register".equals(path)) {
            chain.doFilter(exchange);
            return;
        }
        // 如果验证失败，重定向到 /login 页面
        ResponseSender.sendResponse(exchange, 401, "该地址未授权，请登录账号");
    }

    @Override
    public String description() {
        return "JWT令牌验证过滤器";
    }

}
