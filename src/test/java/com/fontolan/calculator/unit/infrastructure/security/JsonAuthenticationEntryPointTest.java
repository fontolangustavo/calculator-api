package com.fontolan.calculator.unit.infrastructure.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fontolan.calculator.infrastructure.exception.ApiErrorResponse;
import com.fontolan.calculator.infrastructure.security.JsonAuthenticationEntryPoint;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class JsonAuthenticationEntryPointTest {

    private ObjectMapper mapper;
    private HttpServletRequest req;
    private HttpServletResponse res;
    private JsonAuthenticationEntryPoint entryPoint;

    @BeforeEach
    void setUp() throws Exception {
        mapper = mock(ObjectMapper.class);
        req = mock(HttpServletRequest.class);
        res = mock(HttpServletResponse.class);
        when(res.getWriter()).thenReturn(new PrintWriter(new StringWriter()));
        entryPoint = new JsonAuthenticationEntryPoint(mapper);
    }

    @Test
    void shouldSetStatusHeadersAndWriteBody() throws Exception {
        when(req.getRequestURI()).thenReturn("/api/secure");
        entryPoint.commence(req, res, new AuthenticationException("unauthorized") {});
        verify(res).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(res).setContentType(MediaType.APPLICATION_JSON_VALUE);
        verify(res).setCharacterEncoding(StandardCharsets.UTF_8.name());
        ApiErrorResponse expected = new ApiErrorResponse(401, "Missing or invalid token", Map.of("path", "/api/secure"));
        verify(mapper).writeValue(any(java.io.Writer.class), eq(expected));
    }

    @Test
    void shouldSwallowExceptionsFromMapper() throws Exception {
        when(req.getRequestURI()).thenReturn("/x");
        doThrow(new IOException("boom")).when(mapper).writeValue(any(java.io.Writer.class), any());
        assertThatCode(() -> entryPoint.commence(req, res, new AuthenticationException("unauthorized") {}))
                .doesNotThrowAnyException();
    }

    @Test
    void shouldProduceValidJsonWithRealMapper() throws Exception {
        ObjectMapper real = new ObjectMapper();
        JsonAuthenticationEntryPoint realEntry = new JsonAuthenticationEntryPoint(real);
        HttpServletRequest req2 = mock(HttpServletRequest.class);
        HttpServletResponse res2 = mock(HttpServletResponse.class);
        when(req2.getRequestURI()).thenReturn("/secure/resource");
        StringWriter sw = new StringWriter();
        when(res2.getWriter()).thenReturn(new PrintWriter(sw, true));
        realEntry.commence(req2, res2, new AuthenticationException("nope") {});
        verify(res2).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(res2).setContentType(MediaType.APPLICATION_JSON_VALUE);
        verify(res2).setCharacterEncoding(StandardCharsets.UTF_8.name());
        String json = sw.toString();
        assertThat(json).contains("\"status\":401");
        assertThat(json).contains("\"message\":\"Missing or invalid token\"");
        assertThat(json).contains("\"errors\":{\"path\":\"/secure/resource\"}");
    }
}
