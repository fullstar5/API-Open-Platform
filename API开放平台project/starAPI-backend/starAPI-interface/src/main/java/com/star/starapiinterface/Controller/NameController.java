package com.star.starapiinterface.Controller;

import com.star.starapiclientsdk.model.User;
import com.star.starapiclientsdk.utils.SignUtils;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/name")
public class NameController {
    @GetMapping("/get")
    public String getNameByGet(String name, HttpServletRequest request) {
        System.out.println(request.getHeader("star"));
        return "GET 你的名字是" + name;
    }

    @PostMapping("/post")
    public String getNameByPost(@RequestParam String name) {
        return "POST 你的名字是" + name;
    }


    @PostMapping("/user")
    public String getUsernameByPost(@RequestBody User user, HttpServletRequest request){
        String accessKey = request.getHeader("accessKey");
        String nonce = request.getHeader("nonce");
        String timestamp = request.getHeader("timestamp");
        String sign = request.getHeader("sign");
        String body = request.getHeader("body");
        String secretKey = request.getHeader("secretKey");

        // todo 实际情况应该是去数据库中查是否已分配给用户
        if (!accessKey.equals("star")){
            throw new RuntimeException("ak or sk wrong");
        }
        if (Long.parseLong(nonce) > 10000) {
            throw new RuntimeException("no authorised");
        }

//         todo 时间和当前时间不能超过 5 分钟
//        if (timestamp) {
//
//        }
//         todo 实际情况中是从数据库中查出 secretKey
        String serverSign = SignUtils.genSign(body, "1234");
        if (!sign.equals(serverSign)) {
            throw new RuntimeException("无权限");
        }
        // todo 调用次数 + 1 invokeCount

        String result = "POST 用户名字是" + user.getUsername();
        return result;

    }

}
