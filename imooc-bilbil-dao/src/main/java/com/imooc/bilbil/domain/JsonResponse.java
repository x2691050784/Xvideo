package com.imooc.bilbil.domain;

public class JsonResponse<T> {//泛型类
    private  String code ;//返回的状态码
    private  String msg;//返回提示语
    private T data;//返回类型

    //构造器方法
    public JsonResponse(String code,String msg){
        this.code=code;
        this.msg=msg;
    }
    public JsonResponse(T data){
        this.data=data;
        msg="成功";
        code="0";
    }

    //不需要返回前端数据,请求成功
    public static JsonResponse<String> success(){
        return new JsonResponse<String>(null);
    }

    //用户登入以后获取一个令牌传递过来返回前端
    public static JsonResponse<String> success(String data){
        return new JsonResponse<>(data);
    }

    //失败类型
    public static JsonResponse<String> fail(){
        return new JsonResponse<>("1","失败");
    }

    //返回特定的状态吗与信息---在项目中前端可以根据特定的状态吗与信息,定制化提示
    public static JsonResponse<String> fail(String code,String msg){
        return new JsonResponse<>(code,msg);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
