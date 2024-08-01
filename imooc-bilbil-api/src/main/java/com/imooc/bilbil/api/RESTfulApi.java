package com.imooc.bilbil.api;

import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestController
public class RESTfulApi {
private final Map<Integer,Map<String,Object>> dataMap;

public RESTfulApi(){
dataMap=new HashMap<>();
    for (int i = 1; i < 3; i++) {
        Map<String,Object> data =new HashMap<>();
        data.put("id",i);
        data.put("name","name"+i);
        dataMap.put(i,data);
    }
}
@GetMapping("/objects/{id}")
    public Map<String,Object> getData(@PathVariable Integer id){
    return dataMap.get(id);
}
@DeleteMapping("/objects/{id}")
    public String deleteData(@PathVariable Integer id){
    dataMap.remove(id);
    return "删除成功";
}
@PostMapping("/objects")
    public String postData(@RequestBody Map<String,Object> data){
    Integer[] idArray = dataMap.keySet().toArray(new Integer[0]);//获取所有的key值并且返回成数组
    //排序
    Arrays.sort(idArray);
    //找出最大的id
    int nextId=idArray[idArray.length-1]+1;
    dataMap.put(nextId,data);
    return "POST 成功";
}

//put 先判断数组中是否有id，如果有则更新，如果没有则新增
@PutMapping("/objects")
    public String putData(@RequestBody Map<String,Object> data){
    Integer id=Integer.valueOf(String.valueOf(data.get("id")));
    //检查datamap有没有对应的id
    Map<String,Object> con=dataMap.get(id);
    if(con==null){
        Integer[] idArray = dataMap.keySet().toArray(new Integer[0]);//获取所有的key值并且返回成数组
        //排序
        Arrays.sort(idArray);
        //找出最大的id
        int nextId=idArray[idArray.length-1]+1;
        dataMap.put(nextId,data);
    }else{
        dataMap.put(id,data);
    }
    return "put 成功";
}


}
