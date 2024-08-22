package com.imooc.bilbil.service;

import com.alibaba.fastjson.JSONObject;
import com.imooc.bilbil.dao.UserDao;
import com.imooc.bilbil.domain.*;
import com.imooc.bilbil.domain.constant.UserConstant;
import com.imooc.bilbil.domain.exception.ConditionException;
import com.imooc.bilbil.service.util.MD5Util;
import com.imooc.bilbil.service.util.RSAUtil;
import com.imooc.bilbil.service.util.TokenUtil;
import com.mysql.cj.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service//@Service注解用于类上，标记当前类是一个service类，加上该注解会将当前类自动注入到spring容器中，不需要再在applicationContext.xml文件定义bean了。
//等于Component
public class UserService {
@Autowired
    private UserDao userDao;
@Autowired
private UserAuthService userAuthService;//用于添加默认角色

    public  void updateUsers(User user) throws Exception {
        Long id= user.getId();
        User dbUser=userDao.getUserById(id);
        if(dbUser == null){
            throw new ConditionException("用户不存在");

        }
        if(! StringUtils.isNullOrEmpty(user.getPassword())){
            String rawPassword = RSAUtil.decrypt(user.getPassword());
            String md5Password = MD5Util.sign(rawPassword,dbUser.getSalt(),"UTF-8");
            user.setPassword(md5Password);
        }
        user.setUpdateTime(new Date());
        userDao.updateUsers(user);
    }

    public void addUser(User user) {
        String phone=user.getPhone();
        if(StringUtils.isNullOrEmpty(phone)){
            throw new ConditionException("手机号不能为空");
        }
        User dbUser =this.getUserByPhone(phone);
        if(dbUser!=null){
            throw new ConditionException("手机号已经被注册");
        }
        //获取当前的时间戳,用于对当前数据进行MD5加密
        Date now= new Date();
        String salt=String .valueOf(now.getTime());
        //获取当前的加密的密码
        String password=user.getPassword();
        //进行解密
        String rawPassword;
        try {
            rawPassword = RSAUtil.decrypt(password);
        } catch (Exception e) {
            throw new ConditionException("解密失败");
        }
        //重新进行MD5加密
        String md5Password= MD5Util.sign(rawPassword,salt,"UTF-8");
        user.setSalt(salt);
        user.setPassword(md5Password);
        user.setCreateTime(now);
        userDao.addUser(user);
        //添加用户信息
        UserInfo userInfo =new UserInfo();
        userInfo.setUserId(user.getId());
        userInfo.setNick(UserConstant.DEFAULT_NICK);
        userInfo.setBirth(UserConstant.DEFAULT_BIRTH);
        userInfo.setGender(UserConstant.GENDER_MALE);
        userInfo.setCreateTime(now);
        userDao.addUserInfo(userInfo);
        //添加用户默认权限
        userAuthService.addUserDefualtRole(user.getId());



    }
    public User getUserByPhone(String phone){
        return userDao.getUserByPhone(phone);
    }

    public String login(User user) throws Exception {
        String phone =user.getPhone()==null ? "" :user.getPhone();
        String email = user.getEmail()==null ? "":user.getEmail();

        if(StringUtils.isNullOrEmpty(phone) &&StringUtils.isNullOrEmpty(email)){
            throw new ConditionException("参数异常");
        }
        String phoneOrEmail=phone+email;


        User dbUser=userDao.getUserByPhoneOrEmail(phoneOrEmail);
        System.out.println(dbUser);
        if(dbUser==null){
            throw new ConditionException("当前用户不存在");
        }
        //对密码解密
        String password=user.getPassword();
        //作为解密之后的密码
        String rawPassword;
        try {
            rawPassword = RSAUtil.decrypt(password);
        } catch (Exception e) {
            throw new ConditionException("密码解密失败");
        }
        String salt=dbUser.getSalt();
        String md5Password=MD5Util.sign(rawPassword,salt,"UTF-8");
        if(!md5Password.equals(dbUser.getPassword())){
            throw new ConditionException("密码错误");
        }
        //登入成功
        return TokenUtil.generateToken(dbUser.getId());

    }

    //功能
    public User getUserInfo(Long userId) {
        User user= userDao.getUserById(userId);
        UserInfo userInfo=userDao.getUserInfoById(userId);
        user.setUserInfo(userInfo);
        return user;
    }

    public void updateUserInfos(UserInfo userInfo) {

        userDao.updateUserInfos(userInfo);

    }

    public User getUserById(Long followingId) {
        User user=userDao.getUserById(followingId);
        return user;
    }

    public List<UserInfo> getUserInfoByUserIds(Set<Long> followingSet) {
        return userDao.getUserInfoByUserIds(followingSet);
    }

    public PageResult<UserInfo> pageListInfos(JSONObject params) {
        Integer no=params.getInteger("no");
        Integer size=params.getInteger("size");
        params.put("start",(no-1)*size);//从第几条数据开始查
        params.put("limit",size);
        //先判断满足用户条件的信息有多少条
        Integer total=userDao.pageCountUserInfos(params);
        List<UserInfo> list=new ArrayList<>();
        if(total>0){
            //开始分页查询
            list=userDao.pageListUserInfos(params);
        }
        return new PageResult<>(total,list);
    }

    public Map<String, Object> loginForDts(User user) throws Exception{
        String phone =user.getPhone()==null ? "" :user.getPhone();
        String email = user.getEmail()==null ? "":user.getEmail();

        if(StringUtils.isNullOrEmpty(phone) &&StringUtils.isNullOrEmpty(email)){
            throw new ConditionException("参数异常");
        }
        String phoneOrEmail=phone+email;


        User dbUser=userDao.getUserByPhoneOrEmail(phoneOrEmail);
        System.out.println(dbUser);
        if(dbUser==null){
            throw new ConditionException("当前用户不存在");
        }
        //对密码解密
        String password=user.getPassword();
        //作为解密之后的密码
        String rawPassword;
        try {
            rawPassword = RSAUtil.decrypt(password);
        } catch (Exception e) {
            throw new ConditionException("密码解密失败");
        }
        String salt=dbUser.getSalt();
        String md5Password=MD5Util.sign(rawPassword,salt,"UTF-8");
        if(!md5Password.equals(dbUser.getPassword())){
            throw new ConditionException("密码错误");
        }
        Long userId= dbUser.getId();
        //登入成功
        String accessToken= TokenUtil.generateToken(userId);
        String refreshToken=TokenUtil.generateRefreshToken(userId);
//        保存数据库
        userDao.deleteRefreToken(refreshToken,userId);
        userDao.addRefreshToken(refreshToken,userId,new Date());
        Map<String,Object> result=new HashMap<>();
        result.put("accessToken",accessToken);
        result.put("refreshToken",refreshToken);
        return result;
    }

    public void logout(String refreshToken, Long userId) {
        userDao.deleteRefreToken(refreshToken,userId);

    }

    public String refreshAccessToken(String refreshToken) throws Exception {
        RefreshTokenDetail refreshTokenDetail=userDao.getRefreshTokenDetail(refreshToken);
//        判断是否有值
//        如果没有值就是失效状态时不要刷新了
//        如果有值就可以延长时间
        if(refreshTokenDetail==null){
            System.err.println("refreshToken过期了");
            throw new ConditionException("555","token过期了");
        }
        Long userId=refreshTokenDetail.getUserId();
        return TokenUtil.generateToken(userId);
    }
    public List<UserInfo> batchGetUserInfoByUserIds(Set<Long> userIdList) {
        return userDao.batchGetUserInfoByUserIds(userIdList);
    }

}
