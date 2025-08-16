package com.fontolan.calculator.infrastructure.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fontolan.calculator.infrastructure.exception.ApiErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Component
public class JsonAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper mapper;

    public JsonAuthenticationEntryPoint(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void commence(HttpServletRequest req, HttpServletResponse res, AuthenticationException ex) {
        try {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            res.setContentType(MediaType.APPLICATION_JSON_VALUE);
            res.setCharacterEncoding(StandardCharsets.UTF_8.name());

            var body = new ApiErrorResponse(
                    401,
                    "Missing or invalid token",
                    Map.of(
                            "path", req.getRequestURI()
                    )
            );

            mapper.writeValue(res.getWriter(), body);
        } catch (Exception ignored) {}
    }
}
