package com.fontolan.calculator.infrastructure.external.randomorg;

import com.fontolan.calculator.infrastructure.external.randomorg.request.RandomOrgRequest;
import com.fontolan.calculator.infrastructure.external.randomorg.response.RandomOrgResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "randomOrgFeign",
        url = "${random-org.base-url}",
        configuration = RandomOrgFeignConfig.class
)
public interface RandomOrgFeign {
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    RandomOrgResponse invoke(@RequestBody RandomOrgRequest request);
}
