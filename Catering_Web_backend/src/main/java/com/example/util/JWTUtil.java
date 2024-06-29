package com.example.util;

import com.example.dao.TokenDAO;
import com.example.dao.impl.TokenDAOImpl;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
//JWT令牌所有功能
public class JWTUtil {

    private static final String SECRET_KEY = "moligod"; // 定义秘钥

    /**
     * 生成JWT令牌
     * @param username 用户名
     * @return JWT令牌
     */
    public static String generateToken(String username) {
        long currentTimeMillis = System.currentTimeMillis();
        Date issuedAt = new Date(currentTimeMillis);
        Date expiration = new Date(currentTimeMillis + 1000 * 60 * 60 * 10); // 10小时有效期

        String header = Base64.getUrlEncoder().encodeToString("{\"alg\":\"HS256\",\"typ\":\"JWT\"}".getBytes());
        String payload = Base64.getUrlEncoder().encodeToString(("{\"sub\":\"" + username + "\",\"iat\":" + issuedAt.getTime() + ",\"exp\":" + expiration.getTime() + "}").getBytes());
        String signature = generateSignature(header, payload);

        return header + "." + payload + "." + signature;
    }

    /**
     * 验证JWT令牌
     * @param token JWT令牌
     * @return 是否有效
     */
    public static boolean validateToken(String token) {
        try {
            //把token的三部分分开
            String[] parts = token.split("\\.");
            if (parts.length == 3) {
                String header = parts[0];
                String payload = parts[1];
                String signature = parts[2];
                String expectedSignature = generateSignature(header, payload);
                //确保签名没被篡改，还有时间没有过期
                if (!expectedSignature.equals(signature) || isTokenExpired(payload)) {
                    return false;
                }
                // 解析token中的用户名，并且去数据库中查找用户名对应的token
                String username = getUsernameFromToken(token);
                String storedToken = TokenDAOImpl.getTokenByUsername(username);
                return token.equals(storedToken);
            }
            return false;
        } catch (Exception e) {
            System.out.println("Token validation exception: " + e.getMessage());
            e.printStackTrace(); // 打印详细的堆栈跟踪以帮助调试

            return false;
        }
    }

    /**
     * 从JWT令牌中获取用户名
     * @param token JWT令牌
     * @return 用户名
     */
    public static String getUsernameFromToken(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length == 3) {
                String payload = new String(Base64.getUrlDecoder().decode(parts[1]), StandardCharsets.UTF_8);
                Map<String, Object> claims = parsePayload(payload);
                return (String) claims.get("sub");
            }
            return null;
        } catch (Exception e) {
            System.out.println("Error getting username from token: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 生成JWT签名
     * @param header JWT头部
     * @param payload JWT载荷
     * @return JWT签名
     */
    private static String generateSignature(String header, String payload) {
        try {
            String data = header + "." + payload;
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest((data + SECRET_KEY).getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
        } catch (Exception e) {
            throw new RuntimeException("无法生成JWT签名", e);
        }
    }

    /**
     * 检查JWT令牌是否过期
     * @param payload JWT载荷
     * @return 是否过期
     */
    private static boolean isTokenExpired(String payload) {
        //转成json格式，否则无法解析JWT载荷
        String payloadjson = new String(Base64.getUrlDecoder().decode(payload), StandardCharsets.UTF_8);
        Map<String, Object> claims = parsePayload(payloadjson);
        Long exp = (Long) claims.get("exp");
        return exp != null && exp < System.currentTimeMillis();
    }

    /**
     * 解析JWT载荷
     * @param payload JWT载荷
     * @return 载荷内容的键值对
     * 载荷是JWT的主体部分，包含了声明信息:注册声明、公共声明、私有声明
     */
    private static Map<String, Object> parsePayload(String payload) {
        Map<String, Object> claims = new HashMap<>();
        try {
            // 使用正则表达式匹配键值对
            String[] entries = payload.replace("{", "").replace("}", "").split(",");
            for (String entry : entries) {
                String[] keyValue = entry.split(":");
                if (keyValue.length == 2) { // 确保键值对的格式正确
                    claims.put(keyValue[0].replace("\"", "").trim(), parseValue(keyValue[1].replace("\"", "").trim()));
                } else {
                    System.out.println("Invalid key-value pair: " + entry);
                }
            }
        } catch (Exception e) {
            System.out.println("错误解析负载: " + e.getMessage());
            e.printStackTrace();
        }
        return claims;
    }


    /**
     * 解析载荷值
     * @param value 载荷值
     * @return 解析后的值
     */
    private static Object parseValue(String value) {
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            return value;
        }
    }
}
