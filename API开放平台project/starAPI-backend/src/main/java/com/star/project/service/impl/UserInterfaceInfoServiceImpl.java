package com.star.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.star.project.common.ErrorCode;
import com.star.project.exception.BusinessException;
import com.star.project.mapper.UserInterfaceInfoMapper;
import com.star.project.service.UserInterfaceInfoService;
import com.star.starApiCommon.model.entity.UserInterfaceInfo;
import org.springframework.stereotype.Service;

/**
* @author Administrator
* @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Service实现
* @createDate 2023-02-15 20:23:12
*/
@Service
public class UserInterfaceInfoServiceImpl extends ServiceImpl<UserInterfaceInfoMapper, UserInterfaceInfo>
    implements UserInterfaceInfoService {

    @Override
    public void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean add) {
        if (userInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 创建时，所有参数必须非空
        if (add) {
            if (userInterfaceInfo.getInterfaceInfoId() <= 0|| userInterfaceInfo.getUserId() <= 0){
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "Interface or User not exist");
            }
        }
        if (userInterfaceInfo.getLeftNum() < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "left number too small");
        }
    }

    @Override
    public boolean invokeCount(long interfaceInfoId, long userId) {
        if (interfaceInfoId <= 0 || userId <= 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Interface or User not exist");
        }
        UpdateWrapper<UserInterfaceInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("interfaceInfoId", interfaceInfoId);
        updateWrapper.eq("userId", userId);
//        updateWrapper.gt("leftNum", 0);
        updateWrapper.setSql("leftNum = leftNum - 1, totalNum = totalNum + 1");
        boolean update = this.update(updateWrapper);
        return update;
    }


}




