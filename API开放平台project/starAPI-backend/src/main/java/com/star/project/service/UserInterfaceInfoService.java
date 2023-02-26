package com.star.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.star.starApiCommon.model.entity.UserInterfaceInfo;

/**
* @author Administrator
* @description 针对表【interface_info(接口信息)】的数据库操作Service
* @createDate 2023-02-08 15:55:36
*/
public interface UserInterfaceInfoService extends IService<UserInterfaceInfo> {

    void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean add);

    boolean invokeCount(long interfaceInfoId, long userId);
}
