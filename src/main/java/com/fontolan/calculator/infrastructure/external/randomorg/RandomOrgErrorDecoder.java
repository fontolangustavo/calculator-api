package com.fontolan.calculator.infrastructure.external.randomorg;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fontolan.calculator.infrastructure.external.randomorg.exception.RandomOrgException;
import com.fontolan.calculator.infrastructure.external.randomorg.response.RandomOrgResponse;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;

public class RandomOrgErrorDecoder implements ErrorDecoder {
    private final ErrorDecoder defaultDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        try {
            String body = response.body() != null ? Util.toString(response.body().asReader(Util.UTF_8)) : null;
            if (body != null && !body.isBlank()) {
                RandomOrgResponse parsed = new ObjectMapper().readValue(body, RandomOrgResponse.class);
                if (parsed.getError() != null) {
                    return new RandomOrgException(
                            parsed.getError().getCode(),
                            parsed.getError().getMessage(),
                            response.status()
                    );
                }
            }
        } catch (Exception ignored) { }

        return defaultDecoder.decode(methodKey, response);
    }
}

