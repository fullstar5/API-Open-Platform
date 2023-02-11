package com.star.starapiinterface;

import com.star.starapiclientsdk.client.StarApiClient;
import com.star.starapiclientsdk.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class StarApiInterfaceApplicationTests {


    @Resource
    private StarApiClient starApiClient;

    @Test
    void contextLoads() {
        User user = new User();
        user.setUsername("star2");
        String usernamebyPost = starApiClient.getUsernameByPost(user);
        System.out.println(usernamebyPost);
    }

}
