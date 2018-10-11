package com.iss.users.model;

public class RespResult<T> {

    private String statuscode;

    private String message;

    private T data;

    public RespResult() {
    }

    public RespResult(String statuscode, String message, T data) {
        this.statuscode = statuscode;
        this.message = message;
        this.data = data;
    }

    public String getStatuscode() {
        return statuscode;
    }

    public void setStatuscode(String statuscode) {
        this.statuscode = statuscode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
