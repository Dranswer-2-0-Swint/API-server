package com.t3q.dranswer.dto;

import lombok.Getter;

public class RequestContext {
	
    private static final ThreadLocal<RequestContextData> threadLocalData = new ThreadLocal<>();

    public static void setContextData(String requestId, String accessToken, String requestUser) {
        RequestContextData data = new RequestContextData(requestId, accessToken, requestUser);
        threadLocalData.set(data);
    }

    public static RequestContextData getContextData() {
        return threadLocalData.get();
    }

    public static void clear() {
        threadLocalData.remove();
    }

    @Getter
    public static class RequestContextData {
        private final String requestId;
        private final String requestUser;
        private final String accessToken;

        public RequestContextData(String requestId, String accessToken,String requestUser) {
            this.requestId = requestId;
            this.requestUser = requestUser;
            this.accessToken = accessToken;
        }
    }
}
