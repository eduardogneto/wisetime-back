package com.wisetime.wisetime.service.token;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.wisetime.wisetime.infra.security.TokenService;
import com.wisetime.wisetime.models.user.User;

@ExtendWith(MockitoExtension.class)
public class TokenServiceTest {

    private TokenService tokenService;
    private String secret;
    private User user;

    @BeforeEach
    public void setUp() {
        tokenService = new TokenService();
        secret = "testSecretKey";
        ReflectionTestUtils.setField(tokenService, "secret", secret);

        user = new User();
        user.setId(1L);
        user.setEmail("user@example.com");
    }

    @Test
    public void testGenerateToken_Success() {
        String token = tokenService.generateToken(user);
        assertNotNull(token);
        String subject = JWT.decode(token).getSubject();
        assertEquals(user.getEmail(), subject);
    }


    @Test
    public void testValidateToken_ValidToken() {
        String token = tokenService.generateToken(user);
        String subject = tokenService.validateToken(token);
        assertEquals(user.getEmail(), subject);
    }

    @Test
    public void testValidateToken_InvalidToken() {
        String invalidToken = "invalidTokenString";
        String subject = tokenService.validateToken(invalidToken);
        assertEquals("", subject);
    }

    @Test
    public void testValidateToken_ExpiredToken() {
        Instant pastTime = LocalDateTime.now().minusHours(3).toInstant(ZoneOffset.UTC);
        Algorithm algorithm = Algorithm.HMAC256(secret);
        String expiredToken = JWT.create()
                .withIssuer("auth-api")
                .withSubject(user.getEmail())
                .withExpiresAt(pastTime)
                .sign(algorithm);

        String subject = tokenService.validateToken(expiredToken);
        assertEquals("", subject);
    }

    @Test
    public void testValidateToken_JWTVerificationException() {
        String token = tokenService.generateToken(user);
        ReflectionTestUtils.setField(tokenService, "secret", "differentSecret");

        String subject = tokenService.validateToken(token);
        assertEquals("", subject);
    }
}
