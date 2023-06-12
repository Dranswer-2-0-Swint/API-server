package com.t3q.dranswer.dto;

public class RequestContext {
	
    private static final ThreadLocal<RequestContextData> threadLocalData = new ThreadLocal<>();

    public static void setContextData(String request_id, String access_token) {
        RequestContextData data = new RequestContextData(request_id, access_token);
        threadLocalData.set(data);
    }

    public static RequestContextData getContextData() {
        return threadLocalData.get();
    }

    public static void clear() {
        threadLocalData.remove();
    }

    public static class RequestContextData {
        private final String request_id;
        private final String access_token;

        public RequestContextData(String request_id, String access_token) {
            this.request_id = request_id;
            this.access_token = access_token;
        }

        public String getRequestId() {
            return request_id;
        }

        public String getAccessToken() {
            return access_token;
        }
    }
}
