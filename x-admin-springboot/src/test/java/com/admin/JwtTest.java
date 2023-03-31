package com.admin;

import com.admin.common.utils.JwtUtil;
import com.admin.sys.entity.User;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class JwtTest {
    @Autowired
    private JwtUtil jwtUtil;

    @Test
    public void testCreateJWT() {
        User user = new User();
        user.setUsername("123");
        user.setPhone("13211111111");
        String token = jwtUtil.createToken(user);
        System.out.println(token);
    }

    @Test
    public void testParseJWT() {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIyODBjZWViZC1jZjA4LTQzYjktOTQ0My02ZjdjZGZmMDkyODUiLCJzdWIiOiJ7XCJwaG9uZVwiOlwiMTMyMTExMTExMTFcIixcInVzZXJuYW1lXCI6XCIxMjNcIn0iLCJpc3MiOiJzeXN0ZW0iLCJpYXQiOjE2Nzk3NDg3MjYsImV4cCI6MTY3OTc1MDUyNn0.igA6xQQXYvhXGzEK3L7C8oOo8pxvGz1BVhwesKVUkdc";
        Claims claims = jwtUtil.parseToken(token);
        System.out.println(claims);
    }

    @Test
    public void testParseJWTCastClass() {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIyODBjZWViZC1jZjA4LTQzYjktOTQ0My02ZjdjZGZmMDkyODUiLCJzdWIiOiJ7XCJwaG9uZVwiOlwiMTMyMTExMTExMTFcIixcInVzZXJuYW1lXCI6XCIxMjNcIn0iLCJpc3MiOiJzeXN0ZW0iLCJpYXQiOjE2Nzk3NDg3MjYsImV4cCI6MTY3OTc1MDUyNn0.igA6xQQXYvhXGzEK3L7C8oOo8pxvGz1BVhwesKVUkdc";
        User user = jwtUtil.parseToken(token, User.class);
        System.out.println(user);
    }
}
