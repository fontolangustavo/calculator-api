package com.fontolan.calculator.unit.infrastructure.dataprovider.security;


import com.fontolan.calculator.infrastructure.dataprovider.security.JwtUtil;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JwtUtilTest {

    private static final String SECRET_64 =
            "0123456789abcdef0123456789abcdef0123456789abcdef0123456789abcdef";

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil(SECRET_64, 60_000L);
    }

    @Test
    void generateToken_shouldProduceValidToken() {
        String token = jwtUtil.generateToken("MOCK_USERNAME");
        assertThat(token).isNotBlank();
        assertThat(jwtUtil.isValid(token)).isTrue();
        assertThat(jwtUtil.extractUsername(token)).isEqualTo("MOCK_USERNAME");
    }

    @Test
    void isValid_shouldReturnFalseForNullOrMalformed() {
        assertThat(jwtUtil.isValid(null)).isFalse();
        assertThat(jwtUtil.isValid("abc.def.ghi")).isFalse();
        assertThat(jwtUtil.isValid("")).isFalse();
    }

    @Test
    void isValid_shouldReturnFalseForTamperedToken() {
        String token = jwtUtil.generateToken("user");
        String tampered = token + "x";
        assertThat(jwtUtil.isValid(tampered)).isFalse();
    }

    @Test
    void isValid_shouldReturnFalseWhenSignedWithDifferentKey() {
        JwtUtil other = new JwtUtil(SECRET_64.replace('0', '1'), 60_000L);
        String tokenFromOther = other.generateToken("MOCK_USERNAME");
        assertThat(jwtUtil.isValid(tokenFromOther)).isFalse();
    }

    @Test
    void extractUsername_shouldThrowForInvalidToken() {
        assertThatThrownBy(() -> jwtUtil.extractUsername("invalid.token"))
                .isInstanceOf(JwtException.class);
    }

    @Test
    void tokenShouldExpireAccordingToConfiguredTtl() throws InterruptedException {
        JwtUtil shortLived = new JwtUtil(SECRET_64, 1L);
        String token = shortLived.generateToken("ephemeral");
        Thread.sleep(10);
        assertThat(shortLived.isValid(token)).isFalse();
    }
}
