package com.yx.entity;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RestResult {

    private int errCode;
    private String errMsg;
    private Object data;
    private String remake;

    public RestResult() {
        this.setResult(ErrCode.Success);
    }

    public RestResult(int errCode, String errMsg,String remake) {
        this.errCode = errCode;
        this.errMsg = errMsg;
        this.errMsg = remake;
        this.data = null;
    }

    public RestResult(ErrCode errCode){
        this.errCode = errCode.getErrCode();
        this.errMsg = errCode.toString();
        this.data = null;
    }

    public void setResult(ErrCode errCode){
        this.errCode = errCode.getErrCode();
        this.errMsg = errCode.toString();
        this.data = null;
    }

    public static RestResult success(Object data){
        RestResult restResult = new RestResult();
        restResult.setData(data);
        return restResult;
    }

    public static RestResult success(){
        RestResult restResult = new RestResult();
        return restResult;
    }

    public static RestResult error(){
        return new RestResult(ErrCode.failed);
    }

    public static RestResult error(ErrCode errCode){
        return new RestResult(errCode);
    }

    public static RestResult error(String errMsg){
        RestResult restResult = new RestResult(ErrCode.failed);
        restResult.setErrMsg(errMsg);
        return restResult;
    }
}
