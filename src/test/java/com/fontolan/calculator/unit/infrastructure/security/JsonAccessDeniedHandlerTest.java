package com.fontolan.calculator.unit.infrastructure.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fontolan.calculator.infrastructure.exception.ApiErrorResponse;
import com.fontolan.calculator.infrastructure.security.JsonAccessDeniedHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;

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

class JsonAccessDeniedHandlerTest {

    private ObjectMapper mapper;
    private HttpServletRequest req;
    private HttpServletResponse res;
    private JsonAccessDeniedHandler handler;

    @BeforeEach
    void setUp() throws Exception {
        mapper = mock(ObjectMapper.class);
        req = mock(HttpServletRequest.class);
        res = mock(HttpServletResponse.class);
        when(res.getWriter()).thenReturn(new PrintWriter(new StringWriter()));
        handler = new JsonAccessDeniedHandler(mapper);
    }

    @Test
    void shouldSetStatusHeadersAndWriteBody() throws Exception {
        when(req.getRequestURI()).thenReturn("/secure/area");

        handler.handle(req, res, new AccessDeniedException("denied"));

        verify(res).setStatus(HttpServletResponse.SC_FORBIDDEN);
        verify(res).setContentType(MediaType.APPLICATION_JSON_VALUE);
        verify(res).setCharacterEncoding(StandardCharsets.UTF_8.name());
        ApiErrorResponse expected = new ApiErrorResponse(403, "Access denied", Map.of("path", "/secure/area"));
        verify(mapper).writeValue(any(java.io.Writer.class), eq(expected));
    }

    @Test
    void shouldSwallowExceptionsFromMapper() throws Exception {
        when(req.getRequestURI()).thenReturn("/x");
        doThrow(new IOException("boom")).when(mapper).writeValue(any(java.io.Writer.class), any());

        assertThatCode(() -> handler.handle(req, res, new AccessDeniedException("denied")))
                .doesNotThrowAnyException();
    }

    @Test
    void shouldProduceValidJsonWithRealMapper() throws Exception {
        ObjectMapper real = new ObjectMapper();
        JsonAccessDeniedHandler realHandler = new JsonAccessDeniedHandler(real);

        HttpServletRequest req2 = mock(HttpServletRequest.class);
        HttpServletResponse res2 = mock(HttpServletResponse.class);
        when(req2.getRequestURI()).thenReturn("/api/path");
        StringWriter sw = new StringWriter();
        when(res2.getWriter()).thenReturn(new PrintWriter(sw, true));

        realHandler.handle(req2, res2, new AccessDeniedException("nope"));

        verify(res2).setStatus(HttpServletResponse.SC_FORBIDDEN);
        verify(res2).setContentType(MediaType.APPLICATION_JSON_VALUE);
        verify(res2).setCharacterEncoding(StandardCharsets.UTF_8.name());

        String json = sw.toString();
        assertThat(json).contains("\"status\":403");
        assertThat(json).contains("\"message\":\"Access denied\"");
        assertThat(json).contains("\"errors\":{\"path\":\"/api/path\"}");
    }
}
