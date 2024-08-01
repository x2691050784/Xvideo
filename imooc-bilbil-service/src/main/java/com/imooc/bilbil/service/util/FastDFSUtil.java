package com.imooc.bilbil.service.util;

import com.github.tobato.fastdfs.domain.fdfs.FileInfo;
import com.github.tobato.fastdfs.domain.fdfs.MetaData;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.AppendFileStorageClient;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.imooc.bilbil.dao.FileDao;
import com.imooc.bilbil.domain.exception.ConditionException;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.*;
import javax.websocket.HandshakeResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.ConnectException;
import java.util.*;

@Component
public class FastDFSUtil {

    @Autowired
    private FastFileStorageClient fastFileStorageClient;
    @Autowired
    private AppendFileStorageClient appendFileStorageClient;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    @Autowired
    private FileDao fileDao;
    private static final  String DEFAULT_GROUP="group1";

    //上传
    public String getFileType(MultipartFile file){
        if(file==null){
            throw new ConditionException("非法文件");
        }
        String fileName =file.getOriginalFilename();
        int index=fileName.lastIndexOf(".");
        return fileName.substring(index+1);
    }
    //上传可以断点续传
    public String uploadAppenderFile(MultipartFile file) throws IOException {
        String fileName=file.getName();
        //计算类型
        String fileType=this.getFileType(file);
        StorePath storePath=appendFileStorageClient.uploadAppenderFile(DEFAULT_GROUP,file.getInputStream(),file.getSize(),fileType);
        return storePath.getPath();
    }

    public String uploadCommonFile(MultipartFile file) throws IOException {
        Set<MetaData> metaDataSet=new HashSet<>();
        String fileType =this.getFileType(file);
        StorePath storePath=fastFileStorageClient.uploadFile(file.getInputStream(),file.getSize(),fileType,metaDataSet);
        return storePath.getPath();
    }


    //修改
    public void modifyAppenderFile(MultipartFile file,String filePath,long offset) throws IOException {
        appendFileStorageClient.modifyFile(DEFAULT_GROUP,filePath,file.getInputStream(),file.getSize(),offset);
    }
    private static final String PATH_KEY="path-key:";

    private static final String UPLOADED_SIZE_KEY="uploadedSizeKey:";
    private static final String UPLOADED_NO_KEY="uploadedNoKey:";
    private static final int SLICE_SIZE=1024*1024*1;//1mb

    public String uploadFileBySlices(MultipartFile file,String fileMd5,Integer sliceNo,Integer totalSliceNo) throws IOException {
        File dbFileMD5=fileDao.getFileByMD5(fileMd5);

        if(file==null||sliceNo==null||totalSliceNo==null){
            throw new ConditionException("参数异常");
        }
        //MD5加密
        String pathKey = PATH_KEY+fileMd5;
        String uploadedSizeKey=UPLOADED_SIZE_KEY+fileMd5;
        String uploadedNoKey=UPLOADED_NO_KEY+fileMd5;
        String uploadedSizeStr = redisTemplate.opsForValue().get(uploadedSizeKey);
        Long uploadedSize=0L;
        if(!StringUtil.isNullOrEmpty(uploadedSizeStr)){
            uploadedSize=Long.valueOf(uploadedSizeStr);
        }
        String fileType=this.getFileType(file);
        if(sliceNo==1){//上传的是第一个分片
            String path = this.uploadAppenderFile(file);
            if(StringUtil.isNullOrEmpty(path)){
                throw new ConditionException("上传失败！");
            }
            redisTemplate.opsForValue().set(pathKey, path);
            redisTemplate.opsForValue().set(uploadedNoKey, "1");
        }else{
            String filePath=redisTemplate.opsForValue().get(pathKey);
            if(StringUtil.isNullOrEmpty(filePath)){
                throw new ConnectException("获取路径失败");
            }
            this.modifyAppenderFile(file,filePath,uploadedSize);
            //更新
                //序号加一
            redisTemplate.opsForValue().increment(uploadedNoKey);
        }
        //修改历史上传文件大小
        uploadedSize+=file.getSize();
        redisTemplate.opsForValue().set(uploadedSizeKey,String.valueOf(uploadedSize));
        //判断是否可以结束--比较序号--上传完毕删除redis里面的key
        String uploadedNoStr = redisTemplate.opsForValue().get(uploadedNoKey);
        assert uploadedNoStr != null;
        Integer uploadedNo = Integer.valueOf(uploadedNoStr);
        String resultPath="";
        if(uploadedNo.equals(totalSliceNo)){
            resultPath = redisTemplate.opsForValue().get(pathKey);

            List<String> KeyList= Arrays.asList(uploadedNoKey,pathKey,uploadedSizeKey);
            redisTemplate.delete(KeyList);
        }
        return resultPath;
    }

