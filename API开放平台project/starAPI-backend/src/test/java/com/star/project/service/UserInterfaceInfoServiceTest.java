package com.star.project.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.Assert.*;

@SpringBootTest
public class UserInterfaceInfoServiceTest {
    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;

    @Test
    public void invokeCount() {
        userInterfaceInfoService.invokeCount(8L, 1L);
    }
}