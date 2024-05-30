package com.example.util;

import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.net.URI;
import java.sql.SQLOutput;
import java.util.List;
//继承Filter代表过滤器类
public class TokenInterceptor extends Filter {

    //接收HttpExchange对象和过滤器链Chain对象，并抛出IOException异常
    @Override
    public void doFilter(HttpExchange exchange, Chain chain) throws IOException {
        //获取请求的URI路径并将其存储在path变量中。
        String path = exchange.getRequestURI().getPath();
        // 从请求头中获取Authorization(请求头)字段
        List<String> authHeaders = exchange.getRequestHeaders().get("Authorization");

        //检查请求头是否存在且不为空
        if (authHeaders != null && !authHeaders.isEmpty()) {
            //获取Authorization头里面的的token，并移除Bearer 前缀，将剩余部分作为令牌存储在token变量中。
            String token = authHeaders.get(0).replace("Bearer ", "");
            // 验证JWT令牌，不有效直接跳过
            if (JWTUtil.validateToken(token)) {
                // 如果令牌有效，继续执行过滤器链中的下一个过滤器或请求处理器
                chain.doFilter(exchange);
                //退出当前方法，代表令牌验证成功
                return;
            }
        }
        // 对于登录和注册路径，如果没有令牌或令牌无效，则允许请求通过，进行注册或登录
        if ("/login".equals(path) || "/register".equals(path)) {
            chain.doFilter(exchange);
            return;
        }

        // 如果验证失败，重定向到 /login 页面
        ResponseSender.sendResponse(exchange, 401, "该地址未授权，请登录账号");
        ResponseSender.sendRedirect(exchange, "/login");
//        exchange.getResponseHeaders().set("Location", "/login");
//        exchange.sendResponseHeaders(302, -1); // 302状态码用于重定向
//        exchange.close();
//      如果验证失败，返回401未授权响应，或者重定向到登录界面（有点小问题，会覆盖中间件的发送信息）
//      String response = "请登录账号后再进行访问";
//      exchange.sendResponseHeaders(401, response.getBytes().length);
//      exchange.getResponseBody().write(response.getBytes());
//      exchange.close();
    }

    @Override
    public String description() {
        return "JWT令牌验证过滤器";
    }

//    private void redirectToLogin(HttpExchange exchange) throws IOException {
//        // 重定向到登录页面
//        URI loginUri = URI.create("/login");
//        exchange.getResponseHeaders().set("Location", loginUri.toString());
//        exchange.sendResponseHeaders(302, -1); // 302重定向状态码，无响应体
//        exchange.close();
//    }
}
