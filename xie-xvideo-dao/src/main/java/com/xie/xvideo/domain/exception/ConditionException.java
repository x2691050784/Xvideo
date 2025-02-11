package com.xie.xvideo.domain.exception;
//条件异常
public class ConditionException extends RuntimeException{
    private static final long serialVersionUID=1L;//序列号
    private String code ;
    public ConditionException(String code,String name){
        super(name);
        this.code=code;
    }
    public ConditionException(String name){
        super(name);
        code="500";//常规的错误
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
