package com.star.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.star.starApiCommon.model.entity.InterfaceInfo;

/**
* @author Administrator
* @description 针对表【interface_info(接口信息)】的数据库操作Service
* @createDate 2023-02-08 15:55:36
*/
public interface InterfaceInfoService extends IService<InterfaceInfo> {

    void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add);


}
