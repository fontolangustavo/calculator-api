package com.fontolan.calculator.infrastructure.external.randomorg;

import com.fontolan.calculator.infrastructure.dataprovider.RandomStringDataProvider;
import com.fontolan.calculator.infrastructure.external.randomorg.exception.RandomOrgException;
import com.fontolan.calculator.infrastructure.external.randomorg.request.RandomOrgRequest;
import com.fontolan.calculator.infrastructure.external.randomorg.response.RandomOrgResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RandomOrgRandomStringAdapter implements RandomStringDataProvider {

    private final RandomOrgFeign feign;
    private final String apiKey;

    private static final String DEFAULT_METHOD = "generateStrings";
    private static final String DEFAULT_CHARS =
            "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int DEFAULT_QUANTITY_STRINGS = 1;

    public RandomOrgRandomStringAdapter(
            RandomOrgFeign feign,
            @Value("${random-org.api-key}") String apiKey
    ) {
        this.feign = feign;
        this.apiKey = apiKey;
    }

    @Override
    public String getRandomString(Integer length) {
        var params = new RandomOrgRequest.Params(
                apiKey,
                DEFAULT_QUANTITY_STRINGS,
                length,
                DEFAULT_CHARS,
                true
        );
        var request = new RandomOrgRequest(DEFAULT_METHOD, params, 1);

        RandomOrgResponse response = feign.invoke(request);

        if (response.getError() != null) {
            throw new RandomOrgException(response.getError().getCode(),
                    response.getError().getMessage(), 502);
        }

        if (response.getResult() == null
                || response.getResult().getRandom() == null
                || response.getResult().getRandom().getData() == null) {
            throw new IllegalStateException("Invalid response from Random.org");
        }

        List<String> data = response.getResult().getRandom().getData();
        if (data.isEmpty()) {
            throw new IllegalStateException("Random.org did not return any strings");
        }

        return data.get(0);
    }
}
