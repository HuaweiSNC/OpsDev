package com.huawei.network.opsdev.core.inspectview.bean;

import java.text.SimpleDateFormat;


public class HttpServiceRequestBean {
    private String url;
    private String requestTime;
    private String responseTime;
    private Long elapsed;
    private String operation;
    private String response;
    private String request;
    private Long requestTimeLong;
    private Long responseTimeLong;
    
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getRequestTime() {
        return requestTime;
    }
    public void setRequestTime() {
        SimpleDateFormat df3 = new SimpleDateFormat("HH:mm:ss");
        requestTimeLong = System.currentTimeMillis();
        this.requestTime = df3.format(requestTimeLong);
        
    }
    public String getResponseTime() {
        return responseTime;
    }
    public void setResponseTime() {
        SimpleDateFormat df3 = new SimpleDateFormat("HH:mm:ss");
        responseTimeLong = System.currentTimeMillis();
        this.responseTime = df3.format(responseTimeLong);
    }
    public Long getElapsed() {
        return elapsed;
    }
    public void setElapsed() {
        elapsed = responseTimeLong - requestTimeLong;
    }
    public String getOperation() {
        return operation;
    }
    public void setOperation(String operation) {
        this.operation = operation;
    }
    public String getResponse() {
        return response;
    }
    public void setResponse(String response) {
        this.response = response;
    }
    public String getRequest() {
        return request;
    }
    public void setRequest(String request) {
        this.request = request;
    }
    
    
    
}
