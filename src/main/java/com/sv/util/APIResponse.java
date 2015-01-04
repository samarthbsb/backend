package com.sv.util;

/**
 * @author vaibhav
 */
public class APIResponse<T> {
    private String status;
    private String responseCode;
    private String errorMsg;
    private T data;

    public APIResponse() {
    }

    public APIResponse(String status, String responseCode, String errorMsg, T data) {
        this.status = status;
        this.responseCode = responseCode;
        this.errorMsg = errorMsg;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public APIResponse setStatus(String status) {
        this.status = status;
        return this;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public APIResponse setResponseCode(String responseCode) {
        this.responseCode = responseCode;
        return this;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public APIResponse setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
        return this;
    }

    public T getData() {
        return data;
    }

    public APIResponse setData(T data) {
        this.data = data;
        return this;
    }

    public static <T> APIResponse<T> getSuccessResponse(T data) {
        return new APIResponse<T>("success", "0", null, data);
    }

    public static <T> APIResponse<T> getErrorResponse(String responseCode, String message, T data) {
        return new APIResponse<T>("failure", responseCode, message, data);
    }

    public boolean isSuccess() {
        return "success".equals(status);
    }

    public boolean isFailure() {
        return "failure".equals(status);
    }
}
