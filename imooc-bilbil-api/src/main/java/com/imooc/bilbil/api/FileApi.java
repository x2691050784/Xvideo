package com.imooc.bilbil.api;


import com.fasterxml.jackson.databind.ser.std.FileSerializer;
import com.imooc.bilbil.domain.JsonResponse;
import com.imooc.bilbil.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class FileApi {
    @Autowired
    private FileService fileService;
    @PutMapping("/file-slices")
    public JsonResponse<String> uploadFileBySlices(MultipartFile slice,String fileMD5,Integer  fileNo,Integer totalSliceNo) throws IOException {
        String filePath=fileService.uploadFileBySlices(slice, fileMD5,fileNo,totalSliceNo);
        return new JsonResponse<>(filePath);
    }


}
