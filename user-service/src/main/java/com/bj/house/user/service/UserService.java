package com.bj.house.user.service;

import com.alibaba.fastjson.JSON;
import com.bj.house.user.exception.UserException;
import com.bj.house.user.mapper.UserMapper;
import com.bj.house.user.model.User;
import com.bj.house.user.utils.BeanHelper;
import com.bj.house.user.utils.HashUtils;
import com.bj.house.user.utils.JwtHelper;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by BJ on 2018/2/9.
 */
@Service
public class UserService {

    //查询操作是个高访问高并发的操作，需要减轻数据库的压力
    //使用Redis

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private MailService mailService;

    @Autowired
    private UserMapper userMapper;

    @Value("${file.prefix}")
    private String imgPrefix;

    /**
     * 0.首先通过缓存获取
     * 1.不存在将通过数据库获得用户对象
     * 2.将用户对象写入缓存，设置失效时间
     * 3.返回对象
     * @param id
     * @return
     */
    public User getUserById(Long id) {
        //key 格式 user:id
        String key = "user:"+id;
        String tJson = redisTemplate.opsForValue().get(key);
        User user = null;
        //如果为空则从缓存中获取
        if (Strings.isNullOrEmpty(tJson)){
            user = userMapper.selectById(id);
            //设置头像路径
            user.setAvatar(imgPrefix + user.getAvatar());
            //储存至缓存中，设置失效时间
            String userStr = JSON.toJSONString(user);
            redisTemplate.opsForValue().set(key,userStr);
            redisTemplate.expire(key,5, TimeUnit.MINUTES);
        }else {
            user = JSON.parseObject(tJson, User.class);
        }
        return user;
    }

    /**
     * 查询用户列表
     * @param user
     * @return
     */
    public List<User> getUserByQuery(User user) {
        List<User> users = userMapper.select(user);
        //为每个用户设置头像地址
        users.forEach(user1 -> {
            user1.setAvatar(imgPrefix + user1.getAvatar());
        });
        return users;
    }

    /**
     * 注册：添加用户
     * @param user
     * @param enableUrl
     */
    public void addAccount(User user, String enableUrl) {
        user.setPasswd(HashUtils.encryPassword(user.getPasswd()));
        BeanHelper.onInsert(user);
        userMapper.insert(user);
        registerNotify(user.getEmail(),enableUrl);
    }

    //注册用户邮件和Key的缓存绑定
    private void registerNotify(String email, String enableUrl) {
        String randomKey = HashUtils.hashString(email) + RandomStringUtils.randomAlphabetic(10);
        redisTemplate.opsForValue().set(randomKey,email);
        redisTemplate.expire(randomKey,1,TimeUnit.HOURS); //时限1小时
        String content = enableUrl + "?key=" + randomKey; //生成激活链接
        //发送激活邮件
        mailService.sendSimpleMail("House激活邮件：",content,email);
        //fixme 临时Print激活邮件
        System.out.println("激活邮件地址："+content);
    }

    //激活功能
    public boolean enable(String key) {
        String email = redisTemplate.opsForValue().get(key);
        //如果不存在，说明激活邮件不存在或已经过期
        if (StringUtils.isBlank(email)){
            throw new UserException(UserException.Type.USER_NOT_FOUND,"无效的key");
        }
        User updateUser = new User();
        updateUser.setEmail(email);
        updateUser.setEnable(1);
        userMapper.update(updateUser);
        return true;
    }

    /**
     * 校验用户名密码，生成Token并返回用户对象
     * @param email
     * @param passwd
     * @return
     */
    public User auth(String email, String passwd) {
        if (StringUtils.isBlank(email) || StringUtils.isBlank(passwd)){
            System.out.println();
            throw new UserException(UserException.Type.USER_AUTH_FAIL,"User Auth Fail.");
        }
        User user = new User();
        user.setEmail(email);
        user.setPasswd(HashUtils.encryPassword(passwd));
        user.setEnable(1); //只去查询激活的用户
        List<User> list = getUserByQuery(user);
        if (!list.isEmpty()){
            User rtnUser = list.get(0);
            onLogin(rtnUser);
            return rtnUser;
        }
        throw new UserException(UserException.Type.USER_AUTH_FAIL,"User Auth Fail.");

    }

    //自由调整Token失效时间,使用Redis控制Token时间，因JWT失效时间固定
    private String renewToken(String token, String email){
        redisTemplate.opsForValue().set(email, token);
        redisTemplate.expire(email,30,TimeUnit.MINUTES);
        return token;
    }

    //JWT 生成Token 设置token
    private void onLogin(User user) {
        //不可输入密码等敏感信息,明文
        String token = JwtHelper.getToken(ImmutableMap.of(
                "email", user.getEmail(),
                "name", user.getName(),
                "ts", Instant.now().getEpochSecond() + ""));

        renewToken(token, user.getEmail());

        user.setToken(token);
    }

    public User getLoginUserByToken(String token) {
        Map<String,String> map = null;

        try {
            map = JwtHelper.verifyToken(token);
        } catch (Exception e) {
            throw new UserException(UserException.Type.USER_NOT_LOGIN, "User not Login.");
        }
        //获取Email
        String email = map.get("email");
        //查看该token还有多少剩余时间
        Long expire = redisTemplate.getExpire(email);
        if (expire > 0){
            //如果token还生效，自动续约
            renewToken(token,email);
            User user = getUserByEmail(email);
            //需要再次将Token传回去
            user.setToken(token);
            return user;
        }

        throw new UserException(UserException.Type.USER_NOT_LOGIN, "User not Login.");
    }

    //通过Email获取用户信息
    private User getUserByEmail(String email) {
        User user = new User();
        user.setEmail(email);
        List<User> list = getUserByQuery(user);
        if (!list.isEmpty()){
            return list.get(0);
        }
        throw new UserException(UserException.Type.USER_NOT_FOUND, "User not found for : " + email);
    }

    //将Token失效
    public void invalidate(String token) {
        Map<String,String> map = JwtHelper.verifyToken(token);
        redisTemplate.delete(map.get("email"));
    }
}
