package com.fontolan.calculator.infrastructure.external.randomorg.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RandomOrgResponse {
    private String jsonrpc;
    private Object id;
    private Result result;
    private Error error;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Result {
        private Random random;
        private Integer bitsUsed;
        private Integer bitsLeft;
        private Integer requestsLeft;
        private Integer advisoryDelay;

        public Random getRandom() { return random; }
        public Integer getBitsUsed() { return bitsUsed; }
        public Integer getBitsLeft() { return bitsLeft; }
        public Integer getRequestsLeft() { return requestsLeft; }
        public Integer getAdvisoryDelay() { return advisoryDelay; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Random {
        private List<String> data;
        private String completionTime;

        public List<String> getData() { return data; }
        public String getCompletionTime() { return completionTime; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Error {
        private Integer code;
        private String message;

        public Integer getCode() { return code; }
        public String getMessage() { return message; }
    }

    public String getJsonrpc() { return jsonrpc; }
    public Object getId() { return id; }
    public Result getResult() { return result; }
    public Error getError() { return error; }
}
