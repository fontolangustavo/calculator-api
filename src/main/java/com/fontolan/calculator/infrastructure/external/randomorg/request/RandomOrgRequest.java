package com.fontolan.calculator.infrastructure.external.randomorg.request;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RandomOrgRequest {
    private String jsonrpc = "2.0";
    private String method;
    private Params params;
    private Object id;

    public RandomOrgRequest(String method, Params params, Object id) {
        this.method = method;
        this.params = params;
        this.id = id;
    }

    public static class Params {
        private String apiKey;
        private int n;
        private int length;
        private String characters;
        private boolean replacement;

        public Params(String apiKey, int n, int length, String characters, boolean replacement) {
            this.apiKey = apiKey;
            this.n = n;
            this.length = length;
            this.characters = characters;
            this.replacement = replacement;
        }

        public String getApiKey() { return apiKey; }
        public int getN() { return n; }
        public int getLength() { return length; }
        public String getCharacters() { return characters; }
        public boolean isReplacement() { return replacement; }
    }

    public String getJsonrpc() { return jsonrpc; }
    public String getMethod() { return method; }
    public Params getParams() { return params; }
    public Object getId() { return id; }
}
