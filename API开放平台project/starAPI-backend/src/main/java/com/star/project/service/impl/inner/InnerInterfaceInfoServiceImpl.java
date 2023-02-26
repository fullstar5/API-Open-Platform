package com.star.project.service.impl.inner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.star.project.common.ErrorCode;
import com.star.project.exception.BusinessException;
import com.star.project.mapper.InterfaceInfoMapper;
import com.star.starApiCommon.model.entity.InterfaceInfo;
import com.star.starApiCommon.service.InnerInterfaceInfoService;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

@DubboService
public class InnerInterfaceInfoServiceImpl implements InnerInterfaceInfoService {

    @Resource
    private InterfaceInfoMapper interfaceInfoMapper;

    @Override
    public InterfaceInfo getInterfaceInfo(String path, String method) {
        if (StringUtils.isAnyBlank(path, method)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("url", path);
        queryWrapper.eq("method", method);
        InterfaceInfo interfaceInfo = interfaceInfoMapper.selectOne(queryWrapper);
        return interfaceInfo;
    }
}