    //分片
    public void convertFileToSlices(MultipartFile multipartFile) throws IOException {
        String fileName =multipartFile.getOriginalFilename();
        String fileType=this.getFileType(multipartFile);
        File file =this.multipartFileToFile(multipartFile);
        long fileLength=file.length();
        int count=1;
        //分片操作
        for(int i=0;i<fileLength;i+=SLICE_SIZE){
            //RandomAccessFile程序可以直接跳转到文件的任意地方来读写数据
            RandomAccessFile randomAccessFile=new RandomAccessFile(file,"r");
            randomAccessFile.seek(i);
            byte[] bytes=new byte[SLICE_SIZE];
            int len = randomAccessFile.read(bytes);
            String path="src/main/resources/SlipVideo/"+count+"."+fileType;
            File slice=new File(path);
            FileOutputStream fos=new FileOutputStream(slice);
            fos.write(bytes,0,len);
            fos.close();
            randomAccessFile.close();
            count++;
        }
        file.delete();
    }
    public File multipartFileToFile(MultipartFile multipartFile) throws IOException {
        String originalFileName=multipartFile.getOriginalFilename();
        assert originalFileName != null;
        String[] fileName=originalFileName.split("\\.");
        System.err.println(fileName[0]+" "+ fileName[1]);
        File file=File.createTempFile(fileName[0],"."+fileName[1]);//生成临时文件
        multipartFile.transferTo(file);
        return file;
    }


    public void deleteFile(String filePath){
        fastFileStorageClient.deleteFile(filePath);
    }

//    视频在线播放
    @Value("${fdfs.http.storage-addr}")//引入pro中的数据
    private String  httpFdfsStorageAddr;

    public void viewVideoOnlineSlices(HttpServletRequest request, HttpServletResponse response, String url) throws Exception {
        FileInfo fileInfo = fastFileStorageClient.queryFileInfo(DEFAULT_GROUP, url);//获取文件信息
        long totalFileSize=fileInfo.getFileSize();
        String urls=httpFdfsStorageAddr+url;
        Enumeration<String> headerNames = request.getHeaderNames();
        Map<String,Object >headers=new HashMap<>();
        while (headerNames.hasMoreElements()){
            String header=headerNames.nextElement();
            headers.put(header,request.getHeader(header));
        }
        String rangeStr= request.getHeader("Range");
        String[] range;
        if(StringUtil.isNullOrEmpty(rangeStr)){
            rangeStr="bytes=0-"+(totalFileSize-1);
        }
        range=rangeStr.split("bytes=|-");
        long begin=0;
        if(range.length>=2){
            begin=Long.parseLong(range[1]);
        }
        long end =totalFileSize-1;
        if(range.length>=2){
            end=Long.parseLong(range[2]);
        }
        long len=(end-begin)+1;
        String contentRange="bytes "+begin+"-"+end+"/"+totalFileSize;
        response.setHeader("Content-Range",contentRange);
        response.setHeader("Accept-Ranges","bytes");
        response.setHeader("Content-Type","video/mp4");
        response.setContentLength((int)len);
        response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
        HttpUtil.get(url,headers,response);
    }
    //下载*

}
