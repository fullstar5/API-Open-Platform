package com.star.project.model.dto.interfaceInfo;

import lombok.Data;

import java.io.Serializable;

/**
 * invoke request
 *
 * @TableName product
 */
@Data
public class InterfaceInfoInvokeRequest implements Serializable {
    /**
     * 主键
     */
    private Long id;

    /**
     * user request params
     */
    private String userRequestParams;

    private static final long serialVersionUID = 1L;
}