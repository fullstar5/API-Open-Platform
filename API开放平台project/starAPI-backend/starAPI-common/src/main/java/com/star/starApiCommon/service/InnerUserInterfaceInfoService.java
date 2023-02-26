package com.star.starApiCommon.service;


import com.star.starApiCommon.model.entity.UserInterfaceInfo;

/**
* @author Administrator
* @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Service
* @createDate 2023-02-15 20:23:12
*/
public interface InnerUserInterfaceInfoService {


    boolean invokeCount(long interfaceInfoId, long userId);


}
