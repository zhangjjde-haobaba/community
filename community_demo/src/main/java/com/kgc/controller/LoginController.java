package com.kgc.controller;

import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kgc.aspect.LogAspect;
import com.kgc.entity.SysUser;
import com.kgc.form.LoginForm;
import com.kgc.service.SysUserService;
import com.kgc.util.JwtUtil;
import com.kgc.util.Result;
import com.wf.captcha.SpecCaptcha;
import org.apache.catalina.Session;
import org.apache.catalina.security.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.kgc.aspect.LogAspect.sysUser;

/**
 * Created by jiang on 7/25/23 6:50 PM
 */
@RestController
public class LoginController {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/captcha")
    public Result captcha(){
        SpecCaptcha specCaptcha = new SpecCaptcha(130, 48, 4);
        String code = specCaptcha.text().toLowerCase();
        String uuid = IdUtil.simpleUUID();
        //存入redis并设置过期时间为两分钟
        redisTemplate.opsForValue().set(uuid,code,120, TimeUnit.SECONDS);
        Map<String, String> map = new HashMap<String, String>(3);
        map.put("uuid", uuid);
        map.put("code", code);
        map.put("captcha", specCaptcha.toBase64());

        return Result.ok().put("data",map);
    }

    @PostMapping("/login")
    public Result login(@RequestBody LoginForm loginForm, HttpSession session){
        //requestbody将前端传入的json对象转化为自己定义的对象

        //1.验证码校验
        String code = (String) this.redisTemplate.opsForValue().get(loginForm.getUuid());
        //判断验证码是否有效
        if(code == null){
            //返回ok表示已经成功连接上了
            return Result.ok().put("status","fail").put("data","验证码已过期");
        }
        if(!code.equals(loginForm.getCaptcha())){
            return Result.ok().put("status","fail").put("data","验证码不正确");
        }

        //2.查询用户名是否正确
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",loginForm.getUsername());
        SysUser user = this.sysUserService.getOne(queryWrapper);
        if(user == null){
            return Result.ok().put("status","fail").put("data","用户名不存在");
        }
        //3.判断密码是否正确
        //因为数据库存的是暗文密码 传入的是明文密码 所以要先将明文密码加密再进行比较
        String pwd = SecureUtil.sha256(loginForm.getPassword());
        if(!pwd.equals(user.getPassword())){
            return Result.ok().put("status","fail").put("data","密码不正确");
        }
        //4.判断用户是否可用
        if(user.getStatus()==0){
            return Result.ok().put("status","fail").put("data","当前账号已被锁定, 请联系管理员");
        }
        //登陆成功 存储到session中
        session.setAttribute("user",user);

        //创建token
        String token = this.jwtUtil.createToken(String.valueOf(user.getUserId()));
        //放入到redis中
        this.redisTemplate.opsForValue().set("communityuser-:"+user.getUserId(),token, jwtUtil.getExpire());

        Map<String, Object> map = new HashMap<>();
        map.put("token",token);
        map.put("expire",jwtUtil.getExpire());
        LogAspect.sysUser = user; //用作日志处理

        return Result.ok().put("status","success").put("data",map);

    }

    /**
     * 校验token
     */
    @GetMapping("/checkToken")
    public Result checkToken(HttpServletRequest request){
        //从请求头中获取token
        String token = request.getHeader("token");
        boolean result = this.jwtUtil.checkToken(token);
        if(result) return Result.ok().put("status","ok");
        return Result.ok().put("status","error");
    }

    @PostMapping("/logout")
    public Result logout(HttpSession session){
        session.invalidate();
        return Result.ok();
    }

}
