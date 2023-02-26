package com.star.project.service.impl.inner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.star.project.common.ErrorCode;
import com.star.project.exception.BusinessException;
import com.star.project.mapper.UserMapper;
import com.star.starApiCommon.model.entity.User;
import com.star.starApiCommon.service.InnerUserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

@DubboService
public class InnerUserServiceImpl implements InnerUserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public User getInvokeUser(String ak) {
        if (StringUtils.isAnyBlank(ak)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("accessKey", ak);
        User user = userMapper.selectOne(queryWrapper);
        return user;
    }
}
