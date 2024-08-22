package com.imooc.bilbil.api;

import com.imooc.bilbil.api.support.UserSupport;
import com.imooc.bilbil.domain.JsonResponse;
import com.imooc.bilbil.domain.PageResult;
import com.imooc.bilbil.domain.UserVideoHistory;
import com.imooc.bilbil.service.UserHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserHistoryApi {


    @Autowired
    private UserHistoryService userHistoryService;

    @Autowired
    private UserSupport userSupport;

    @GetMapping("/user-video-histories")
    public JsonResponse<PageResult<UserVideoHistory>> pagListUserVideoHistory(@RequestParam Integer size,
                                                                              @RequestParam Integer no){
        Long userId = userSupport.getCurrentUserId();
        PageResult<UserVideoHistory> result = userHistoryService.pagListUserVideoHistory(size,no,userId);
        return new JsonResponse<>(result);
    }

}