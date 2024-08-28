package com.xie.xvideo.api;

import com.xie.xvideo.api.support.UserSupport;
import com.xie.xvideo.domain.JsonResponse;
import com.xie.xvideo.domain.PageResult;
import com.xie.xvideo.domain.UserVideoHistory;
import com.xie.xvideo.service.UserHistoryService;
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