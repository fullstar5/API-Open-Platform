package com.star.starApiCommon.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.star.starApiCommon.model.entity.User;


/**
 * 用户服务
 *
 * @author yifei
 */
public interface InnerUserService{

    User getInvokeUser(String ak);
}
